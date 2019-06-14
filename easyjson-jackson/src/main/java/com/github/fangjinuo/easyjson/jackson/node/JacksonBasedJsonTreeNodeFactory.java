package com.github.fangjinuo.easyjson.jackson.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.github.fangjinuo.easyjson.api.JsonTreeNode;
import com.github.fangjinuo.easyjson.api.node.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JacksonBasedJsonTreeNodeFactory implements JsonTreeNodeFactory<JsonNode> {

    private JsonTreeNode createFromPojo(Object object) {
        if (object == null) {
            return JsonNullNode.INSTANCE;
        }
        if (object instanceof JsonNode) {
            return create((JsonNode) object);
        }
        if (object instanceof Number) {
            return new JsonPrimitiveNode((Number) object);
        }
        if (object instanceof String) {
            return new JsonPrimitiveNode(object.toString());
        }
        if (object instanceof Boolean) {
            return new JsonPrimitiveNode((Boolean) object);
        }
        if (object instanceof Collection) {
            Collection c = (Collection) object;
            JsonArrayNode arrayNode = new JsonArrayNode(c.size());
            for (Object e : c) {
                arrayNode.add(createFromPojo(e));
            }
            return arrayNode;
        }
        if (object instanceof Map) {
            Map map = (Map) object;
            JsonObjectNode jsonObjectNode = new JsonObjectNode();
            Set<Object> keys = map.keySet();
            for (Object key : keys) {
                jsonObjectNode.addProperty(key.toString(), createFromPojo(map.get(key)));
            }
            return jsonObjectNode;
        }
        return new JsonPrimitiveNode(object.toString());
    }

    @Override
    public JsonTreeNode create(JsonNode jsonNode) {
        if (jsonNode.isNull()) {
            return JsonNullNode.INSTANCE;
        }
        // Primitive
        if (jsonNode.isNumber()) {
            if (jsonNode.isShort() || jsonNode.isInt()) {
                return new JsonPrimitiveNode(jsonNode.intValue());
            }
            if (jsonNode.isFloat() || jsonNode.isFloatingPointNumber()) {
                return new JsonPrimitiveNode(jsonNode.floatValue());
            }
            if (jsonNode.isDouble()) {
                return new JsonPrimitiveNode(jsonNode.doubleValue());
            }
            if (jsonNode.isLong()) {
                return new JsonPrimitiveNode(jsonNode.longValue());
            }
            if (jsonNode.isBigDecimal()) {
                return new JsonPrimitiveNode(jsonNode.decimalValue());
            }
            if (jsonNode.isBigInteger()) {
                return new JsonPrimitiveNode(jsonNode.bigIntegerValue());
            }
        }
        if (jsonNode.isBinary() || jsonNode.isTextual()) {
            return new JsonPrimitiveNode(jsonNode.textValue());
        }
        if (jsonNode.isBoolean()) {
            return new JsonPrimitiveNode(jsonNode.booleanValue());
        }

        // array
        if (jsonNode.isArray()) {
            JsonArrayNode arrayNode = new JsonArrayNode(jsonNode.size());
            for (JsonNode element : jsonNode) {
                arrayNode.add(create(element));
            }
            return arrayNode;
        }

        // object
        if (jsonNode.isObject()) {
            JsonObjectNode jsonObjectNode = new JsonObjectNode();
            Iterator<String> iter = jsonNode.fieldNames();
            while (iter.hasNext()) {
                String fieldName = iter.next();
                jsonObjectNode.addProperty(fieldName, create(jsonNode.findValue(fieldName)));
            }
            return jsonObjectNode;
        }
        if (jsonNode.isPojo()) {
            POJONode pojoNode = (POJONode) jsonNode;
            Object object = pojoNode.getPojo();
            return createFromPojo(object);
        }
        return JsonNullNode.INSTANCE;
    }
}
