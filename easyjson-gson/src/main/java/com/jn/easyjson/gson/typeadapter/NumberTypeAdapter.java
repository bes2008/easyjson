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
import com.jn.easyjson.core.JSONBuilderAware;
import com.jn.easyjson.core.codec.dialect.PropertyCodecConfiguration;
import com.jn.easyjson.core.util.LazilyParsedNumber;
import com.jn.easyjson.gson.GsonJSONBuilder;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.type.Primitives;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * priority: longUsingString > usingString > number
 */
public class NumberTypeAdapter extends TypeAdapter<Number> implements JSONBuilderAware<GsonJSONBuilder>, FieldAware, JsonSerializer<Number>, JsonDeserializer<Number> {
    private boolean longUsingString;
    private boolean usingString;
    private Field currentField;
    private GsonJSONBuilder jsonBuilder;
    private Class targetClass;

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public void setJSONBuilder(GsonJSONBuilder jsonBuilder) {
        this.jsonBuilder = jsonBuilder;
    }

    @Override
    public GsonJSONBuilder getJSONBuilder() {
        return this.jsonBuilder;
    }

    public void setField(Field currentField) {
        this.currentField = currentField;
    }

    public void setLongUsingString(boolean longUsingString) {
        this.longUsingString = longUsingString;
    }

    public void setUsingString(boolean usingString) {
        this.usingString = usingString;
    }

    @Override
    public Number deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject() || json.isJsonArray() || json.isJsonNull()) {
            return null;
        }
        JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
        if (jsonPrimitive.isString()) {
            if (typeOfT == long.class || typeOfT == Long.class) {
                return jsonPrimitive.getAsNumber().longValue();
            }
            if (typeOfT == double.class || typeOfT == Double.class) {
                return jsonPrimitive.getAsNumber().doubleValue();
            }
            if (typeOfT == int.class || typeOfT == Integer.class) {
                return jsonPrimitive.getAsNumber().intValue();
            }
            if (typeOfT == float.class || typeOfT == Float.class) {
                return jsonPrimitive.getAsNumber().floatValue();
            }
            if (typeOfT == short.class || typeOfT == Short.class) {
                return jsonPrimitive.getAsNumber().shortValue();
            }
            if (typeOfT == byte.class || typeOfT == Byte.class) {
                return jsonPrimitive.getAsNumber().byteValue();
            }
            if (typeOfT == BigDecimal.class) {
                return new BigDecimal(jsonPrimitive.getAsString());
            }
            if (typeOfT == BigInteger.class) {
                return new BigInteger(jsonPrimitive.getAsString());
            }
        }
        if (jsonPrimitive.isNumber()) {
            Number number = jsonPrimitive.getAsNumber();
            typeOfT = Primitives.wrap(typeOfT);
            if (typeOfT == Byte.class) {
                return number.byteValue();
            }
            if (typeOfT == Short.class) {
                return number.shortValue();
            }
            if (typeOfT == Integer.class) {
                return number.intValue();
            }
            if (typeOfT == Float.class) {
                return number.floatValue();
            }
            if (typeOfT == Double.class) {
                return number.doubleValue();
            }
            if (typeOfT == Long.class) {
                return number.longValue();
            }
            if (typeOfT == BigDecimal.class) {
                return new BigDecimal(number.toString());
            }
            if (typeOfT == BigInteger.class) {
                return new BigInteger(number.toString());
            }
            if (typeOfT == LazilyParsedNumber.class) {
                return number;
            }
            if (typeOfT == Number.class) {
                return number;
            }
            return 0;
        }
        return 0;
    }

    @Override
    public JsonElement serialize(Number src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        if (longUsingString) {
            if (Long.class == typeOfSrc || long.class == typeOfSrc) {
                return new JsonPrimitive(src.toString());
            } else {
                return new JsonPrimitive(src);
            }
        }
        if (usingString) {
            return new JsonPrimitive(src.toString());
        }
        return new JsonPrimitive(src);
    }


    @Override
    public void write(JsonWriter out, Number value) throws IOException {
        if(value==null){
            out.nullValue();
            return;
        }

        if (longUsingString && ( value.getClass() == Long.class || value.getClass() == Long.TYPE)) {
            out.value(value.toString());
            return;
        }
        if (usingString) {
            out.value(value.toString());
            return;
        }
        out.value(value);
        return;
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
    public Number read(JsonReader in) throws IOException {
        JsonToken jsonToken = in.peek();
        if (jsonToken == JsonToken.NULL) {
            return null;
        }

        if(invalidValueTokens.contains(jsonToken)){
            return null;
        }

        if (jsonToken==JsonToken.STRING) {
            String stringValue = jsonToken==JsonToken.STRING ? in.nextString():null;
            if (targetClass == Long.TYPE || targetClass == Long.class) {
                return Numbers.createLong(stringValue);
            }
            if (targetClass == double.class || targetClass == Double.class) {
                return Numbers.createDouble(stringValue);
            }
            if (targetClass == int.class || targetClass == Integer.class) {
                return  Numbers.createInteger(stringValue);
            }
            if (targetClass == float.class || targetClass == Float.class) {
                return  Numbers.createFloat(stringValue);
            }
            if (targetClass == short.class || targetClass == Short.class) {
                return Numbers.createInteger(stringValue);
            }
            if (targetClass == byte.class || targetClass == Byte.class) {
                return Numbers.createInteger(stringValue);
            }
            if (targetClass == BigDecimal.class) {
                return Numbers.createBigDecimal(stringValue);
            }
            if (targetClass == BigInteger.class) {
                return Numbers.createBigInteger(stringValue);
            }
        }
        if (jsonToken==JsonToken.NUMBER) {
            Class typeOfT = Primitives.wrap(targetClass);
            if (typeOfT == Byte.class) {
                return Numbers.toByte(in.nextInt());
            }
            if (typeOfT == Short.class) {
                return Numbers.toShort(in.nextInt());
            }
            if (typeOfT == Integer.class) {
                return in.nextInt();
            }
            if (typeOfT == Float.class) {
                return Numbers.toFloat(in.nextDouble());
            }
            if (typeOfT == Double.class) {
                return in.nextDouble();
            }
            if (typeOfT == Long.class) {
                return in.nextLong();
            }
            if (typeOfT == BigDecimal.class) {
                return Numbers.createBigDecimal(""+in.nextDouble());
            }
            if (typeOfT == BigInteger.class) {
                return Numbers.createBigInteger(""+in.nextDouble());
            }
            return in.nextDouble();
        }
        return 0;
    }

    @Override
    public String toString() {
        return "com.jn.easyjson.gson.typeadapter.NumberTypeAdapter{" +
                "longUsingString=" + longUsingString +
                ", usingString=" + usingString +
                '}';
    }
}
