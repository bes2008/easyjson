package com.github.fangjinuo.easyjson.api.tree;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import com.github.fangjinuo.easyjson.api.tree.stream.JsonReader;
import com.github.fangjinuo.easyjson.api.tree.stream.JsonToken;
import com.github.fangjinuo.easyjson.api.tree.stream.MalformedJsonException;
import com.github.fangjinuo.easyjson.api.JsonTreeNode;

/**
 * A parser to parse Json into a parse tree of {@link JsonTreeNode}s
 *
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
     *     text is not valid JSON
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
