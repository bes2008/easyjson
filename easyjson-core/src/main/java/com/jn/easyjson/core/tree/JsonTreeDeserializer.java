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

package com.jn.easyjson.core.tree;

import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.tree.stream.JsonReader;
import com.jn.easyjson.core.tree.stream.JsonToken;
import com.jn.easyjson.core.tree.stream.MalformedJsonException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * A parser to parse Json into a parse tree of {@link JsonTreeNode}s
 */
public final class JsonTreeDeserializer {

    /**
     * Parses the specified JSON string into a parse tree
     *
     * @param json JSON text
     * @return a parse tree of {@link JsonTreeNode}s corresponding to the specified JSON
     * @throws JsonParseException if the specified text is not valid JSON
     * @since 1.3
     */
    public JsonTreeNode parse(String json) throws JsonSyntaxException {
        return parse(new StringReader(json));
    }

    /**
     * Parses the specified JSON string into a parse tree
     *
     * @param json JSON text
     * @return a parse tree of {@link JsonTreeNode}s corresponding to the specified JSON
     * @throws JsonParseException if the specified text is not valid JSON
     * @since 1.3
     */
    public JsonTreeNode parse(Reader json) throws JsonIOException, JsonSyntaxException {
        try {
            JsonReader jsonReader = new JsonReader(json);
            JsonTreeNode element = parse(jsonReader);
            if (!element.isJsonNullNode() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonSyntaxException("Did not consume the entire document.");
            }
            return element;
        } catch (MalformedJsonException e) {
            throw new JsonSyntaxException(e);
        } catch (IOException e) {
            throw new JsonIOException(e);
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * Returns the next value from the JSON stream as a parse tree.
     *
     * @throws JsonParseException if there is an IOException or if the specified
     *                            text is not valid JSON
     * @since 1.6
     */
    public JsonTreeNode parse(JsonReader json) throws JsonIOException, JsonSyntaxException {
        boolean lenient = json.isLenient();
        json.setLenient(true);
        try {
            return Streams.parse(json);
        } catch (StackOverflowError e) {
            throw new JsonParseException("Failed parsing JSON source: " + json + " to Json", e);
        } catch (OutOfMemoryError e) {
            throw new JsonParseException("Failed parsing JSON source: " + json + " to Json", e);
        } finally {
            json.setLenient(lenient);
        }
    }
}
