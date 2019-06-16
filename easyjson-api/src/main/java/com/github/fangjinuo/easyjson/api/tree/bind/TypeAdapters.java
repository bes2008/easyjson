package com.github.fangjinuo.easyjson.api.tree.bind;


import com.github.fangjinuo.easyjson.api.JsonTreeNode;
import com.github.fangjinuo.easyjson.api.node.JsonArrayNode;
import com.github.fangjinuo.easyjson.api.node.JsonNullNode;
import com.github.fangjinuo.easyjson.api.node.JsonObjectNode;
import com.github.fangjinuo.easyjson.api.node.JsonPrimitiveNode;
import com.github.fangjinuo.easyjson.api.tree.stream.JsonReader;
import com.github.fangjinuo.easyjson.api.tree.stream.JsonWriter;
import com.github.fangjinuo.easyjson.api.util.LazilyParsedNumber;

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
        @Override public JsonTreeNode read(JsonReader in) throws IOException {
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

        @Override public void write(JsonWriter out, JsonTreeNode value) throws IOException {
            if (value == null || value.isJsonNullNode()) {
                out.nullValue();
            } else if (value.isJsonPrimitiveNode()) {
                JsonPrimitiveNode primitive = value.getAsJsonPrimitiveNode();
                if (primitive.isNumber()) {
                    out.value(primitive.getAsNumber());
                } else if (primitive.isBoolean()) {
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