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
import com.jn.easyjson.gson.GsonJSONBuilder;
import com.jn.langx.util.collection.Collects;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class BooleanTypeAdapter extends TypeAdapter<Boolean> implements JSONBuilderAware<GsonJSONBuilder>, FieldAware, JsonSerializer<Boolean>, JsonDeserializer<Boolean> {
    private static final List<String> evalTrues = Arrays.asList(new String[]{"true", "on", "1"});
    private boolean using1_0 = false;
    private boolean usingOnOff = false;
    private Field currentField;
    private GsonJSONBuilder jsonBuilder;

    @Override
    public GsonJSONBuilder getJSONBuilder() {
        return jsonBuilder;
    }

    @Override
    public void setJSONBuilder(GsonJSONBuilder jsonBuilder) {
        this.jsonBuilder = jsonBuilder;
    }

    public void setField(Field currentField) {
        this.currentField = currentField;
    }

    public void setUsing1_0(boolean using1_0) {
        this.using1_0 = using1_0;
    }

    public void setUsingOnOff(boolean usingOnOff) {
        this.usingOnOff = usingOnOff;
    }

    @Override
    public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull() || json.isJsonArray() || json.isJsonObject()) {
            return false;
        }
        JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
        if (jsonPrimitive.isString()) {
            String vstring = jsonPrimitive.getAsString().toLowerCase();
            return evalTrues.contains(vstring);
        }
        if (jsonPrimitive.isNumber()) {
            return jsonPrimitive.getAsInt() == 1;
        }
        if (jsonPrimitive.isBoolean()) {
            return jsonPrimitive.getAsBoolean();
        }
        return false;
    }

    @Override
    public JsonElement serialize(Boolean src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        if (usingOnOff) {
            return new JsonPrimitive(src ? "on" : "off");
        }
        if (using1_0) {
            return new JsonPrimitive(src ? 1 : 0);
        }
        return new JsonPrimitive(src);
    }

    @Override
    public void write(JsonWriter out, Boolean value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        if (jsonBuilder != null && currentField != null) {
            PropertyCodecConfiguration propertyCodecConfiguration = PropertyCodecConfiguration.getPropertyCodecConfiguration(jsonBuilder.proxyDialectIdentify(), currentField.getDeclaringClass(), currentField.getName());
            if (propertyCodecConfiguration != null) {
                if (propertyCodecConfiguration.getBooleanUsingONOFF()) {
                    out.value(value ? "on" : "off");
                    return;
                }
                if (propertyCodecConfiguration.getBooleanUsing01()) {
                    out.value(value ? 1 : 0);
                    return;
                }
            }
        }

        if (usingOnOff) {
            out.value(value ? "on" : "off");
            return;
        }
        if (using1_0) {
            out.value(value ? 1 : 0);
            return;
        }
        out.value(value);
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
    public Boolean read(JsonReader in) throws IOException {
        JsonToken jsonToken = in.peek();
        if (jsonToken == JsonToken.NULL) {
            return null;
        }
        if (invalidValueTokens.contains(jsonToken)) {
            return null;
        }
        if (jsonToken == JsonToken.STRING) {
            String vstring = in.nextString().toLowerCase();
            return evalTrues.contains(vstring);
        }
        if (jsonToken == JsonToken.NUMBER) {
            return in.nextInt() == 1;
        }
        if (jsonToken == JsonToken.BOOLEAN) {
            return in.nextBoolean();
        }
        return false;
    }

    @Override
    public String toString() {
        return "com.jn.easyjson.gson.typeadapter.BooleanTypeAdapter{" +
                "using1_0=" + using1_0 +
                ", usingOnOff=" + usingOnOff +
                '}';
    }
}
