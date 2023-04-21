package com.jn.easyjson.fastjson.node;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.*;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FastjsonMapper {

    private static final List<Class> supportedTypes = Collects.<Class>newArrayList(
            JSONArray.class,
            JSONObject.class
    );

    public static JsonTreeNode toJsonTreeNode(Object object) {
        return JsonTreeNodes.toJsonTreeNode(object, new ToJsonTreeNodeMapper() {
            @Override
            public boolean isAcceptable(@NonNull final Object object) {
                return Collects.anyMatch(supportedTypes, new Predicate<Class>() {
                    @Override
                    public boolean test(Class value) {
                        return Reflects.isSubClassOrEquals(value, object.getClass());
                    }
                });
            }

            @Override
            public JsonTreeNode mapping(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) object;
                    JsonArrayNode arrayNode = new JsonArrayNode();
                    for (Object item : jsonArray) {
                        arrayNode.add(JsonTreeNodes.toJsonTreeNode(item, this));
                    }
                    return arrayNode;
                }

                if (object instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) object;
                    JsonObjectNode objectNode = new JsonObjectNode();
                    Iterator<Map.Entry<String, Object>> iter = jsonObject.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        JsonTreeNode value = JsonTreeNodes.toJsonTreeNode(entry.getValue(), this);
                        objectNode.addProperty(entry.getKey(), value);
                    }
                    return objectNode;
                }
                return null;
            }
        });
    }

    public static Object fromJsonTreeNode(JsonTreeNode treeNode) {
        return JsonTreeNodes.toXxxJson(treeNode, new ToXxxJsonMapper<JSONObject, JSONArray, Object, JSON>() {
            @Override
            public JSON mappingNull(JsonNullNode node) {
                return null;
            }

            @Override
            public Object mappingPrimitive(JsonPrimitiveNode node) {
                if (node.isNumberNode()) {
                    return node.getAsNumber();
                }
                if (node.isStringNode()) {
                    return node.getAsString();
                }
                if (node.isBooleanNode()) {
                    return node.getAsBoolean();
                }
                return node.getValue();
            }

            @Override
            public JSONArray mappingArray(JsonArrayNode arrayNode) {
                JSONArray array = new JSONArray();
                for (int i = 0; i < arrayNode.size(); i++) {
                    array.add(JsonTreeNodes.toXxxJson(arrayNode.get(i), this));
                }
                return array;
            }

            @Override
            public JSONObject mappingObject(JsonObjectNode node) {
                JsonObjectNode objectNode = node.getAsJsonObjectNode();
                Iterator<Map.Entry<String, JsonTreeNode>> iter = objectNode.propertySet().iterator();
                JSONObject map = new JSONObject();
                while (iter.hasNext()) {
                    Map.Entry<String, JsonTreeNode> entry = iter.next();
                    map.put(entry.getKey(), JsonTreeNodes.toXxxJson(entry.getValue(), this));
                }
                return map;
            }
        });
    }
}
