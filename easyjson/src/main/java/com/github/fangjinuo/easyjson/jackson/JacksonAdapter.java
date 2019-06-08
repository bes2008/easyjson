package com.github.fangjinuo.easyjson.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fangjinuo.easyjson.core.JsonException;
import com.github.fangjinuo.easyjson.core.JsonHandler;
import com.github.fangjinuo.easyjson.core.type.Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class JacksonAdapter implements JsonHandler {
    private ObjectMapper objectMapper;

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
        }catch (Throwable ex){
            throw new JsonException(ex);
        }
        return null;
    }


    @Override
    public String serialize(Object src, Type typeOfT) throws JsonException {
        try {
            return objectMapper.writeValueAsString(src);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
