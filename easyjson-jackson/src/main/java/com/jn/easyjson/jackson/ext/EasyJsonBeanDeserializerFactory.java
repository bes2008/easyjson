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

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.jn.easyjson.core.exclusion.ExclusionConfiguration;
import com.jn.easyjson.jackson.deserializer.DateDeserializer;
import com.jn.easyjson.jackson.deserializer.NumberDeserializer;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.Types;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EasyJsonBeanDeserializerFactory extends BeanDeserializerFactory {
    private EasyJsonObjectMapper objectMapper;

    EasyJsonBeanDeserializerFactory(DeserializerFactoryConfig config, EasyJsonObjectMapper objectMapper) {
        super(config);
        this.objectMapper = objectMapper;
    }

    @Override
    public DeserializerFactory withConfig(DeserializerFactoryConfig config) {
        if (_factoryConfig == config) {
            return this;
        }

        Method verifyMustOverrideMethod = Reflects.getAnyMethod(ClassUtil.class, "verifyMustOverride", Class.class, Object.class, String.class);
        if(verifyMustOverrideMethod!=null){
            try {
                verifyMustOverrideMethod.invoke(null, EasyJsonBeanDeserializerFactory.class, this, "withConfig");
            }catch (Throwable ex){
                Throwables.wrapAsRuntimeException(ex);
            }
        }
        return new EasyJsonBeanDeserializerFactory(config, objectMapper);
    }

    @Override
    protected JsonDeserializer<?> findStdDeserializer(DeserializationContext ctxt,
                                                      JavaType type, BeanDescription beanDesc)
            throws JsonMappingException {
        // note: we do NOT check for custom deserializers here, caller has already
        // done that
        Class<?> rawType = type.getRawClass();
        String clsName = rawType.getName();
        if (Types.isPrimitive(rawType) || clsName.startsWith("java.")) {
            // Primitives/wrappers, other Numbers:
            JsonDeserializer<?> deser = null;
            // code block append by easyjson [start]
            if (Number.class.isAssignableFrom(rawType)) {
                deser = new NumberDeserializer().createContextual(ctxt, null, Types.getPrimitiveWrapClass(rawType));
                if (deser != null) {
                    return deser;
                }
            }
            // code block append by easyjson [end]
        }
        if (Date.class.isAssignableFrom(rawType)) {
            JsonDeserializer<?> deser = new DateDeserializer().createContextual(ctxt, null, rawType);
            if (deser != null) {
                return deser;
            }
        }
        return super.findStdDeserializer(ctxt, type, beanDesc);
    }

    @Override
    protected List<BeanPropertyDefinition> filterBeanProps(DeserializationContext ctxt,
                                                           BeanDescription beanDesc, BeanDeserializerBuilder builder,
                                                           List<BeanPropertyDefinition> propDefsIn,
                                                           Set<String> ignored)
            throws JsonMappingException {
        List<BeanPropertyDefinition> propertyDefinitions = super.filterBeanProps(ctxt, beanDesc, builder, propDefsIn, ignored);
        if (objectMapper != null) {
            ExclusionConfiguration exclusionConfiguration = objectMapper.getJsonBuilder().getExclusionConfiguration();
            Class clazz = beanDesc.getType().getRawClass();
            if (!exclusionConfiguration.isExcludedClass(clazz, false)) {
                Iterator<BeanPropertyDefinition> iter = propertyDefinitions.iterator();
                while (iter.hasNext()) {
                    BeanPropertyDefinition property = iter.next();
                    if (property.hasField()) {
                        Field field = property.getField().getAnnotated();
                        if (exclusionConfiguration.isExcludedField(field, false)) {
                            iter.remove();
                        }
                    }
                }
            } else {
                propertyDefinitions.clear();
            }
        }
        return propertyDefinitions;
    }
}
