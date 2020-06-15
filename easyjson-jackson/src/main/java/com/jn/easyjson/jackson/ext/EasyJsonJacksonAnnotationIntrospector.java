package com.jn.easyjson.jackson.ext;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.jn.easyjson.jackson.serializer.NullSerializer;

public class EasyJsonJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {
    @Override
    public Object findNullSerializer(Annotated a) {
        JsonSerialize ann = _findAnnotation(a, JsonSerialize.class);
        if (ann != null) {
            @SuppressWarnings("rawtypes")
            Class<? extends JsonSerializer> serClass = ann.nullsUsing();
            if (serClass != JsonSerializer.None.class) {
                return serClass;
            }
        }else{
            return new NullSerializer();
        }
        return null;
    }
}
