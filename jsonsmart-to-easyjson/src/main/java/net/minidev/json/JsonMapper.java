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

package net.minidev.json;

import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.*;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonMapper {
    public static Object fromJsonTreeNode(JsonTreeNode node) {
        return JsonTreeNodes.toXxxJson(node, new ToXxxJsonMapper<JSONObject, JSONArray, Object, Object>() {
            @Override
            public Object mappingNull(JsonNullNode node) {
                return null;
            }

            @Override
            public Object mappingPrimitive(JsonPrimitiveNode node) {
                if (node.isBoolean()) {
                    return node.getAsBoolean();
                }
                if (node.isString()) {
                    return node.getAsString();
                }
                if (node.isNumber()) {
                    return node.getAsNumber();
                }
                return node.getValue();
            }

            @Override
            public JSONArray mappingArray(JsonArrayNode arrayNode) {
                JSONArray array = new JSONArray();
                for (int i = 0; i < arrayNode.size(); i++) {
                    array.appendElement(JsonTreeNodes.toXxxJson(arrayNode.get(i), this));
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
                    map.appendField(entry.getKey(), JsonTreeNodes.toXxxJson(entry.getValue(), this));
                }
                return map;
            }
        });
    }


    private static final List<Class> supportedTypes = Collects.<Class>newArrayList(
            JSONArray.class,
            JSONObject.class,
            JSONAware.class,
            JSONStreamAware.class
    );

    public static JsonTreeNode toJsonTreeNode(Object object) {
        return JsonTreeNodes.toJsonTreeNode(object, new ToJsonTreeNodeMapper() {
            @Override
            public boolean isAcceptable(final Object object) {
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
                    JsonArrayNode array = new JsonArrayNode();
                    JSONArray jsonArray = (JSONArray) object;
                    for (Object item : jsonArray) {
                        array.add(JsonTreeNodes.toJsonTreeNode(item, this));
                    }
                    return array;
                }

                if (object instanceof JSONObject) {
                    JsonObjectNode objectNode = new JsonObjectNode();
                    JSONObject obj = (JSONObject) object;
                    for (String key : obj.keySet()) {
                        objectNode.addProperty(key, JsonTreeNodes.toJsonTreeNode(obj.get(key), this));
                    }
                    return objectNode;
                }

                String jsonString = null;
                if (object instanceof JSONAware) {
                    jsonString = ((JSONAware) object).toJSONString();
                }
                if (object instanceof JSONStreamAware) {
                    StringBuilder stringBuilder = new StringBuilder();
                    try {
                        ((JSONStreamAware) object).writeJSONString(stringBuilder);
                    } catch (IOException e) {
                        // TODO log
                    }
                    jsonString = stringBuilder.toString();
                }
                if (jsonString != null) {
                    return JSONBuilderProvider.simplest().fromJson(jsonString);
                }
                return null;
            }
        });
    }
}
