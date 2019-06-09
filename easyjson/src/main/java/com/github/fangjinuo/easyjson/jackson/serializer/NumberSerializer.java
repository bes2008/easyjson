package com.github.fangjinuo.easyjson.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.fangjinuo.easyjson.jackson.Jacksons;
import com.google.gson.JsonPrimitive;

import java.io.IOException;

public class NumberSerializer extends JsonSerializer<Number> {
    public static final String WRITE_LONG_AS_STRING_ATTR_KEY="WRITE_LONG_AS_STRING";
    public static final String WRITE_NUMBER_AS_STRING_ATTR_KEY="WRITE_NUMBER_AS_STRING";

    @Override
    public void serialize(Number value, JsonGenerator gen, SerializerProvider sp) throws IOException {
        boolean longUsingString = Jacksons.getBoolean(sp.getAttribute(WRITE_LONG_AS_STRING_ATTR_KEY));
        boolean usingString = Jacksons.getBoolean(sp.getAttribute(WRITE_NUMBER_AS_STRING_ATTR_KEY));
        if (longUsingString) {
            Class typeOfSrc = value.getClass();
            if (Long.class == typeOfSrc || long.class == typeOfSrc) {
                gen.writeString(value.toString());
                return;
            } else {
                gen.writeNumber(value.toString());
                return;
            }
        }
        if (usingString) {
            gen.writeString(value.toString());
            return ;
        }
        gen.writeNumber(value.toString());
    }
}
