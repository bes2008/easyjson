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

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.*;
import com.jn.easyjson.core.exclusion.ExclusionConfiguration;

import java.lang.reflect.Field;
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

    protected PropertyBuilder constructPropertyBuilder(SerializationConfig config, BeanDescription beanDesc){
        EasyjsonPropertyBuilder propertyBuilder = new EasyjsonPropertyBuilder(config, beanDesc);
        propertyBuilder.setObjectMapper(objectMapper);
        return propertyBuilder;
    }

}
