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
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.enums.Enums;
import com.jn.langx.util.function.Supplier0;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.List;

/**
 * priority : ordinal() > toString() > field > name()
 */
public class EnumTypeAdapter extends EasyjsonAbstractTypeAdapter<Enum> implements JsonSerializer<Enum>, JsonDeserializer<Enum> {
    private static Logger logger = LoggerFactory.getLogger(EnumTypeAdapter.class);
    private static List<JsonToken> invalidValueTokens = Collects.newArrayList(
            JsonToken.BEGIN_ARRAY,
            JsonToken.END_ARRAY,
            JsonToken.BEGIN_OBJECT,
            JsonToken.END_OBJECT,
            JsonToken.END_DOCUMENT,
            JsonToken.NAME
    );
    private final boolean usingName = true;
    private boolean usingValue = false; // using ordinal()
    private boolean usingToString = false;
    private String usingField = null;


    public boolean isUsingValue() {
        return usingValue;
    }

    public void setUsingValue(boolean usingValue) {
        this.usingValue = usingValue;
    }

    public boolean isUsingToString() {
        return usingToString;
    }

    public void setUsingToString(boolean usingToString) {
        this.usingToString = usingToString;
    }

    public String getUsingField() {
        return usingField;
    }

    public void setUsingField(String usingField) {
        this.usingField = usingField;
    }

