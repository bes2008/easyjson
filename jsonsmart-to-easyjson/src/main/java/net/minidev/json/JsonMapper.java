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

package net.minidev.json;

import com.github.fangjinuo.easyjson.core.JsonTreeNode;
import com.github.fangjinuo.easyjson.core.node.JsonArrayNode;
import com.github.fangjinuo.easyjson.core.node.JsonNullNode;
import com.github.fangjinuo.easyjson.core.node.JsonObjectNode;

import java.util.*;

public class JsonMapper {
    public static Object fromJsonTreeNode(JsonTreeNode node){
        if (node == null || JsonNullNode.INSTANCE == node) {
            return null;
        }
        if (node.isJsonPrimitiveNode()) {
            return node.getAsJsonPrimitiveNode().getValue();
        }
        if (node.isJsonArrayNode()) {
            JsonArrayNode arrayNode = node.getAsJsonArrayNode();
            JSONArray array = new JSONArray();
            for (int i = 0; i < arrayNode.size(); i++) {
                array.appendElement(fromJsonTreeNode(arrayNode.get(i)));
            }
            return array;
        }
        if (node.isJsonObjectNode()) {
            JsonObjectNode objectNode = node.getAsJsonObjectNode();
            Iterator<Map.Entry<String, JsonTreeNode>> iter = objectNode.propertySet().iterator();
            JSONObject map = new JSONObject();
            while (iter.hasNext()) {
                Map.Entry<String, JsonTreeNode> entry = iter.next();
                map.appendField(entry.getKey(), fromJsonTreeNode(entry.getValue()));
            }
            return map;
        }
        return null;
    }
}
