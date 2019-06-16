package com.github.fangjinuo.easyjson.api.tree.stream;


/**
 * A structure, name or value type in a JSON-encoded string.
 *
 * @since 1.6
 */
public enum JsonToken {

    /**
     * The opening of a JSON array. Written using {@link JsonWriter#beginArray}
     * and read using {@link JsonReader#beginArray}.
     */
    BEGIN_ARRAY,

    /**
     * The closing of a JSON array. Written using {@link JsonWriter#endArray}
     * and read using {@link JsonReader#endArray}.
     */
    END_ARRAY,

    /**
     * The opening of a JSON object. Written using {@link JsonWriter#beginObject}
     * and read using {@link JsonReader#beginObject}.
     */
    BEGIN_OBJECT,

    /**
     * The closing of a JSON object. Written using {@link JsonWriter#endObject}
     * and read using {@link JsonReader#endObject}.
     */
    END_OBJECT,

    /**
     * A JSON property name. Within objects, tokens alternate between names and
     * their values. Written using {@link JsonWriter#name} and read using {@link
     * JsonReader#nextName}
     */
    NAME,

    /**
     * A JSON string.
     */
    STRING,

    /**
     * A JSON number represented in this API by a Java {@code double}, {@code
     * long}, or {@code int}.
     */
    NUMBER,

    /**
     * A JSON {@code true} or {@code false}.
     */
    BOOLEAN,

    /**
     * A JSON {@code null}.
     */
    NULL,

    /**
     * The end of the JSON stream. This sentinel value is returned by {@link
     * JsonReader#peek()} to signal that the JSON-encoded value has no more
     * tokens.
     */
    END_DOCUMENT
}
