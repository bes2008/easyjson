package com.jn.easyjson.gson.node;

import com.google.gson.*;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.*;

import java.util.Map;

public class GsonToJsonMapper implements ToJSONMapper<JsonObject, JsonArray, JsonPrimitive, JsonNull> {

    @Override
    public JsonNull mappingNull(JsonNullNode node) {
        return JsonNull.INSTANCE;
    }

    @Override
    public JsonPrimitive mappingPrimitive(JsonPrimitiveNode node) {
        JsonPrimitiveNode jsonPrimitive = node.getAsJsonPrimitiveNode();
        if (jsonPrimitive.isNumber()) {
            return new JsonPrimitive(jsonPrimitive.getAsNumber());
        }
        if (jsonPrimitive.isBoolean()) {
            return new JsonPrimitive(jsonPrimitive.getAsBoolean());
        }
        return new JsonPrimitive(jsonPrimitive.getAsString());

    }

    @Override
    public JsonArray mappingArray(JsonArrayNode node) {
        JsonArrayNode jsonArray = node.getAsJsonArrayNode();
        JsonArray jsonArrayNode = new JsonArray(jsonArray.size());
        for (JsonTreeNode je : jsonArray) {
            jsonArrayNode.add((JsonElement) JsonTreeNodes.toXxxJSON(je, this));
        }
        return jsonArrayNode;
    }

    @Override
    public JsonObject mappingObject(JsonObjectNode node) {
        JsonObjectNode jsonObject = node.getAsJsonObjectNode();
        JsonObject jsonObjectNode = new JsonObject();
        for (Map.Entry<String, JsonTreeNode> property : jsonObject.propertySet()) {
            String propertyName = property.getKey();
            JsonTreeNode propertyValue = property.getValue();
            jsonObjectNode.add(propertyName, (JsonElement) JsonTreeNodes.toXxxJSON(propertyValue, this));
        }
        return jsonObjectNode;
    }
}
