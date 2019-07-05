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

package com.eclipsesource.json;

import com.github.fangjinuo.easyjson.core.JsonTreeNode;
import com.github.fangjinuo.easyjson.core.node.JsonArrayNode;
import com.github.fangjinuo.easyjson.core.node.JsonNullNode;
import com.github.fangjinuo.easyjson.core.node.JsonObjectNode;
import com.github.fangjinuo.easyjson.core.node.JsonPrimitiveNode;

import java.util.Map;

public class JsonMapper {

    public static JsonValue fromJsonTreeNode(JsonTreeNode treeNode) {
        if (treeNode == null || treeNode instanceof JsonNullNode) {
            return Json.NULL;
        }

        if (treeNode.isJsonPrimitiveNode()) {
            JsonPrimitiveNode primitiveNode = treeNode.getAsJsonPrimitiveNode();
            if (primitiveNode.isBoolean()) {
                return primitiveNode.getAsBoolean() ? Json.TRUE : Json.FALSE;
            }
            if (primitiveNode.isString()) {
                return new JsonString(primitiveNode.getAsString());
            }
            if (primitiveNode.isNumber()) {
                return new JsonNumber(primitiveNode.getAsString());
            }
        }

        if (treeNode.isJsonArrayNode()) {
            JsonArrayNode arrayNode = treeNode.getAsJsonArrayNode();
            JsonArray jsonArray = new JsonArray();
            for (JsonTreeNode node : arrayNode) {
                jsonArray.add(fromJsonTreeNode(node));
            }
            return jsonArray;
        }

        if (treeNode.isJsonObjectNode()) {
            JsonObjectNode objectNode = treeNode.getAsJsonObjectNode();
            JsonObject jsonObject = new JsonObject();
            for (Map.Entry<String, JsonTreeNode> entry : objectNode.propertySet()) {
                jsonObject.add(entry.getKey(), fromJsonTreeNode(entry.getValue()));
            }
            return jsonObject;
        }
        return Json.NULL;
    }


    public static JsonTreeNode toJsonTreeNode(JsonValue jsonValue) {
        if (isNull(jsonValue)) {
            return JsonNullNode.INSTANCE;
        }

        if (jsonValue instanceof JsonLiteral) {
            return new JsonPrimitiveNode(jsonValue.asBoolean());
        }

        if (jsonValue instanceof JsonString) {
            return new JsonPrimitiveNode(jsonValue.asString());
        }

        if (jsonValue instanceof JsonNumber) {
            return new JsonPrimitiveNode(jsonValue.asDouble());
        }

        if (jsonValue instanceof JsonArray) {
            JsonArray jsonArray = (JsonArray) jsonValue;
            JsonArrayNode arrayNode = new JsonArrayNode(jsonArray.size());
            for (JsonValue item : jsonArray) {
                arrayNode.add(toJsonTreeNode(item));
            }
            return arrayNode;
        }

        if (jsonValue instanceof JsonObject) {
            JsonObject jsonObject = (JsonObject) jsonValue;
            JsonObjectNode objectNode = new JsonObjectNode();
            for (JsonObject.Member member : jsonObject) {
                objectNode.addProperty(member.getName(), toJsonTreeNode(member.getValue()));
            }
            return objectNode;
        }

        return JsonNullNode.INSTANCE;
    }

    private static boolean isNull(JsonValue jsonValue) {
        return jsonValue == null || jsonValue.isNull();
    }

}
