package com.github.fangjinuo.easyjson.jackson.deserializer;

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
