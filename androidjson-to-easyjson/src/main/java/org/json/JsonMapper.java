/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.json;

import com.github.fangjinuo.easyjson.core.JSONBuilderProvider;
import com.github.fangjinuo.easyjson.core.JsonTreeNode;
import com.github.fangjinuo.easyjson.core.node.*;

import java.util.Iterator;
import java.util.Map;

public class JsonMapper {
    public static Object toJSON(Object object) {
        return fromJsonTreeNode(toJsonTreeNode(object));
    }

    public static Object fromJsonTreeNode(JsonTreeNode treeNode) {
        return JsonTreeNodes.toJSON(treeNode, new ToJSONMapper() {
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
            public Object mappingArray(JsonArrayNode arrayNode) {
                JSONArray jsonArray = new JSONArray();
                for (JsonTreeNode jsonTreeNode : arrayNode) {
                    jsonArray.put(JsonTreeNodes.toJSON(jsonTreeNode, this));
                }
                return jsonArray;
            }

            @Override
            public Object mappingObject(JsonObjectNode objectNode) {
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, JsonTreeNode> entry : objectNode.propertySet()) {
                    try {
                        jsonObject.put(entry.getKey(), JsonTreeNodes.toJSON(entry.getValue(), this));
                    } catch (JSONException ex) {
                        // TODO log
                    }
                }
                return jsonObject;
            }
        });
    }

    public static JsonTreeNode toJsonTreeNode(Object object) {
        return JsonTreeNodes.fromJavaObject(object, new ToJsonTreeNodeMapper() {
            @Override
            public JsonTreeNode mapping(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) object;
                    JsonArrayNode arrayNode = new JsonArrayNode();
                    for (Object item : jsonArray.values()) {
                        arrayNode.add(JsonTreeNodes.fromJavaObject(item, this));
                    }
                    return arrayNode;
                }

                if (object instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) object;
                    Iterator<String> iter = jsonObject.keys();
                    JsonObjectNode objectNode = new JsonObjectNode();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {
                            objectNode.addProperty(key, JsonTreeNodes.fromJavaObject(jsonObject.get(key), this));
                        } catch (Throwable ex) {

                        }
                        return objectNode;
                    }
                }

                if (object instanceof JSONTokener) {
                    JSONTokener jsonTokener = (JSONTokener) object;
                    return JSONBuilderProvider.simplest().fromJson(JsonTokeners.readToString(jsonTokener));
                }
                return null;
            }
        });
    }
}
