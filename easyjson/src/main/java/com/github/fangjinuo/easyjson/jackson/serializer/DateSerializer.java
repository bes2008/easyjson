package com.github.fangjinuo.easyjson.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.github.fangjinuo.easyjson.jackson.JacksonConstants;
import com.github.fangjinuo.easyjson.jackson.Jacksons;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer<T extends Date> extends JsonSerializer<T> implements ContextualSerializer {
    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider ctx) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        DateFormat df = Jacksons.getDateFormatAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_DATE_FORMAT_ATTR_KEY);
        String pattern = Jacksons.getStringAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_PATTERN_ATTR_KEY);
        boolean usingToString = Jacksons.getBooleanAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_TO_STRING_ATTR_KEY);
        if (df == null && pattern != null && !pattern.isEmpty()) {
            df = new SimpleDateFormat(pattern);
        }

        if (df != null) {
            gen.writeString(df.format(value));
            return;
        }
        if (usingToString) {
            gen.writeString(value.toString());
        }
        gen.writeNumber(value.getTime());
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property, JavaType type) throws JsonMappingException {
        if (type.getRawClass() == Date.class) {
            return this;
        }
        return null;
    }
}
