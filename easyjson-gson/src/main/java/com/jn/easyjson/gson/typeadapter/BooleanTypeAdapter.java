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

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class BooleanTypeAdapter implements JsonSerializer<Boolean>, JsonDeserializer<Boolean> {
    private static final List<String> evalTrues = Arrays.asList(new String[]{"true", "on", "1"});
    private boolean using1_0 = false;
    private boolean usingOnOff = false;

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
}
