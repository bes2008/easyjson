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

package com.github.fangjinuo.easyjson.core.tree;


import com.github.fangjinuo.easyjson.core.JsonTreeNode;
import com.github.fangjinuo.easyjson.core.node.JsonNullNode;
import com.github.fangjinuo.easyjson.core.tree.bind.TypeAdapters;
import com.github.fangjinuo.easyjson.core.tree.stream.JsonReader;
import com.github.fangjinuo.easyjson.core.tree.stream.JsonWriter;
import com.github.fangjinuo.easyjson.core.tree.stream.MalformedJsonException;

import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;

/**
 * Reads and writes JSON parse trees over streams.
 */
public final class Streams {
    private Streams() {
        throw new UnsupportedOperationException();
    }

    /**
     * Takes a reader in any state and returns the next value as a JsonTreeNode.
     */
    public static JsonTreeNode parse(JsonReader reader) throws JsonParseException {
        boolean isEmpty = true;
        try {
            reader.peek();
            isEmpty = false;
            return TypeAdapters.JSON_ELEMENT.read(reader);
        } catch (EOFException e) {
            /*
             * For compatibility with JSON 1.5 and earlier, we return a JsonNullNode for
             * empty documents instead of throwing.
             */
            if (isEmpty) {
                return JsonNullNode.INSTANCE;
            }
            // The stream ended prematurely so it is likely a syntax error.
            throw new JsonSyntaxException(e);
        } catch (MalformedJsonException e) {
            throw new JsonSyntaxException(e);
        } catch (IOException e) {
            throw new JsonIOException(e);
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * Writes the JSON element to the writer, recursively.
     */
    public static void write(JsonTreeNode element, JsonWriter writer) throws IOException {
        TypeAdapters.JSON_ELEMENT.write(writer, element);
    }

    public static Writer writerForAppendable(Appendable appendable) {
        return appendable instanceof Writer ? (Writer) appendable : new AppendableWriter(appendable);
    }

    /**
     * Adapts an {@link Appendable} so it can be passed anywhere a {@link Writer}
     * is used.
     */
    private static final class AppendableWriter extends Writer {
        private final Appendable appendable;
        private final CurrentWrite currentWrite = new CurrentWrite();

        AppendableWriter(Appendable appendable) {
            this.appendable = appendable;
        }

        @Override public void write(char[] chars, int offset, int length) throws IOException {
            currentWrite.chars = chars;
            appendable.append(currentWrite, offset, offset + length);
        }

        @Override public void write(int i) throws IOException {
            appendable.append((char) i);
        }

        @Override public void flush() {}
        @Override public void close() {}

        /**
         * A mutable char sequence pointing at a single char[].
         */
        static class CurrentWrite implements CharSequence {
            char[] chars;
            public int length() {
                return chars.length;
            }
            public char charAt(int i) {
                return chars[i];
            }
            public CharSequence subSequence(int start, int end) {
                return new String(chars, start, end - start);
            }
        }
    }

}
