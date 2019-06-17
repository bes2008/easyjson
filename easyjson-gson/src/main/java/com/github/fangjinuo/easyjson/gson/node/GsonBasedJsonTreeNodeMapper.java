/*
 *  Copyright 2019 the original author or authors.
 *
 *  Licensed under the LGPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.github.fangjinuo.easyjson.gson.node;

import com.github.fangjinuo.easyjson.core.JsonTreeNode;
import com.github.fangjinuo.easyjson.core.node.*;
import com.google.gson.*;

import java.util.Map;

public class GsonBasedJsonTreeNodeMapper implements JsonTreeNodeFactory<JsonElement> {
    @Override
    public JsonTreeNode create(JsonElement jsonElement) {
        if (jsonElement.isJsonNull()) {
            return JsonNullNode.INSTANCE;
        }
        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (jsonPrimitive.isNumber()) {
                return new JsonPrimitiveNode(jsonPrimitive.getAsNumber());
            }
            if (jsonPrimitive.isBoolean()) {
                return new JsonPrimitiveNode(jsonPrimitive.getAsBoolean());
            }
            if (jsonPrimitive.isString()) {
                return new JsonPrimitiveNode(jsonPrimitive.getAsString());
            }
            return JsonNullNode.INSTANCE;
        }
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            JsonArrayNode jsonArrayNode = new JsonArrayNode(jsonArray.size());
            for (JsonElement je : jsonArray) {
                jsonArrayNode.add(create(je));
            }
            return jsonArrayNode;
        }
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObjectNode jsonObjectNode = new JsonObjectNode();
            for (Map.Entry<String, JsonElement> property : jsonObject.entrySet()) {
                String propertyName = property.getKey();
                JsonElement propertyValue = property.getValue();
                jsonObjectNode.addProperty(propertyName, create(propertyValue));
            }
            return jsonObjectNode;
        }
        return JsonNullNode.INSTANCE;
    }

    public JsonElement mapping(JsonTreeNode jsonElement) {
        if (jsonElement.isJsonNullNode()) {
            return JsonNull.INSTANCE;
        }
        if (jsonElement.isJsonPrimitiveNode()) {
            JsonPrimitiveNode jsonPrimitive = jsonElement.getAsJsonPrimitiveNode();
            if (jsonPrimitive.isNumber()) {
                return new JsonPrimitive(jsonPrimitive.getAsNumber());
            }
            if (jsonPrimitive.isBoolean()) {
                return new JsonPrimitive(jsonPrimitive.getAsBoolean());
            }
            if (jsonPrimitive.isString()) {
                return new JsonPrimitive(jsonPrimitive.getAsString());
            }
            return JsonNull.INSTANCE;
        }
        if (jsonElement.isJsonArrayNode()) {
            JsonArrayNode jsonArray = jsonElement.getAsJsonArrayNode();
            JsonArray jsonArrayNode = new JsonArray(jsonArray.size());
            for (JsonTreeNode je : jsonArray) {
                jsonArrayNode.add(mapping(je));
            }
            return jsonArrayNode;
        }
        if (jsonElement.isJsonObjectNode()) {
            JsonObjectNode jsonObject = jsonElement.getAsJsonObjectNode();
            JsonObject jsonObjectNode = new JsonObject();
            for (Map.Entry<String, JsonTreeNode> property : jsonObject.propertySet()) {
                String propertyName = property.getKey();
                JsonTreeNode propertyValue = property.getValue();
                jsonObjectNode.add(propertyName, mapping(propertyValue));
            }
            return jsonObjectNode;
        }
        return JsonNull.INSTANCE;
    }
}
