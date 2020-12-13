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

package com.jn.easyjson.gson.typeadapter;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jn.easyjson.core.codec.dialect.PropertyCodecConfiguration;
import com.jn.langx.util.Dates;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class DateTypeAdapter extends EasyjsonAbstractTypeAdapter<Date> implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private static final Logger logger = LoggerFactory.getLogger(DateTypeAdapter.class);
    private DateFormat df = null;
    private boolean usingToString = false;

    public void setDateFormat(DateFormat df) {
        this.df = df;
    }

    public void setUsingToString(boolean using) {
        usingToString = using;
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject() || json.isJsonArray() || json.isJsonNull()) {
            return null;
        }
        JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
        if (df != null) {
            try {
                return df.parse(jsonPrimitive.getAsString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (usingToString) {
            return new Date(json.getAsString());
        }
        return new Date(json.getAsLong());
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        if (df != null) {
            return new JsonPrimitive(df.format(src));
        }
        if (usingToString) {
            return new JsonPrimitive(src.toString());
        }
        return new JsonPrimitive(src.getTime());
    }

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        if (jsonBuilder != null && isField()) {
            PropertyCodecConfiguration propertyCodecConfiguration = PropertyCodecConfiguration.getPropertyCodecConfiguration(jsonBuilder.proxyDialectIdentify(), getDeclaringClass(), currentFieldOrClass.getFieldName());
            if (propertyCodecConfiguration != null) {
                if (propertyCodecConfiguration.getDateFormat() != null) {
                    out.value(propertyCodecConfiguration.getDateFormat().format(value));
                    return;
                }
                if (Strings.isNotBlank(propertyCodecConfiguration.getDatePattern())) {
                    out.value(Dates.getSimpleDateFormat(propertyCodecConfiguration.getDatePattern(), jsonBuilder.serializeUsingTimeZone()).format(value));
                    return;
                }
            }
        }

        if (df != null) {
            out.value(df.format(value));
            return;
        }
        if (usingToString) {
            out.value(value.toString());
            return;
        }
        out.value(value.getTime());
    }

    private static List<JsonToken> invalidValueTokens = Collects.newArrayList(
            JsonToken.BEGIN_ARRAY,
            JsonToken.END_ARRAY,
            JsonToken.BEGIN_OBJECT,
            JsonToken.END_OBJECT,
            JsonToken.END_DOCUMENT,
            JsonToken.NAME
    );

    @Override
    public Date read(JsonReader in) throws IOException {
       Date date = readDate(in);
       if(date==null){
           return null;
       }
       Class type = getDataClass();
       if(type == java.sql.Date.class){
           return new java.sql.Date(date.getTime());
       }
       if(type == Timestamp.class){
           return new Timestamp(date.getTime());
       }
       return date;
    }

    private Date readDate(JsonReader in) throws IOException {
        JsonToken jsonToken = in.peek();
        if (jsonToken == JsonToken.NULL) {
            return null;
        }
        if (invalidValueTokens.contains(jsonToken)) {
            return null;
        }
        PropertyCodecConfiguration propertyCodecConfiguration = null;
        if (jsonBuilder != null && isField()) {
            propertyCodecConfiguration = PropertyCodecConfiguration.getPropertyCodecConfiguration(jsonBuilder.proxyDialectIdentify(), getDeclaringClass(), currentFieldOrClass.getFieldName());
        }

        if (jsonToken == JsonToken.STRING) {
            String value = in.nextString();
            try {
                if (propertyCodecConfiguration != null) {
                    if (propertyCodecConfiguration.getDateFormat() != null) {
                        return propertyCodecConfiguration.getDateFormat().parse(value);
                    }
                    if (Strings.isNotBlank(propertyCodecConfiguration.getDatePattern())) {
                        return Dates.parse(value, propertyCodecConfiguration.getDatePattern());
                    }
                }

                if (df != null) {
                    return df.parse(value);
                }
            } catch (ParseException e) {
                logger.error("Can't parse {} to a Date", value);
                return null;
            } catch (RuntimeException ex) {
                logger.error("Can't parse {} to a Date", value);
                return null;
            }
            if (usingToString) {
                return new Date(value);
            }
        }
        return new Date(in.nextLong());
    }

    @Override
    public String toString() {
        return "com.jn.easyjson.gson.typeadapter.DateTypeAdapter{" +
                "df=" + df +
                ", usingToString=" + usingToString +
                '}';
    }
}

