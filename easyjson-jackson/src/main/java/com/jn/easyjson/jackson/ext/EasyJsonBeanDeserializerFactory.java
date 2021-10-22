/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache, Version 2.0 (the "License");
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
import com.fasterxml.jackson.databind.deser.BasicDeserializerFactory;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.MapType;
import com.jn.easyjson.core.exclusion.ExclusionConfiguration;
import com.jn.easyjson.jackson.JacksonMigrates;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EasyJsonBeanDeserializerFactory extends BeanDeserializerFactory {
    private EasyJsonObjectMapper objectMapper;

    EasyJsonBeanDeserializerFactory(DeserializerFactoryConfig config, EasyJsonObjectMapper objectMapper) {
        super(config);
        this.objectMapper = objectMapper;
        configureMapLikeInterfaceDefaultImplementClass();
    }

    @Override
    public DeserializerFactory withConfig(DeserializerFactoryConfig config) {
        if (_factoryConfig == config) {
            return this;
        }

        JacksonMigrates.verifyMustOverride(EasyJsonBeanDeserializerFactory.class, this, "withConfig");
        return new EasyJsonBeanDeserializerFactory(config, objectMapper);
    }

    @Override
    protected List<BeanPropertyDefinition> filterBeanProps(DeserializationContext ctxt,
                                                           BeanDescription beanDesc, BeanDeserializerBuilder builder,
                                                           List<BeanPropertyDefinition> propDefsIn,
                                                           Set<String> ignored)
            throws JsonMappingException {
        List<BeanPropertyDefinition> propertyDefinitions = super.filterBeanProps(ctxt, beanDesc, builder, propDefsIn, ignored);
        if (objectMapper != null) {
            // 应用排除规则
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

    // override _mapFallbacks
    final static HashMap<String, Class<? extends Map>> _mapFallbacks;
    static {
        HashMap<String, Class<? extends Map>> fallbacks = new HashMap();

        final Class<? extends Map> DEFAULT_MAP = LinkedHashMap.class;
        fallbacks.put(Map.class.getName(), DEFAULT_MAP);
        fallbacks.put(AbstractMap.class.getName(), DEFAULT_MAP);
        fallbacks.put(ConcurrentMap.class.getName(), ConcurrentHashMap.class);
        fallbacks.put(SortedMap.class.getName(), TreeMap.class);

        fallbacks.put(java.util.NavigableMap.class.getName(), TreeMap.class);
        fallbacks.put(java.util.concurrent.ConcurrentNavigableMap.class.getName(), java.util.concurrent.ConcurrentSkipListMap.class);
        fallbacks.put(MultiValueMap.class.getName(), LinkedMultiValueMap.class);
        _mapFallbacks = fallbacks;
    }

    /**
     * @override jackson [2.11.0, )
     */
    protected MapType _mapAbstractMapType(JavaType type, DeserializationConfig config){
        final Class<?> mapClass = _mapFallbacks.get(type.getRawClass().getName());
        if (mapClass != null) {
            return (MapType) config.getTypeFactory().constructSpecializedType(type, mapClass);
        }
        return null;
    }


    // Jackson [2.6.0 ~ 2.11.0)
    private void configureMapLikeInterfaceDefaultImplementClass() {
        Field _mapFallbacksField = Reflects.getStaticField(BasicDeserializerFactory.class, "_mapFallbacks");
        HashMap<String, Class<? extends Map>> _mapFallbacks = null;
        if (_mapFallbacksField != null) {
            _mapFallbacks = Reflects.getFieldValue(_mapFallbacksField, null, true, true);
        }
        if (_mapFallbacks != null) {
            _mapFallbacks.put(MultiValueMap.class.getName(), LinkedMultiValueMap.class);
        }
    }


    @Override
    protected BeanDeserializerBuilder constructBeanDeserializerBuilder(DeserializationContext ctxt, BeanDescription beanDesc) {
        return new EasyJsonBeanDeserializerBuilder(beanDesc, ctxt);
    }

}
