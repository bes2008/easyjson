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

package com.jn.easyjson.gson.typeadapter;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jn.easyjson.core.JSONBuilderAware;
import com.jn.easyjson.core.codec.dialect.PropertyCodecConfiguration;
import com.jn.easyjson.gson.GsonJSONBuilder;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.enums.Enums;
import com.jn.langx.util.reflect.Reflects;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.List;

/**
 * priority : ordinal() > toString() > field > name()
 */
public class EnumTypeAdapter extends TypeAdapter<Enum> implements JSONBuilderAware<GsonJSONBuilder>, FieldAware, JsonSerializer<Enum>, JsonDeserializer<Enum> {
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
    /**
     * 当使用 TypeAdapter API时，不能为 null
     */
    private Class<Enum> enumClass;
    private Field currentField;
    private GsonJSONBuilder jsonBuilder;

    public void setEnumClass(Class enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public GsonJSONBuilder getJSONBuilder() {
        return this.jsonBuilder;
    }

    @Override
    public void setJSONBuilder(GsonJSONBuilder jsonBuilder) {
        this.jsonBuilder = jsonBuilder;
    }

    public void setField(Field currentField) {
        this.currentField = currentField;
    }

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
        JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
        EnumSet es = EnumSet.allOf((Class<Enum>) typeOfT);
        if (usingValue && jsonPrimitive.isNumber()) {
            for (Object obj : es) {
                Enum e = (Enum) obj;
                if (e.ordinal() == jsonPrimitive.getAsInt()) {
                    return e;
                }
            }
        }
        if (usingToString && jsonPrimitive.isString()) {
            for (Object obj : es) {
                Enum e = (Enum) obj;
                if (e.toString().equals(jsonPrimitive.getAsString())) {
                    return e;
                }
            }
        }
        if (usingField != null) {
            try {
                Field field = ((Class<Enum>) typeOfT).getDeclaredField(usingField);
                Class fieldType = field.getType();
                if (String.class == fieldType) {
                    for (Object obj : es) {
                        Enum e = (Enum) obj;
                        String v = (String) field.get(e);
                        if (v.equals(jsonPrimitive.getAsString())) {
                            return e;
                        }
                    }
                }
                if (Character.class == fieldType) {
                    for (Object obj : es) {
                        Enum e = (Enum) obj;
                        Character v = (Character) field.get(e);
                        if (v.equals(jsonPrimitive.getAsCharacter())) {
                            return e;
                        }
                    }
                }
                if (Boolean.class == fieldType) {
                    for (Object obj : es) {
                        Enum e = (Enum) obj;
                        Boolean v = (Boolean) field.get(e);
                        if (v.equals(jsonPrimitive.getAsBoolean())) {
                            return e;
                        }
                    }
                }
                if (Number.class == fieldType) {
                    for (Object obj : es) {
                        Enum e = (Enum) obj;
                        Number v = (Number) field.get(e);
                        if (v.equals(jsonPrimitive.getAsNumber())) {
                            return e;
                        }
                    }
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        if (usingName) {
            for (Object obj : es) {
                Enum e = (Enum) obj;
                if (e.name().equals(jsonPrimitive.getAsString())) {
                    return e;
                }
            }
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
        return new JsonPrimitive(e.name());
    }

    @Override
    public void write(JsonWriter out, Enum value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        if (jsonBuilder != null && currentField != null) {
            PropertyCodecConfiguration propertyCodecConfiguration = PropertyCodecConfiguration.getPropertyCodecConfiguration(jsonBuilder.proxyDialectIdentify(), currentField.getDeclaringClass(), currentField.getName());
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
        out.value(value.name());
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

        EnumSet es = EnumSet.allOf(enumClass);

        if (jsonBuilder != null && currentField != null) {
            PropertyCodecConfiguration propertyCodecConfiguration = PropertyCodecConfiguration.getPropertyCodecConfiguration(jsonBuilder.proxyDialectIdentify(), currentField.getDeclaringClass(), currentField.getName());
            if (propertyCodecConfiguration != null) {
                if (propertyCodecConfiguration.getEnumUsingIndex()) {
                    return Enums.ofValue(Numbers.toInt(doubleValue), enumClass);
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
                    return Enums.ofName(enumClass, stringValue);
                }
            }
        }

        if (usingField != null) {
            try {
                Field field = Reflects.getAnyField(enumClass, usingField);
                if (field == null) {
                    return null;
                }
                if (stringValue != null) {
                    if (Reflects.isSubClass(CharSequence.class, field.getType())) {
                        return Enums.ofField(enumClass, usingField, stringValue);
                    }
                }
                if (doubleValue != null) {
                    if (Reflects.isSubClass(Number.class, field.getType())) {
                        return Enums.ofField(enumClass, usingField, doubleValue);
                    }
                }
                if (booleanValue != null) {
                    if (Boolean.class == field.getType()) {
                        return Enums.ofField(enumClass, usingField, booleanValue);
                    }
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        if (usingValue && jsonToken == JsonToken.NUMBER) {
            return Enums.ofValue(doubleValue.intValue(), enumClass);
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
            return Enums.ofName(enumClass, stringValue);
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
