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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.jn.easyjson.core.codec.dialect.PropertyCodecConfiguration;
import com.jn.easyjson.jackson.JacksonConstants;
import com.jn.easyjson.jackson.Jacksons;
import com.jn.langx.util.Dates;
import com.jn.langx.util.Strings;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Modifiers;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.jn.easyjson.jackson.JacksonConstants.ENABLE_CUSTOM_CONFIGURATION;

public class DateDeserializer extends JsonDeserializer implements com.fasterxml.jackson.databind.deser.ContextualDeserializer {
    private static Logger logger = Loggers.getLogger(DateDeserializer.class);
    private Class type;
    private DateFormat customFormat;
    private TimeZone customTz;
    private Locale locale;

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
            Constructor defaultCtor = (Constructor<Calendar>) Reflects.getConstructor(type);
            TimeZone tz = Jacksons.getTimeZone(ctx);
            Calendar c = null;
            if (defaultCtor == null || !Modifiers.isPublic(defaultCtor)) {
                c = Calendar.getInstance(tz);
            } else {
                try {
                    c = (Calendar) defaultCtor.newInstance();
                } catch (Exception e) {
                    throw ctx.instantiationException(type, e);
                }
            }
            if (c != null) {
                c.setTimeZone(tz);
                c.setTimeInMillis(date.getTime());
            }
            return c;
        }
        return null;
    }

    private Date parseDate(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
        JsonToken curr = p.getCurrentToken();
        if (curr == JsonToken.VALUE_NULL) {
            return null;
        }

        if (curr == JsonToken.VALUE_STRING) {

            DateFormat df = this.customFormat;
            String pattern = null;
            if (df == null && Jacksons.getBooleanAttr(ctx, ENABLE_CUSTOM_CONFIGURATION)) {
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


            if (df == null && Strings.isNotBlank(pattern)) {
                df = Dates.getSimpleDateFormat(pattern);
            }
            if (df == null && this.customTz != null && locale != null) {
                df = ctx.getConfig().getDateFormat();
                // one shortcut: with our custom format, can simplify handling a bit
                if (df.getClass() == StdDateFormat.class) {
                    StdDateFormat std = (StdDateFormat) df;
                    std = std.withTimeZone(customTz);
                    std = std.withLocale(locale);
                    df = std;
                } else {
                    df = (DateFormat) df.clone();
                    df.setTimeZone(customTz);
                }
            }

            if (df != null) {
                try {
                    return df.parse(p.getValueAsString());
                } catch (ParseException e) {
                    logger.error(e.getMessage(), e);
                    return null;
                }
            } else {
                if (usingToString) {
                    return new Date(p.getValueAsString());
                } else {
                    return ctx.parseDate(p.getValueAsString());
                }
            }
        }
        if (curr.isNumeric()) {
            long timestamp = p.getLongValue();
            return new Date(timestamp);
        }
        return null;
    }

    @Override
    public Class<?> handledType() {
        return type;
    }

    public DateDeserializer withDateFormat(DateFormat df, String formatStr) {
        this.customFormat = df;

        return this;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        JsonFormat.Value format = ctxt.getAnnotationIntrospector().findFormat((Annotated) property.getMember());
        if (format != null) {
            TimeZone tz = format.getTimeZone();
            // First: fully custom pattern?
            if (format.hasPattern()) {
                String pattern = format.getPattern();
                final Locale loc = format.hasLocale() ? format.getLocale() : ctxt.getLocale();
                if (tz == null) {
                    tz = ctxt.getTimeZone();
                }
                SimpleDateFormat df = Dates.getSimpleDateFormat(pattern, tz, loc);
                return withDateFormat(df, pattern);
            }
            // But if not, can still override timezone
            if (tz != null) {
                this.customTz = tz;
                this.locale = format.hasLocale() ? format.getLocale() : ctxt.getLocale();
            }
        }

        return this;
    }
}
