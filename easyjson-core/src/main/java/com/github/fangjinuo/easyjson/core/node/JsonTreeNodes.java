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

package com.github.fangjinuo.easyjson.core.node;

import com.github.fangjinuo.easyjson.core.JSON;
import com.github.fangjinuo.easyjson.core.JSONBuilderProvider;
import com.github.fangjinuo.easyjson.core.JsonTreeNode;

import java.util.*;

public class JsonTreeNodes {
    public static JsonTreeNode fromJavaObject(Object object) {
        if (object == null) {
            return JsonNullNode.INSTANCE;
        }
        JSON json = JSONBuilderProvider.create().build();
        String jsonString = json.toJson(object);
        return json.fromJson(jsonString);
    }

    public static Object toJavaObject(JsonTreeNode node) {
        if (node == null || JsonNullNode.INSTANCE == node) {
            return null;
        }
        if (node.isJsonPrimitiveNode()) {
            return node.getAsJsonPrimitiveNode().getValue();
        }
        if (node.isJsonArrayNode()) {
            JsonArrayNode arrayNode = node.getAsJsonArrayNode();
            List<Object> array = new ArrayList<Object>();
            for (int i = 0; i < arrayNode.size(); i++) {
                array.add(toJavaObject(arrayNode.get(i)));
            }
            return array;
        }
        if (node.isJsonObjectNode()) {
            JsonObjectNode objectNode = node.getAsJsonObjectNode();
            Iterator<Map.Entry<String, JsonTreeNode>> iter = objectNode.propertySet().iterator();
            Map<String, Object> map = new HashMap<String, Object>();
            while (iter.hasNext()) {
                Map.Entry<String, JsonTreeNode> entry = iter.next();
                map.put(entry.getKey(), toJavaObject(entry.getValue()));
            }
            return map;
        }
        return null;
    }
}
