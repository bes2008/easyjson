/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.fastjson.easyjson;

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

public class JsonMapper {

    private static final List<Class> supportedTypes = Collects.<Class>newArrayList(
            JSONSerializable.class,
            JSONArray.class,
            JSONObject.class,
            JSONAware.class
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

                if (object instanceof JSONAware) {
                    JSONAware jsonAware = (JSONAware) object;
                    return JSONBuilderProvider.simplest().fromJson(jsonAware.toJSONString());
                }

                if (object instanceof JSONSerializable) {
                    String json = JSON.toJSONString(object);
                    return JSONBuilderProvider.simplest().fromJson(json);
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
