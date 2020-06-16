/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.easyjson.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jn.easyjson.core.codec.dialect.PropertyCodecConfiguration;
import com.jn.easyjson.jackson.JacksonConstants;
import com.jn.easyjson.jackson.Jacksons;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.jn.easyjson.jackson.JacksonConstants.ENABLE_CUSTOM_CONFIGURATION;

public class DateDeserializer<T extends Date> extends JsonDeserializer<T> implements ContextualDeserializer {

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
        JsonToken curr = p.getCurrentToken();
        if (curr == JsonToken.VALUE_NULL) {
            return null;
        }

        DateFormat df = null;
        String pattern = null;
        if (Jacksons.getBooleanAttr(ctx, ENABLE_CUSTOM_CONFIGURATION)) {
            PropertyCodecConfiguration propertyCodecConfiguration = Jacksons.getPropertyCodecConfiguration(p);
            if (propertyCodecConfiguration != null) {
                df = propertyCodecConfiguration.getDateFormat();
                pattern = propertyCodecConfiguration.getDatePattern();
            }
        }
        if (df == null) {
            df = Jacksons.getDateFormatAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_DATE_FORMAT_ATTR_KEY);
        }
        if (pattern == null) {
            pattern = Jacksons.getStringAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_PATTERN_ATTR_KEY);
        }
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
