package com.jn.easyjson.javax.json.node;

import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.*;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;

import javax.json.*;
import java.util.Map;

public class JsonMapper {

    public static JsonTreeNode toJsonTreeNode(Object object) {
        return JsonTreeNodes.toJsonTreeNode(object, new ToJsonTreeNodeMapper() {
            @Override
            public JsonTreeNode mapping(Object object) {
                return doMapping((JsonValue) object);
            }

            private JsonTreeNode doMapping(JsonValue node) {
                JsonValue.ValueType valueType = node.getValueType();
                JsonTreeNode treeNode = null;
                switch (valueType) {
                    case NULL:
                        treeNode = JsonNullNode.INSTANCE;
                        break;
                    case TRUE:
                        treeNode = new JsonPrimitiveNode(true);
                        break;
                    case FALSE:
                        treeNode = new JsonPrimitiveNode(false);
                        break;
                    case NUMBER:
                        treeNode = new JsonPrimitiveNode(Numbers.createNumber(node.toString()));
                        break;
                    case STRING:
                        treeNode = new JsonPrimitiveNode(node.toString());
                        break;
                    case ARRAY:
                        JsonArray jsonArray = node.asJsonArray();
                        JsonArrayNode arrayNode = new JsonArrayNode();
                        for (JsonValue element : jsonArray) {
                            arrayNode.add(doMapping(element));
                        }
                        treeNode = arrayNode;
                        break;
                    case OBJECT:
                        JsonObject jsonObject = node.asJsonObject();
                        final JsonObjectNode objectNode = new JsonObjectNode();
                        Collects.forEach(jsonObject, new Consumer2<String, JsonValue>() {
                            @Override
                            public void accept(String key, JsonValue value) {
                                objectNode.addProperty(key, doMapping(value));
                            }
                        });
                        treeNode = objectNode;
                        break;
                    default:
                        break;
                }
                return treeNode;
            }

            @Override
            public boolean isAcceptable(Object object) {
                return object instanceof JsonValue;
            }
        });
    }

    public static JsonValue fromJsonTreeNode(JsonTreeNode treeNode) {
        return (JsonValue) JsonTreeNodes.fromJsonTreeNode(treeNode, new ToJSONMapper<JsonObject, JsonArray, JsonValue, JsonValue>() {
            @Override
            public JsonValue mappingNull(JsonNullNode node) {
                return JsonValue.NULL;
            }

            @Override
            public JsonValue mappingPrimitive(JsonPrimitiveNode node) {
                if (node.isBoolean()) {
                    boolean value = node.getAsBoolean();
                    return value ? JsonValue.TRUE : JsonValue.FALSE;
                } else if (node.isNumber()) {
                    if (node.isByte() || node.isShort() || node.isInteger()) {
                        return Json.createValue(node.getAsInt());
                    }
                    if (node.isFloat() || node.isDouble()) {
                        return Json.createValue(node.getAsDouble());
                    }
                    if (node.isBigInteger()) {
                        return Json.createValue(node.getAsBigInteger());
                    }
                    if (node.isBigDecimal()) {
                        return Json.createValue(node.getAsBigDecimal());
                    }
                    if (node.isLong()) {
                        return Json.createValue(node.getAsLong());
                    }
                } else if (node.isChar()) {
                    return Json.createValue(node.getAsCharacter() + "");
                }

                return Json.createValue(node.getAsString());
            }

            @Override
            public JsonArray mappingArray(JsonArrayNode node) {
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                for (JsonTreeNode treeNode : node) {
                    arrayBuilder.add((JsonValue) JsonTreeNodes.fromJsonTreeNode(treeNode, this));
                }
                return arrayBuilder.build();
            }

            @Override
            public JsonObject mappingObject(JsonObjectNode node) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                for (Map.Entry<String, JsonTreeNode> entry : node.propertySet()) {
                    String name = entry.getKey();
                    JsonValue value = (JsonValue) JsonTreeNodes.fromJsonTreeNode(entry.getValue(), this);
                    objectBuilder.add(name, value);
                }
                return objectBuilder.build();
            }
        });
    }

}
