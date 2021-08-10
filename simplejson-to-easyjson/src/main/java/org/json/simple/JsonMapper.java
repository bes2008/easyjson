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

package org.json.simple;

import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.*;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonMapper {

    public static Object fromJsonTreeNode(JsonTreeNode treeNode) {
        return JsonTreeNodes.toXxxJson(treeNode, new ToXxxJsonMapper<JSONObject, JSONArray, Object, Object>() {
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
            public JSONArray mappingArray(JsonArrayNode node) {
                JSONArray jsonArray = new JSONArray();
                for (JsonTreeNode item : node) {
                    jsonArray.add(JsonTreeNodes.toXxxJson(item, this));
                }
                return jsonArray;
            }

            @Override
            public JSONObject mappingObject(JsonObjectNode node) {
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, JsonTreeNode> entry : node.propertySet()) {
                    jsonObject.put(entry.getKey(), JsonTreeNodes.toXxxJson(entry.getValue(), this));
                }
                return jsonObject;
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
                    JsonArrayNode arrayNode = new JsonArrayNode();
                    for (Object item : (JSONArray) object) {
                        arrayNode.add(JsonTreeNodes.toJsonTreeNode(item, this));
                    }
                    return arrayNode;
                }

                if (object instanceof JSONObject) {
                    JsonObjectNode objectNode = new JsonObjectNode();
                    Iterator<Map.Entry<String, Object>> iter = ((JSONObject) object).entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        objectNode.addProperty(entry.getKey(), JsonTreeNodes.toJsonTreeNode(entry.getValue(), this));
                    }
                    return objectNode;
                }

                String jsonString = null;

                if (object instanceof JSONAware) {
                    jsonString = ((JSONAware) object).toJSONString();
                }

                if (object instanceof JSONStreamAware) {
                    StringWriter stringWriter = new StringWriter();
                    try {
                        ((JSONStreamAware) object).writeJSONString(stringWriter);
                    } catch (IOException e) {
                        // TODO log
                    }
                    jsonString = stringWriter.toString();
                }

                if (jsonString != null) {
                    return JSONBuilderProvider.simplest().fromJson(jsonString);
                }

                return null;
            }
        });

    }
}
