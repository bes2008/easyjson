package com.jn.easyjson.jackson.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.*;

import java.util.Iterator;

class JacksonToJsonMapper implements ToXxxJsonMapper<ObjectNode, ArrayNode, ValueNode, NullNode> {
    JsonNodeFactory jsonNodeFactory = new JsonNodeFactory(true);

    @Override
    public NullNode mappingNull(JsonNullNode node) {
        return NullNode.instance;
    }

    @Override
    public ValueNode mappingPrimitive(JsonPrimitiveNode node) {
        JsonPrimitiveNode jsonPrimitiveNode = node.getAsJsonPrimitiveNode();
        if (jsonPrimitiveNode.isNumber()) {
            if (jsonPrimitiveNode.isDouble()) {
                return jsonNodeFactory.numberNode(jsonPrimitiveNode.getAsDouble());
            }
            if (jsonPrimitiveNode.isLong()) {
                return jsonNodeFactory.numberNode(jsonPrimitiveNode.getAsLong());
            }
            if (jsonPrimitiveNode.isFloat()) {
                return jsonNodeFactory.numberNode(jsonPrimitiveNode.getAsFloat());
            }
            if (jsonPrimitiveNode.isInteger()) {
                return jsonNodeFactory.numberNode(jsonPrimitiveNode.getAsInt());
            }
            if (jsonPrimitiveNode.isShort()) {
                return jsonNodeFactory.numberNode(jsonPrimitiveNode.getAsShort());
            }
            if (jsonPrimitiveNode.isByte()) {
                return jsonNodeFactory.numberNode(jsonPrimitiveNode.getAsByte());
            }
            if (jsonPrimitiveNode.isBigInteger()) {
                return jsonNodeFactory.numberNode(jsonPrimitiveNode.getAsBigInteger());
            }
            if (jsonPrimitiveNode.isBigDecimal()) {
                return jsonNodeFactory.numberNode(jsonPrimitiveNode.getAsBigDecimal());
            }
        }
        if (jsonPrimitiveNode.isBoolean()) {
            return jsonNodeFactory.booleanNode(jsonPrimitiveNode.getAsBoolean());
        }
        if (jsonPrimitiveNode.isChar()) {
            return jsonNodeFactory.textNode(jsonPrimitiveNode.getAsString());
        }
        return jsonNodeFactory.textNode(jsonPrimitiveNode.getAsString());
    }

    @Override
    public ArrayNode mappingArray(JsonArrayNode node) {
        JsonArrayNode jsonArrayNode = node.getAsJsonArrayNode();
        ArrayNode arrayNode = jsonNodeFactory.arrayNode();
        for (JsonTreeNode element : jsonArrayNode) {
            arrayNode.add((JsonNode) JsonTreeNodes.toXxxJson(element, this));
        }
        return arrayNode;
    }

    @Override
    public ObjectNode mappingObject(JsonObjectNode node) {
        JsonObjectNode jsonObjectNode = node.getAsJsonObjectNode();
        ObjectNode objectNode = jsonNodeFactory.objectNode();
        Iterator<String> iter = jsonObjectNode.propertyNames().iterator();
        while (iter.hasNext()) {
            String fieldName = iter.next();
            objectNode.set(fieldName, (JsonNode) JsonTreeNodes.toXxxJson(jsonObjectNode.getProperty(fieldName), this));
        }
        return objectNode;
    }
}
