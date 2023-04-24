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

package com.jn.easyjson.jackson.serializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.jn.easyjson.core.codec.dialect.PropertyCodecConfiguration;
import com.jn.easyjson.jackson.JacksonConstants;
import com.jn.easyjson.jackson.Jacksons;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Dates;
import com.jn.langx.util.Strings;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateSerializer extends JsonSerializer implements com.fasterxml.jackson.databind.ser.ContextualSerializer {
    private DateFormat customFormat;
    private boolean toTimestamp;
    private TimeZone customTz;
    private Locale locale;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider ctx) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        Date date = null;
        if (value instanceof Date) {
            date = (Date) value;
        } else if (value instanceof Calendar) {
            date = ((Calendar) value).getTime();
        }
        serializeDate(date, gen, ctx);
    }

    private void serializeDate(@NonNull Date value, JsonGenerator gen, SerializerProvider ctx) throws IOException {
        if (toTimestamp) {
            gen.writeNumber(value.getTime());
            return;
        }

        DateFormat df = customFormat;
        String pattern = null;

        if (df == null && Jacksons.getBooleanAttr(ctx, JacksonConstants.ENABLE_CUSTOM_CONFIGURATION)) {
            PropertyCodecConfiguration propertyCodecConfiguration = Jacksons.getPropertyCodecConfiguration(gen);
            if (propertyCodecConfiguration != null) {
                df = propertyCodecConfiguration.getDateFormat();
                pattern = propertyCodecConfiguration.getDatePattern();
                if (df == null && Strings.isNotBlank(pattern)) {
                    df = Dates.getSimpleDateFormat(pattern, this.customTz, this.locale);
                }
            }
        }
        if (df == null) {
            df = Jacksons.getDateFormatAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_DATE_FORMAT_ATTR_KEY);
        }
        boolean usingToString = Jacksons.getBooleanAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_TO_STRING_ATTR_KEY);
        if (df == null && Strings.isNotBlank(pattern)) {
            df = Dates.getSimpleDateFormat(pattern, this.customTz, this.locale);
        }
        if (df == null && this.customTz != null && this.locale != null) {
            df = ctx.getConfig().getDateFormat();
            if (df.getClass() == StdDateFormat.class) {
                df = StdDateFormat.getISO8601Format(this.customTz, this.locale);
            } else {
                // otherwise need to clone, re-set timezone:
                df = (DateFormat) df.clone();
                df.setTimeZone(this.customTz);
            }
        }

        if (df != null) {
            if(df instanceof SimpleDateFormat){
                gen.writeString(((SimpleDateFormat)df.clone()).format(value));
            }else{
                gen.writeString(df.format(value));
            }
            return;
        }
        if (usingToString) {
            gen.writeString(value.toString());
        }
        gen.writeNumber(value.getTime());
    }

    public DateSerializer withFormat(boolean timestamp, DateFormat customFormat) {
        this.customFormat = customFormat;
        this.toTimestamp = timestamp;
        return this;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            JsonFormat.Value format = prov.getAnnotationIntrospector().findFormat((Annotated) property.getMember());
            if (format != null) {

                // Simple case first: serialize as numeric timestamp?
                if (format.getShape().isNumeric()) {
                    return withFormat(Boolean.TRUE, null);
                }

                // If not, do we have a pattern?
                TimeZone tz = format.getTimeZone();
                if (format.hasPattern()) {
                    String pattern = format.getPattern();
                    final Locale loc = format.hasLocale() ? format.getLocale() : prov.getLocale();
                    if (tz == null) {
                        tz = TimeZone.getDefault();
                    }
                    SimpleDateFormat df = Dates.getSimpleDateFormat(pattern, tz, loc);
                    return withFormat(false, df);
                }
                // If not, do we at least have a custom timezone?
                if (tz != null) {
                    this.customTz = tz;
                    this.locale = format.hasLocale() ? format.getLocale() : prov.getLocale();
                }
            }
        }
        return this;
    }
}
