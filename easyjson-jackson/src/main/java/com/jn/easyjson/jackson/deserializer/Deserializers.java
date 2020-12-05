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

package com.jn.easyjson.jackson.deserializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.type.ClassKey;

public class Deserializers extends SimpleDeserializers {
    @Override
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        if (_classMappings == null) {
            return null;
        }
        JsonDeserializer<?> deser = _classMappings.get(new ClassKey(type));
        if (deser == null) {
            if (_hasEnumDeserializer && type.isEnum()) {
                deser = _classMappings.get(new ClassKey(Enum.class));
                if (deser instanceof ContextualDeserializer) {
                    deser = ((ContextualDeserializer) deser).createContextual(null, null, type);
                    if (deser != null) {
                        _classMappings.put(new ClassKey(type), deser);
                    }
                }
            }
        }
        return deser;
    }
}
