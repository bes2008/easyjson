package com.github.fangjinuo.easyjson.jackson.serializer;

import com.fasterxml.jackson.databind.*;

public interface ContextualSerializer {
    JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property, JavaType type)
            throws JsonMappingException;
}
