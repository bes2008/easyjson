/*
 * Copyright 2020 the original author or authors.
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
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.jn.easyjson.jackson.JacksonMigrates;

public class EasyjsonPropertyBuilder extends PropertyBuilder {
    private EasyJsonObjectMapper objectMapper;

    public EasyjsonPropertyBuilder(SerializationConfig config, BeanDescription beanDesc) {
        super(config, beanDesc);
    }

    public void setObjectMapper(EasyJsonObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected BeanPropertyWriter buildWriter(SerializerProvider prov, BeanPropertyDefinition propDef, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, TypeSerializer contentTypeSer, AnnotatedMember am, boolean defaultUseStaticTyping) throws JsonMappingException {
        JavaType serializationType;
        try {
            serializationType = findSerializationType(am, defaultUseStaticTyping, declaredType);
        } catch (JsonMappingException e) {
            if (propDef == null) {
                return prov.reportBadDefinition(declaredType, JacksonMigrates.exceptionMessage(e));
            }
            return prov.reportBadPropertyDefinition(_beanDesc, propDef, JacksonMigrates.exceptionMessage(e));
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

        // ***************************EasyJson patch start*******************************//
        if (!suppressNulls && objectMapper != null && objectMapper.getJsonBuilder() != null) {
            suppressNulls = !objectMapper.getJsonBuilder().serializeNulls();
        }
        // ***************************EasyJson patch end*********************************//

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
}
