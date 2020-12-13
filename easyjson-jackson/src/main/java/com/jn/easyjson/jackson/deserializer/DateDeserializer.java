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

package com.jn.easyjson.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.jn.easyjson.core.codec.dialect.PropertyCodecConfiguration;
import com.jn.easyjson.jackson.JacksonConstants;
import com.jn.easyjson.jackson.Jacksons;
import com.jn.langx.util.Dates;
import com.jn.langx.util.Strings;
import com.jn.langx.util.reflect.Modifiers;
import com.jn.langx.util.reflect.Reflects;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.jn.easyjson.jackson.JacksonConstants.ENABLE_CUSTOM_CONFIGURATION;

public class DateDeserializer extends JsonDeserializer {
    private Class type;

    public DateDeserializer(Class type) {
        this.type = type;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
        Date date = parseDate(p, ctx);
        if (date == null) {
            return null;
        }
        if (this.type == Date.class) {
            return date;
        }
        if (this.type == java.sql.Date.class) {
            return new java.sql.Date(date.getTime());
        }
        if (this.type == Timestamp.class) {
            return new Timestamp(date.getTime());
        }
        if (Reflects.isSubClassOrEquals(Calendar.class, type)) {
            Constructor _defaultCtor = (Constructor<Calendar>) ClassUtil.findConstructor(type, true);
            TimeZone tz = Jacksons.getTimeZone(ctx);
            Calendar c = null;
            if (_defaultCtor == null || !Modifiers.isPublic(_defaultCtor)) {
                c = Calendar.getInstance(tz);
            }else {
                try {
                    c = (Calendar) _defaultCtor.newInstance();
                } catch (Exception e) {
                    return (Calendar) ctx.handleInstantiationProblem(handledType(), date, e);
                }
            }
            c.setTimeZone(tz);
            c.setTimeInMillis(date.getTime());
            return c;
        }
        return null;
    }

    private Date parseDate(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
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

                if (df == null && Strings.isNotBlank(pattern)) {
                    df = Dates.getSimpleDateFormat(pattern);
                }
            }
        }
        if (df == null) {
            df = Jacksons.getDateFormatAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_DATE_FORMAT_ATTR_KEY);
        }
        boolean usingToString = Jacksons.getBooleanAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_TO_STRING_ATTR_KEY);


        if (curr == JsonToken.VALUE_STRING) {
            if (df == null && Strings.isNotBlank(pattern)) {
                df = Dates.getSimpleDateFormat(pattern);
            }
            if (df != null) {
                try {
                    return df.parse(p.getValueAsString());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            } else if (usingToString) {
                return new Date(p.getValueAsString());
            } else {
                return null;
            }
        }
        if (curr.isNumeric()) {
            long timestamp = p.getLongValue();
            return new Date(timestamp);
        }
        return null;
    }

}
