package com.github.fangjinuo.easyjson.gson.node;

import com.github.fangjinuo.easyjson.api.JsonTreeNode;
import com.github.fangjinuo.easyjson.api.node.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Map;

public class GsonBasedJsonTreeNodeFactory implements JsonTreeNodeFactory<JsonElement> {
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
}
