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

package com.jn.easyjson.core.node;

import com.jn.easyjson.core.JsonException;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.util.JSONs;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.reflect.type.Primitives;

import java.util.*;

public class JsonTreeNodes extends JsonNodeNavigator {
    public static JsonTreeNode toJsonTreeNode(Object object) {
        return toJsonTreeNode(object, null);
    }

    public static JsonTreeNode toJsonTreeNode(Object object, final ToJsonTreeNodeMapper mapper) {
        if (object == null) {
            return JsonNullNode.INSTANCE;
        }
        if (object instanceof JsonTreeNode) {
            return (JsonTreeNode) object;
        }

        JsonTreeNode node = null;
        if (mapper != null && mapper.isAcceptable(object)) {
            node = mapper.mapping(object);
            if (node != null) {
                return node;
            }
        }

        if (Primitives.isPrimitiveOrPrimitiveWrapperType(object.getClass())) {
            return new JsonPrimitiveNode(object);
        }

        if (object instanceof Collection) {
            JsonArrayNode arrayNode = new JsonArrayNode();
            Collection c = (Collection) object;
            for (Object e : c) {
                JsonTreeNode element = toJsonTreeNode(e, mapper);
                arrayNode.add(element);
            }
            return arrayNode;
        }

        if (object.getClass().isArray()) {
            Object[] array = (Object[]) object;
            JsonArrayNode arrayNode = new JsonArrayNode();
            for (int i = 0; i < array.length; i++) {
                Object e = array[i];
                JsonTreeNode element = toJsonTreeNode(e, mapper);
                arrayNode.add(element);
            }
            return arrayNode;
        }

        if (object instanceof Map) {
            final JsonObjectNode objectNode = new JsonObjectNode();
            Map map = (Map) object;
            Collects.forEach(map, new Consumer2<Object, Object>() {
                @Override
                public void accept(Object key, Object value) {
                    objectNode.addProperty(key.toString(), toJsonTreeNode(value, mapper));
                }
            });
            return objectNode;
        }

        if (object instanceof Enum) {
            return new JsonPrimitiveNode(((Enum) object).name());
        }

        String jsonString = null;
        if (object instanceof String) {
            jsonString = (String) object;
        } else {
            jsonString = JSONs.toJson(object);
        }
        try {
            if (JSONs.isJsonString(jsonString) || JSONs.isJsonArrayOrObject(jsonString)) {
                JsonTreeNode jsonTreeNode = JSONs.parse(jsonString);
                if (jsonTreeNode != null) {
                    return jsonTreeNode;
                }
            }
            return new JsonPrimitiveNode(jsonString);
        } catch (JsonException ex) {
            return new JsonPrimitiveNode(jsonString);
        }
    }

    public static Object toJavaObject(JsonTreeNode node) {
        return toXxxJson(node, null);
    }

    public static Object toXxxJson(JsonTreeNode node, ToXxxJsonMapper mapper) {
        if (node == null || JsonNullNode.INSTANCE == node) {
            if (mapper != null) {
                return mapper.mappingNull(JsonNullNode.INSTANCE);
            }
            return null;
        }
        if (node.isJsonPrimitiveNode()) {
            if (mapper != null) {
                Object obj = mapper.mappingPrimitive(node.getAsJsonPrimitiveNode());
                if (obj != null) {
                    return obj;
                }
            }
            JsonPrimitiveNode primitiveNode = node.getAsJsonPrimitiveNode();
            if (primitiveNode.isNumberNode()) {
                return node.getAsNumber();
            }
            if (primitiveNode.isStringNode()) {
                return node.getAsString();
            }
            if (primitiveNode.isBooleanNode()) {
                return node.getAsBoolean();
            }
            return primitiveNode.getValue();
        }
        if (node.isJsonArrayNode()) {
            if (mapper != null) {
                Object obj = mapper.mappingArray(node.getAsJsonArrayNode());
                if (obj != null) {
                    return obj;
                }
            }
            JsonArrayNode arrayNode = node.getAsJsonArrayNode();
            List<Object> array = Lists.<Object>newArrayList();
            for (int i = 0; i < arrayNode.size(); i++) {
                array.add(toJavaObject(arrayNode.get(i)));
            }
            return array;
        }
        if (node.isJsonObjectNode()) {
            if (mapper != null) {
                Object obj = mapper.mappingObject(node.getAsJsonObjectNode());
                if (obj != null) {
                    return obj;
                }
            }
            JsonObjectNode objectNode = node.getAsJsonObjectNode();
            Iterator<Map.Entry<String, JsonTreeNode>> iter = objectNode.propertySet().iterator();
            Map<String, Object> map = Maps.<String, Object>newLinkedHashMap();
            while (iter.hasNext()) {
                Map.Entry<String, JsonTreeNode> entry = iter.next();
                map.put(entry.getKey(), toJavaObject(entry.getValue()));
            }
            return map;
        }
        return node;
    }

    /**
     * merge node2 to node1
     *
     * @return an new node
     */
    public static JsonTreeNode combine(JsonTreeNode node1, JsonTreeNode node2) {
        if (node1.isJsonNullNode()) {
            return node2.deepCopy();
        }
        if (node2.isJsonNullNode()) {
            return node1.deepCopy();
        }

        if (node1.isJsonPrimitiveNode()) {
            if (node2.isJsonPrimitiveNode() || node2.isJsonObjectNode()) {
                return node1.deepCopy();
            }
        }

        if (node1.isJsonArrayNode() || node2.isJsonArrayNode()) {
            JsonArrayNode arrayNode = node1.isJsonArrayNode() ? (JsonArrayNode) node1.deepCopy() : (JsonArrayNode) node2.deepCopy();
            node2 = node1.isJsonArrayNode() ? node2.deepCopy() : node1.deepCopy();
            if (!node2.isJsonArrayNode()) {
                arrayNode.add(node2);
            } else {
                arrayNode.addAll(((JsonArrayNode) node2));
            }
            return node1;
        }

        if (node1.isJsonObjectNode() && node2.isJsonObjectNode()) {
            JsonObjectNode obj1 = (JsonObjectNode) node1.deepCopy();
            JsonObjectNode obj2 = (JsonObjectNode) node2.deepCopy();
            for (Map.Entry<String, JsonTreeNode> entry : obj2.propertySet()) {
                obj1.addProperty(entry.getKey(), entry.getValue());
            }
            return obj1;
        }

        return node1.deepCopy();

    }
}
