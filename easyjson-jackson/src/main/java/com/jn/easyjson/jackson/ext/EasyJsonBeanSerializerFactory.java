/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.easyjson.jackson.ext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.jn.easyjson.core.exclusion.ExclusionConfiguration;
import com.jn.easyjson.jackson.serializer.DateSerializer;
import com.jn.easyjson.jackson.serializer.NumberSerializer;
import com.jn.langx.util.reflect.type.Types;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class EasyJsonBeanSerializerFactory extends BeanSerializerFactory {
    private EasyJsonObjectMapper objectMapper;

    EasyJsonBeanSerializerFactory(SerializerFactoryConfig config, EasyJsonObjectMapper objectMapper) {
        super(config);
        this.objectMapper = objectMapper;
    }

    @Override
    public SerializerFactory withConfig(SerializerFactoryConfig config) {
        if (_factoryConfig == config) {
            return this;
        }
        if (getClass() != EasyJsonBeanSerializerFactory.class) {
            throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName()
                    + ") has not properly overridden method 'withAdditionalSerializers': cannot instantiate subtype with "
                    + "additional serializer definitions");
        }
        return new EasyJsonBeanSerializerFactory(config, objectMapper);
    }

    @Override
    protected List<BeanPropertyWriter> findBeanProperties(SerializerProvider prov, BeanDescription beanDesc, BeanSerializerBuilder builder)
            throws JsonMappingException {
        //==================EasyJson exclusion start=========================
        if (objectMapper != null && objectMapper.getJsonBuilder() != null) {
            // filter using exclusion strategies
            ExclusionConfiguration exclusionConfiguration = objectMapper.getJsonBuilder().getExclusionConfiguration();
            Class clazz = beanDesc.getType().getRawClass();
            List<BeanPropertyDefinition> properties = beanDesc.findProperties();
            if (!exclusionConfiguration.isExcludedClass(clazz, true)) {
                Iterator<BeanPropertyDefinition> iter = properties.iterator();
                while (iter.hasNext()) {
                    BeanPropertyDefinition property = iter.next();
                    if (property.hasField()) {
                        // exclusionConfiguration.isExcludedField(property.)
                        Field field = property.getField().getAnnotated();
                        if (exclusionConfiguration.isExcludedField(field, true)) {
                            iter.remove();
                        }
                    }
                }
            } else {
                properties.clear();
            }
        }
        //==================EasyJson exclusion end=========================
        return super.findBeanProperties(prov, beanDesc, builder);
    }

    @Override
    protected PropertyBuilder constructPropertyBuilder(SerializationConfig config, BeanDescription beanDesc) {
        if (!objectMapper.getJsonBuilder().serializeNulls()) {
            return new PropertyBuilder(config, beanDesc) {
                @Override
                protected BeanPropertyWriter buildWriter(SerializerProvider prov, BeanPropertyDefinition propDef, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, TypeSerializer contentTypeSer, AnnotatedMember am, boolean defaultUseStaticTyping) throws JsonMappingException {
                    // do we have annotation that forces type to use (to declared type or its super type)?
                    JavaType serializationType;
                    try {
                        serializationType = findSerializationType(am, defaultUseStaticTyping, declaredType);
                    } catch (JsonMappingException e) {
                        if (propDef == null) {
                            return prov.reportBadDefinition(declaredType, ClassUtil.exceptionMessage(e));
                        }
                        return prov.reportBadPropertyDefinition(_beanDesc, propDef, ClassUtil.exceptionMessage(e));
                    }

                    // Container types can have separate type serializers for content (value / element) type
                    if (contentTypeSer != null) {
                        // 04-Feb-2010, tatu: Let's force static typing for collection, if there is
                        //    type information for contents. Should work well (for JAXB case); can be
                        //    revisited if this causes problems.
                        if (serializationType == null) {
//                serializationType = TypeFactory.type(am.getGenericType(), _beanDesc.getType());
                            serializationType = declaredType;
                        }
                        JavaType ct = serializationType.getContentType();
                        // Not exactly sure why, but this used to occur; better check explicitly:
                        if (ct == null) {
                            prov.reportBadPropertyDefinition(_beanDesc, propDef,
                                    "serialization type " + serializationType + " has no content");
                        }
                        serializationType = serializationType.withContentTypeHandler(contentTypeSer);
                        ct = serializationType.getContentType();
                    }

                    Object valueToSuppress = null;
                    boolean suppressNulls = false;

                    // 12-Jul-2016, tatu: [databind#1256] Need to make sure we consider type refinement
                    JavaType actualType = (serializationType == null) ? declaredType : serializationType;

                    // 17-Mar-2017: [databind#1522] Allow config override per property type
                    AnnotatedMember accessor = propDef.getAccessor();
                    if (accessor == null) {
                        // neither Setter nor ConstructorParameter are expected here
                        return prov.reportBadPropertyDefinition(_beanDesc, propDef,
                                "could not determine property type");
                    }
                    Class<?> rawPropertyType = accessor.getRawType();

                    // 17-Aug-2016, tatu: Default inclusion covers global default (for all types), as well
                    //   as type-default for enclosing POJO. What we need, then, is per-type default (if any)
                    //   for declared property type... and finally property annotation overrides
                    JsonInclude.Value inclV = _config.getDefaultInclusion(actualType.getRawClass(),
                            rawPropertyType, _defaultInclusion);

                    // property annotation override

                    inclV = inclV.withOverrides(propDef.findInclusion());

                    JsonInclude.Include inclusion = inclV.getValueInclusion();
                    if (inclusion == JsonInclude.Include.USE_DEFAULTS) { // should not occur but...
                        inclusion = JsonInclude.Include.ALWAYS;
                    }
                    switch (inclusion) {
                        case NON_DEFAULT:
                            // 11-Nov-2015, tatu: This is tricky because semantics differ between cases,
                            //    so that if enclosing class has this, we may need to access values of property,
                            //    whereas for global defaults OR per-property overrides, we have more
                            //    static definition. Sigh.
                            // First: case of class/type specifying it; try to find POJO property defaults
                            Object defaultBean;

                            // 16-Oct-2016, tatu: Note: if we cannot for some reason create "default instance",
                            //    revert logic to the case of general/per-property handling, so both
                            //    type-default AND null are to be excluded.
                            //    (as per [databind#1417]
                            if (_useRealPropertyDefaults && (defaultBean = getDefaultBean()) != null) {
                                // 07-Sep-2016, tatu: may also need to front-load access forcing now
                                if (prov.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
                                    am.fixAccess(_config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
                                }
                                try {
                                    valueToSuppress = am.getValue(defaultBean);
                                } catch (Exception e) {
                                    _throwWrapped(e, propDef.getName(), defaultBean);
                                }
                            } else {
                                valueToSuppress = BeanUtil.getDefaultValue(actualType);
                                suppressNulls = true;
                            }
                            if (valueToSuppress == null) {
                                suppressNulls = true;
                            } else {
                                if (valueToSuppress.getClass().isArray()) {
                                    valueToSuppress = ArrayBuilders.getArrayComparator(valueToSuppress);
                                }
                            }
                            break;
                        case NON_ABSENT: // new with 2.6, to support Guava/JDK8 Optionals
                            // always suppress nulls
                            suppressNulls = true;
                            // and for referential types, also "empty", which in their case means "absent"
                            if (actualType.isReferenceType()) {
                                valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
                            }
                            break;
                        case NON_EMPTY:
                            // always suppress nulls
                            suppressNulls = true;
                            // but possibly also 'empty' values:
                            valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
                            break;
                        case CUSTOM: // new with 2.9
                            valueToSuppress = prov.includeFilterInstance(propDef, inclV.getValueFilter());
                            if (valueToSuppress == null) { // is this legal?
                                suppressNulls = true;
                            } else {
                                suppressNulls = prov.includeFilterSuppressNulls(valueToSuppress);
                            }
                            break;
                        case NON_NULL:
                            suppressNulls = true;
                            // fall through
                        case ALWAYS: // default
                        default:
                            // we may still want to suppress empty collections
                            if (actualType.isContainerType()
                                    && !_config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)) {
                                valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
                            }
                            break;
                    }
                    Class<?>[] views = propDef.findViews();
                    if (views == null) {
                        views = _beanDesc.findDefaultViews();
                    }

                    //========================EasyJson serialize null start=======================
                    if (!suppressNulls && objectMapper != null && objectMapper.getJsonBuilder() != null) {
                        suppressNulls = !objectMapper.getJsonBuilder().serializeNulls();
                    }
                    //========================EasyJson serialize null end=========================
                    BeanPropertyWriter bpw = new BeanPropertyWriter(propDef,
                            am, _beanDesc.getClassAnnotations(), declaredType,
                            ser, typeSer, serializationType, suppressNulls, valueToSuppress, views);

                    // How about custom null serializer?
                    Object serDef = _annotationIntrospector.findNullSerializer(am);
                    if (serDef != null) {
                        bpw.assignNullSerializer(prov.serializerInstance(am, serDef));
                    }
                    // And then, handling of unwrapping
                    NameTransformer unwrapper = _annotationIntrospector.findUnwrappingNameTransformer(am);
                    if (unwrapper != null) {
                        bpw = bpw.unwrappingWriter(unwrapper);
                    }
                    return bpw;
                }
            };
        } else {
            return new PropertyBuilder(config, beanDesc);
        }

    }


    @Override
    protected JsonSerializer<?> _createSerializer2(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping)
            throws JsonMappingException {
        Class<?> rawType = type.getRawClass();
        String clsName = rawType.getName();
        JsonSerializer<?> ser = null;
        if (Types.isPrimitive(rawType) || clsName.startsWith("java.")) {
            if (Number.class.isAssignableFrom(Types.getPrimitiveWrapClass(rawType))) {
                ser = new NumberSerializer().createContextual(prov, null, type);
                if (ser != null) {
                    return ser;
                }
            }
        }
        if (Date.class.isAssignableFrom(rawType)) {
            ser = new DateSerializer().createContextual(prov, null, type);
            if (ser != null) {
                return ser;
            }
        }
        return super._createSerializer2(prov, type, beanDesc, staticTyping);
    }
}
