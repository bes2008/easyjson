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

package com.jn.easyjson.core.tree.bind;


import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.JsonArrayNode;
import com.jn.easyjson.core.node.JsonNullNode;
import com.jn.easyjson.core.node.JsonObjectNode;
import com.jn.easyjson.core.node.JsonPrimitiveNode;
import com.jn.easyjson.core.tree.stream.JsonReader;
import com.jn.easyjson.core.tree.stream.JsonWriter;
import com.jn.easyjson.core.util.LazilyParsedNumber;

import java.io.IOException;
import java.util.Map;

/**
 * Type adapters for basic types.
 */
public final class TypeAdapters {
    private TypeAdapters() {
        throw new UnsupportedOperationException();
    }

    public static final TypeAdapter<JsonTreeNode> JSON_ELEMENT = new TypeAdapter<JsonTreeNode>() {
        @Override
        public JsonTreeNode read(JsonReader in) throws IOException {
            switch (in.peek()) {
                case STRING:
                    return new JsonPrimitiveNode(in.nextString());
                case NUMBER:
                    String number = in.nextString();
                    return new JsonPrimitiveNode(new LazilyParsedNumber(number));
                case BOOLEAN:
                    return new JsonPrimitiveNode(in.nextBoolean());
                case NULL:
                    in.nextNull();
                    return JsonNullNode.INSTANCE;
                case BEGIN_ARRAY:
                    JsonArrayNode array = new JsonArrayNode();
                    in.beginArray();
                    while (in.hasNext()) {
                        array.add(read(in));
                    }
                    in.endArray();
                    return array;
                case BEGIN_OBJECT:
                    JsonObjectNode object = new JsonObjectNode();
                    in.beginObject();
                    while (in.hasNext()) {
                        object.addProperty(in.nextName(), read(in));
                    }
                    in.endObject();
                    return object;
                case END_DOCUMENT:
                case NAME:
                case END_OBJECT:
                case END_ARRAY:
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public void write(JsonWriter out, JsonTreeNode value) throws IOException {
            if (value == null || value.isJsonNullNode()) {
                out.nullValue();
            } else if (value.isJsonPrimitiveNode()) {
                JsonPrimitiveNode primitive = value.getAsJsonPrimitiveNode();
                if (primitive.isNumberNode()) {
                    out.value(primitive.getAsNumber());
                } else if (primitive.isBooleanNode()) {
                    out.value(primitive.getAsBoolean());
                } else {
                    out.value(primitive.getAsString());
                }

            } else if (value.isJsonArrayNode()) {
                out.beginArray();
                for (JsonTreeNode e : value.getAsJsonArrayNode()) {
                    write(out, e);
                }
                out.endArray();

            } else if (value.isJsonObjectNode()) {
                out.beginObject();
                for (Map.Entry<String, JsonTreeNode> e : value.getAsJsonObjectNode().propertySet()) {
                    out.name(e.getKey());
                    write(out, e.getValue());
                }
                out.endObject();

            } else {
                throw new IllegalArgumentException("Couldn't write " + value.getClass());
            }
        }
    };

}