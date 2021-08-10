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

package com.jn.easyjson.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jn.easyjson.core.JsonException;
import com.jn.easyjson.core.JsonHandlerAdapter;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.JsonTreeNodes;
import com.jn.easyjson.jackson.node.JacksonToJsonMapper;
import com.jn.easyjson.jackson.node.JacksonToTreeNodeMapper;
import com.jn.langx.util.reflect.type.Types;

import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class JacksonAdapter extends JsonHandlerAdapter<ObjectMapper> {

    @Override
    public <T> T deserialize(String json, Type typeOfT) throws JsonException {
        try {
            return getDelegate().readValue(json, toJavaType(typeOfT));
        } catch (Throwable ex) {
            throw new JsonException(ex);
        }
    }

    @Override
    public <T> T deserialize(Reader reader, Type typeOfT) throws JsonException {
        try {
            return getDelegate().readValue(reader, toJavaType(typeOfT));
        } catch (Throwable ex) {
            throw new JsonException(ex);
        }
    }

    private JavaType toJavaType(Type typeOfT) {
        if (Jacksons.isJacksonJavaType(typeOfT)) {
            return Jacksons.toJavaType(typeOfT);
        }
        if (Types.isPrimitive(typeOfT)) {
            return getDelegate().getTypeFactory().constructType(Types.getPrimitiveWrapClass(typeOfT));
        }

        if (Types.isClass(typeOfT)) {
            return getDelegate().getTypeFactory().constructType(Types.toClass(typeOfT));
        }

        if (Types.isParameterizedType(typeOfT)) {
            ParameterizedType pType = (ParameterizedType) typeOfT;
            Class<?> parametrized = Types.toClass(pType.getRawType());
            Type[] parameterTypes = pType.getActualTypeArguments();
            JavaType[] parameterClasses = new JavaType[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                parameterClasses[i] = toJavaType(parameterTypes[i]);
            }
            return getDelegate().getTypeFactory().constructParametricType(parametrized, parameterClasses);
        }
        return Jacksons.toJavaType(typeOfT);
    }

    @Override
    public JsonTreeNode deserialize(String json) throws JsonException {
        try {
            JsonNode jsonNode = getDelegate().readTree(json);
            return JsonTreeNodes.fromJavaObject(jsonNode, new JacksonToTreeNodeMapper());
        } catch (Throwable ex) {
            throw new JsonException(ex);
        }
    }

    @Override
    public String serialize(Object src, Type typeOfT) throws JsonException {
        try {
            if (src instanceof JsonTreeNode) {
                JsonNode jsonNode = (JsonNode) JsonTreeNodes.toXxxJSON((JsonTreeNode) src, new JacksonToJsonMapper());
                return getDelegate().writeValueAsString(jsonNode);
            }
            return getDelegate().writeValueAsString(src);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    @Deprecated
    public void setObjectMapper(ObjectMapper objectMapper) {
        setDelegate(objectMapper);
    }

}
