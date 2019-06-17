/*
 *  Copyright 2019 the original author or authors.
 *
 *  Licensed under the LGPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.github.fangjinuo.easyjson.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fangjinuo.easyjson.core.JsonException;
import com.github.fangjinuo.easyjson.core.JsonHandler;
import com.github.fangjinuo.easyjson.core.JsonTreeNode;
import com.github.fangjinuo.easyjson.core.util.type.Types;
import com.github.fangjinuo.easyjson.jackson.node.JacksonBasedJsonTreeNodeMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class JacksonAdapter implements JsonHandler {
    private ObjectMapper objectMapper;
    private JacksonBasedJsonTreeNodeMapper treeNodeMapper = new JacksonBasedJsonTreeNodeMapper();

    @Override
    public <T> T deserialize(String json, Type typeOfT) throws JsonException {
        try {
            if (Jacksons.isJacksonJavaType(typeOfT)) {
                return objectMapper.readValue(json, Jacksons.toJavaType(typeOfT));
            }
            if (Types.isPrimitive(typeOfT)) {
                return objectMapper.readValue(json, objectMapper.getTypeFactory().constructType(Types.getPrimitiveWrapClass(typeOfT)));
            }
            if (Types.isClass(typeOfT)) {
                return objectMapper.readValue(json, objectMapper.getTypeFactory().constructType(Types.toClass(typeOfT)));
            }

            if (Types.isParameterizedType(typeOfT)) {
                ParameterizedType pType = (ParameterizedType) typeOfT;
                Class<?> parametrized = Types.toClass(pType.getRawType());
                Type[] parameterTypes = pType.getActualTypeArguments();
                Class[] parameterClasses = new Class[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    parameterClasses[i] = Types.toClass(parameterTypes[i]);
                }
                return objectMapper.readValue(json, objectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses));
            }
        } catch (Throwable ex) {
            throw new JsonException(ex);
        }
        return null;
    }

    @Override
    public JsonTreeNode deserialize(String json) throws JsonException {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            return treeNodeMapper.create(jsonNode);
        }catch (Throwable ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String serialize(Object src, Type typeOfT) throws JsonException {
        try {
            if(src instanceof JsonTreeNode){
                return objectMapper.writeValueAsString(treeNodeMapper.mapping((JsonTreeNode)src));
            }
            return objectMapper.writeValueAsString(src);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
