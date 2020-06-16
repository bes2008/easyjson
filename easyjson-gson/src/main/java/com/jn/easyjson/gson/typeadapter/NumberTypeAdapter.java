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
import com.jn.easyjson.core.util.LazilyParsedNumber;
import com.jn.langx.util.reflect.type.Primitives;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * priority: longUsingString > usingString > number
 */
public class NumberTypeAdapter implements JsonSerializer<Number>, JsonDeserializer<Number> {
    private boolean longUsingString;
    private boolean usingString;

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
    public String toString() {
        return "com.jn.easyjson.gson.typeadapter.NumberTypeAdapter{" +
                "longUsingString=" + longUsingString +
                ", usingString=" + usingString +
                '}';
    }
}