    @Override
    public Enum deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull() || json.isJsonArray() || json.isJsonObject()) {
            return null;
        }
        final JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
        if (usingValue && jsonPrimitive.isNumber()) {
            return Enums.ofCode((Class<? extends Enum>) typeOfT, jsonPrimitive.getAsInt());
        }
        if (usingToString && jsonPrimitive.isString()) {
            return Enums.ofToString((Class<? extends Enum>) typeOfT, jsonPrimitive.getAsString());
        }
        if (usingField != null) {
            try {
                Field field = ((Class<Enum>) typeOfT).getDeclaredField(usingField);
                final Class fieldType = field.getType();

                return Enums.ofField((Class<? extends Enum>) typeOfT, usingField, new Supplier0<Object>() {
                    @Override
                    public Object get() {
                        try {
                            if (String.class == fieldType) {
                                return jsonPrimitive.getAsString();
                            }
                            if (Character.class == fieldType) {
                                return jsonPrimitive.getAsCharacter();
                            }
                            if (Boolean.class == fieldType) {
                                return jsonPrimitive.getAsBoolean();
                            }
                            if (Number.class == fieldType) {
                                return jsonPrimitive.getAsNumber();
                            }
                        } catch (Throwable ex) {
                            logger.error(ex.getMessage(), ex);
                        }
                        return null;
                    }
                });
            } catch (Throwable ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        if (usingName) {
            return Enums.ofName((Class<? extends Enum>) typeOfT, jsonPrimitive.getAsString());
        }
        return null;
    }

    @Override
    public JsonElement serialize(Enum e, Type typeOfSrc, JsonSerializationContext context) {
        if (e == null) {
            return JsonNull.INSTANCE;
        }
        if (usingValue) {
            return new JsonPrimitive(e.ordinal());
        }
        if (usingToString) {
            return new JsonPrimitive(e.toString());
        }
        if (usingField != null) {
            try {
                Field field = e.getClass().getDeclaredField(usingField);
                field.setAccessible(true);
                Class fieldClazz = field.getType();
                if (fieldClazz.isArray() || fieldClazz.isAnonymousClass() || fieldClazz.isAnnotation() || fieldClazz.isSynthetic()) {
                    throw new UnsupportedOperationException();
                }
                if (fieldClazz == String.class) {
                    return new JsonPrimitive(field.get(e).toString());
                }
                if (fieldClazz == Character.class) {
                    return new JsonPrimitive((Character) field.get(e));
                }
                if (fieldClazz == Boolean.class) {
                    return new JsonPrimitive((Boolean) field.get(e));
                }
                if (Number.class.isAssignableFrom(fieldClazz)) {
                    return new JsonPrimitive((Number) field.get(e));
                }
            } catch (NoSuchFieldException e1) {
                // ignore it
            } catch (IllegalAccessException e1) {
                // ignore it
            }
        }
        return new JsonPrimitive(Enums.getName(e));
    }

    @Override
    public void write(JsonWriter out, Enum value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        if (jsonBuilder != null && isField()) {
            PropertyCodecConfiguration propertyCodecConfiguration = PropertyCodecConfiguration.getPropertyCodecConfiguration(jsonBuilder.proxyDialectIdentify(), getDeclaringClass(), this.currentFieldOrClass.getFieldName());
            if (propertyCodecConfiguration != null) {
                if (propertyCodecConfiguration.getEnumUsingIndex()) {
                    out.value(value.ordinal());
                    return;
                }
                if (propertyCodecConfiguration.getEnumUsingToString()) {
                    out.value(value.toString());
                    return;
                }
                if (propertyCodecConfiguration.getEnumUsingName()) {
                    out.value(value.name());
                    return;
                }
            }
        }


        if (usingValue) {
            out.value(value.ordinal());
            return;
        }
        if (usingToString) {
            out.value(value.toString());
            return;
        }
        if (usingField != null) {
            try {
                Field field = value.getClass().getDeclaredField(usingField);
                field.setAccessible(true);
                Class fieldClazz = field.getType();
                if (fieldClazz.isArray() || fieldClazz.isAnonymousClass() || fieldClazz.isAnnotation() || fieldClazz.isSynthetic()) {
                    throw new UnsupportedOperationException();
                }
                if (fieldClazz == String.class) {
                    out.value(field.get(value).toString());
                    return;
                }
                if (fieldClazz == Character.class) {
                    out.value((Character) field.get(value));
                    return;
                }
                if (fieldClazz == Boolean.class) {
                    out.value((Boolean) field.get(value));
                    return;
                }
                if (Number.class.isAssignableFrom(fieldClazz)) {
                    out.value((Number) field.get(value));
                    return;
                }
            } catch (NoSuchFieldException e1) {
                // ignore it
            } catch (IllegalAccessException e1) {
                // ignore it
            }
        }
        out.value(Enums.getName(value));
    }

    @Override
    public Enum read(JsonReader in) throws IOException {
        JsonToken jsonToken = in.peek();
        if (jsonToken == JsonToken.NULL) {
            return null;
        }

        if (invalidValueTokens.contains(jsonToken)) {
            return null;
        }

        String stringValue = jsonToken == JsonToken.STRING ? in.nextString() : null;
        Double doubleValue = jsonToken == JsonToken.NUMBER ? in.nextDouble() : null;
        Boolean booleanValue = jsonToken == JsonToken.BOOLEAN ? in.nextBoolean() : null;

        EnumSet es = EnumSet.allOf(getDataClass());

        if (jsonBuilder != null && isField()) {
            PropertyCodecConfiguration propertyCodecConfiguration = PropertyCodecConfiguration.getPropertyCodecConfiguration(jsonBuilder.proxyDialectIdentify(), getDeclaringClass(), this.currentFieldOrClass.getFieldName());
            if (propertyCodecConfiguration != null) {
                if (propertyCodecConfiguration.getEnumUsingIndex()) {
                    return Enums.ofValue(Numbers.toInt(doubleValue), getDataClass());
                }
                if (propertyCodecConfiguration.getEnumUsingToString()) {
                    if (Emptys.isNotEmpty(stringValue)) {
                        for (Object obj : es) {
                            Enum e = (Enum) obj;
                            if (e.toString().equals(stringValue)) {
                                return e;
                            }
                        }
                    }
                }

                if (propertyCodecConfiguration.getEnumUsingName()) {
                    return Enums.ofName(getDataClass(), stringValue);
                }
            }
        }

        if (usingField != null) {
            try {
                Field field = Reflects.getAnyField(getDataClass(), usingField);
                if (field == null) {
                    return null;
                }
                if (stringValue != null) {
                    if (Reflects.isSubClass(CharSequence.class, field.getType())) {
                        return Enums.ofField(getDataClass(), usingField, stringValue);
                    }
                }
                if (doubleValue != null) {
                    if (Reflects.isSubClass(Number.class, field.getType())) {
                        return Enums.ofField(getDataClass(), usingField, doubleValue);
                    }
                }
                if (booleanValue != null) {
                    if (Boolean.class == field.getType()) {
                        return Enums.ofField(getDataClass(), usingField, booleanValue);
                    }
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        if (usingValue && jsonToken == JsonToken.NUMBER) {
            return Enums.ofValue(doubleValue.intValue(), getDataClass());
        }
        if (usingToString && jsonToken == JsonToken.STRING) {
            String value = in.nextString();
            for (Object obj : es) {
                Enum e = (Enum) obj;
                if (e.toString().equals(value)) {
                    return e;
                }
            }
        }

        if (usingName) {
            return Enums.ofName(getDataClass(), stringValue);
        }
        return null;
    }

    @Override
    public String toString() {
        return "com.jn.easyjson.gson.typeadapter.EnumTypeAdapter{" +
                "usingValue=" + usingValue +
                ", usingToString=" + usingToString +
                ", usingField='" + usingField + '\'' +
                ", usingName=" + usingName +
                '}';
    }

}
