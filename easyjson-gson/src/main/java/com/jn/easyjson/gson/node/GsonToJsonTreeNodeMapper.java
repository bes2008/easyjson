package com.jn.easyjson.gson.node;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.*;

import java.util.Map;

public class GsonToJsonTreeNodeMapper implements ToJsonTreeNodeMapper {

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
