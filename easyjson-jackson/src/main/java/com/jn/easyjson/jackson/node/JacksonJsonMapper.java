package com.jn.easyjson.jackson.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.*;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class JacksonJsonMapper {

    public static JsonTreeNode toJsonTreeNode(Object obj) {
        return JsonTreeNodes.toJsonTreeNode(obj, new JacksonToTreeNodeMapper());
    }

    public static JsonNode fromJsonTreeNode(JsonTreeNode treeNode) {
        return (JsonNode) JsonTreeNodes.toXxxJson(treeNode, new JacksonToJsonMapper());
    }

    static class JacksonToJsonMapper implements ToXxxJsonMapper<ObjectNode, ArrayNode, ValueNode, NullNode> {
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

    static class JacksonToTreeNodeMapper implements ToJsonTreeNodeMapper {
        @Override
        public JsonTreeNode mapping(Object object) {
            return create((JsonNode) object);
        }

        @Override
        public boolean isAcceptable(Object object) {
            return object instanceof JsonNode;
        }

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
                final JsonObjectNode jsonObjectNode = new JsonObjectNode();
                Collects.forEach(map, new Consumer2<Object, Object>() {
                    @Override
                    public void accept(Object key, Object value) {
                        jsonObjectNode.addProperty(key.toString(), createFromPojo(value));
                    }
                });

                return jsonObjectNode;
            }
            return new JsonPrimitiveNode(object.toString());
        }

        private JsonTreeNode create(JsonNode jsonNode) {
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
                    jsonObjectNode.addProperty(fieldName, create(jsonNode.get(fieldName)));
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

}
