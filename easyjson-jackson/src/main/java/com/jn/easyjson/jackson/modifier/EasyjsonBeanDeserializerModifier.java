/*
 * Copyright 2020 the original author or authors.
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

package com.jn.easyjson.jackson.modifier;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.jn.easyjson.jackson.DateSeries;
import com.jn.easyjson.jackson.deserializer.BooleanDeserializer;
import com.jn.easyjson.jackson.deserializer.DateDeserializer;
import com.jn.easyjson.jackson.deserializer.EnumDeserializer;
import com.jn.easyjson.jackson.deserializer.NumberDeserializer;
import com.jn.langx.util.reflect.type.Types;

public class EasyjsonBeanDeserializerModifier extends BeanDeserializerModifier {

    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
        Class beanClass = beanDesc.getBeanClass();
        if (Boolean.class == beanClass || boolean.class == beanClass) {
            return new BooleanDeserializer();
        }
        if (Types.isPrimitive(beanClass)) {
            if (Number.class.isAssignableFrom(Types.getPrimitiveWrapClass(beanClass))) {
                return new NumberDeserializer(beanClass);
            }
        }
        if (DateSeries.isSupported(beanClass)) {
            return new DateDeserializer(beanClass);
        }
        if (beanClass.isEnum()) {
            return new EnumDeserializer(beanClass);
        }
        return super.modifyDeserializer(config, beanDesc, deserializer);
    }
}
