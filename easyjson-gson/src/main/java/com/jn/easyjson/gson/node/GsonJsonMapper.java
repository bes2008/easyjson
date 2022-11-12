package com.jn.easyjson.gson.node;

import com.google.gson.*;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.*;

import java.util.Map;

public class GsonJsonMapper {

    public static JsonTreeNode toJsonTreeNode(Object obj) {
        return JsonTreeNodes.toJsonTreeNode(obj, new GsonToJsonTreeNodeMapper());
    }

    public static JsonElement fromJsonTreeNode(JsonTreeNode jsonTreeNode) {
        return (JsonElement) JsonTreeNodes.toXxxJson(jsonTreeNode, new GsonToJsonMapper());
    }

    static class GsonToJsonMapper implements ToXxxJsonMapper<JsonObject, JsonArray, JsonPrimitive, JsonNull> {

        @Override
        public JsonNull mappingNull(JsonNullNode node) {
            return JsonNull.INSTANCE;
        }

        @Override
        public JsonPrimitive mappingPrimitive(JsonPrimitiveNode node) {
            JsonPrimitiveNode jsonPrimitive = node.getAsJsonPrimitiveNode();
            if (jsonPrimitive.isNumberNode()) {
                return new JsonPrimitive(jsonPrimitive.getAsNumber());
            }
            if (jsonPrimitive.isBooleanNode()) {
                return new JsonPrimitive(jsonPrimitive.getAsBoolean());
            }
            return new JsonPrimitive(jsonPrimitive.getAsString());

        }

        @Override
        public JsonArray mappingArray(JsonArrayNode node) {
            JsonArrayNode jsonArray = node.getAsJsonArrayNode();
            JsonArray jsonArrayNode = new JsonArray(jsonArray.size());
            for (JsonTreeNode je : jsonArray) {
                jsonArrayNode.add((JsonElement) JsonTreeNodes.toXxxJson(je, this));
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
                jsonObjectNode.add(propertyName, (JsonElement) JsonTreeNodes.toXxxJson(propertyValue, this));
            }
            return jsonObjectNode;
        }
    }

    static class GsonToJsonTreeNodeMapper implements ToJsonTreeNodeMapper {

        @Override
        public boolean isAcceptable(Object object) {
            return object instanceof JsonElement;
        }

        @Override
        public JsonTreeNode mapping(Object object) {
            return create((JsonElement) object);
        }

        private JsonTreeNode create(JsonElement jsonElement) {
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

}
