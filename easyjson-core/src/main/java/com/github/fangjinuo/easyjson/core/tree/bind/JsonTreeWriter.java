/*
 *  Copyright 2019 the original author or authors.
 *
 *  Licensed under the LGPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.github.fangjinuo.easyjson.core.tree.bind;


import com.github.fangjinuo.easyjson.core.JsonTreeNode;
import com.github.fangjinuo.easyjson.core.node.JsonArrayNode;
import com.github.fangjinuo.easyjson.core.node.JsonNullNode;
import com.github.fangjinuo.easyjson.core.node.JsonObjectNode;
import com.github.fangjinuo.easyjson.core.node.JsonPrimitiveNode;
import com.github.fangjinuo.easyjson.core.tree.stream.JsonWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * This writer creates a JsonTreeNode.
 */
public final class JsonTreeWriter extends JsonWriter {
    private static final Writer UNWRITABLE_WRITER = new Writer() {
        @Override
        public void write(char[] buffer, int offset, int counter) {
            throw new AssertionError();
        }

        @Override
        public void flush() throws IOException {
            throw new AssertionError();
        }

        @Override
        public void close() throws IOException {
            throw new AssertionError();
        }
    };
    /**
     * Added to the top of the stack when this writer is closed to cause following ops to fail.
     */
    private static final JsonPrimitiveNode SENTINEL_CLOSED = new JsonPrimitiveNode("closed");

    /**
     * The JsonTreeNodes and JsonArrayNodes under modification, outermost to innermost.
     */
    private final List<JsonTreeNode> stack = new ArrayList<JsonTreeNode>();

    /**
     * The name for the next JSON object value. If non-null, the top of the stack is a JsonObjectNode.
     */
    private String pendingName;

    /**
     * the JSON element constructed by this writer.
     */
    private JsonTreeNode product = JsonNullNode.INSTANCE; // TODO: is this really what we want?;

    public JsonTreeWriter() {
        super(UNWRITABLE_WRITER);
    }

    /**
     * Returns the top level object produced by this writer.
     */
    public JsonTreeNode get() {
        if (!stack.isEmpty()) {
            throw new IllegalStateException("Expected one JSON element but was " + stack);
        }
        return product;
    }

    private JsonTreeNode peek() {
        return stack.get(stack.size() - 1);
    }

    private void put(JsonTreeNode value) {
        if (pendingName != null) {
            if (!value.isJsonNullNode() || getSerializeNulls()) {
                JsonObjectNode object = (JsonObjectNode) peek();
                object.addProperty(pendingName, value);
            }
            pendingName = null;
        } else if (stack.isEmpty()) {
            product = value;
        } else {
            JsonTreeNode element = peek();
            if (element instanceof JsonArrayNode) {
                ((JsonArrayNode) element).add(value);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public JsonWriter beginArray() throws IOException {
        JsonArrayNode array = new JsonArrayNode();
        put(array);
        stack.add(array);
        return this;
    }

    @Override
    public JsonWriter endArray() throws IOException {
        if (stack.isEmpty() || pendingName != null) {
            throw new IllegalStateException();
        }
        JsonTreeNode element = peek();
        if (element instanceof JsonArrayNode) {
            stack.remove(stack.size() - 1);
            return this;
        }
        throw new IllegalStateException();
    }

    @Override
    public JsonWriter beginObject() throws IOException {
        JsonObjectNode object = new JsonObjectNode();
        put(object);
        stack.add(object);
        return this;
    }

    @Override
    public JsonWriter endObject() throws IOException {
        if (stack.isEmpty() || pendingName != null) {
            throw new IllegalStateException();
        }
        JsonTreeNode element = peek();
        if (element instanceof JsonObjectNode) {
            stack.remove(stack.size() - 1);
            return this;
        }
        throw new IllegalStateException();
    }

    @Override
    public JsonWriter name(String name) throws IOException {
        if (stack.isEmpty() || pendingName != null) {
            throw new IllegalStateException();
        }
        JsonTreeNode element = peek();
        if (element instanceof JsonObjectNode) {
            pendingName = name;
            return this;
        }
        throw new IllegalStateException();
    }

    @Override
    public JsonWriter value(String value) throws IOException {
        if (value == null) {
            return nullValue();
        }
        put(new JsonPrimitiveNode(value));
        return this;
    }

    @Override
    public JsonWriter nullValue() throws IOException {
        put(JsonNullNode.INSTANCE);
        return this;
    }

    @Override
    public JsonWriter value(boolean value) throws IOException {
        put(new JsonPrimitiveNode(value));
        return this;
    }

    @Override
    public JsonWriter value(Boolean value) throws IOException {
        if (value == null) {
            return nullValue();
        }
        put(new JsonPrimitiveNode(value));
        return this;
    }

    @Override
    public JsonWriter value(double value) throws IOException {
        if (!isLenient() && (Double.isNaN(value) || Double.isInfinite(value))) {
            throw new IllegalArgumentException("JSON forbids NaN and infinities: " + value);
        }
        put(new JsonPrimitiveNode(value));
        return this;
    }

    @Override
    public JsonWriter value(long value) throws IOException {
        put(new JsonPrimitiveNode(value));
        return this;
    }

    @Override
    public JsonWriter value(Number value) throws IOException {
        if (value == null) {
            return nullValue();
        }

        if (!isLenient()) {
            double d = value.doubleValue();
            if (Double.isNaN(d) || Double.isInfinite(d)) {
                throw new IllegalArgumentException("JSON forbids NaN and infinities: " + value);
            }
        }

        put(new JsonPrimitiveNode(value));
        return this;
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
        if (!stack.isEmpty()) {
            throw new IOException("Incomplete document");
        }
        stack.add(SENTINEL_CLOSED);
    }
}
