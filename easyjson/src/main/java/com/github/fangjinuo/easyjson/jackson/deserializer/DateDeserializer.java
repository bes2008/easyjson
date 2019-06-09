package com.github.fangjinuo.easyjson.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.fangjinuo.easyjson.jackson.JacksonConstants;
import com.github.fangjinuo.easyjson.jackson.Jacksons;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer<T extends Date> extends JsonDeserializer<T> implements ContextualDeserializer {

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
        JsonToken curr = p.getCurrentToken();
        if (curr == JsonToken.VALUE_NULL) {
            return null;
        }

        DateFormat df = Jacksons.getDateFormatAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_DATE_FORMAT_ATTR_KEY);
        String pattern = Jacksons.getStringAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_PATTERN_ATTR_KEY);
        boolean usingToString = Jacksons.getBooleanAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_TO_STRING_ATTR_KEY);


        if (curr == JsonToken.VALUE_STRING) {
            if (df == null && pattern != null && !pattern.trim().isEmpty()) {
                df = new SimpleDateFormat(pattern);
            }
            if (df != null) {
                try {
                    return (T) df.parse(p.getValueAsString());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            } else if (usingToString) {
                return (T) new Date(p.getValueAsString());
            } else {
                return null;
            }
        }
        if (curr.isNumeric()) {
            long timestamp = p.getLongValue();
            return (T) new Date(timestamp);
        }
        return null;
    }

    @Override
    public JsonDeserializer<T> createContextual(DeserializationContext context, BeanProperty beanProperty, Class<?> type) throws JsonMappingException {
        if (type == Date.class) {
            return this;
        }
        return null;
    }
}
