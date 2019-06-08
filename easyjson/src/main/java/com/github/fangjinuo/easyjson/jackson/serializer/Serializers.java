package com.github.fangjinuo.easyjson.jackson.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;

public class Serializers extends com.fasterxml.jackson.databind.ser.Serializers.Base {
    @Override
    public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
        return super.findSerializer(config, type, beanDesc);
    }
}
