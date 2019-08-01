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

package com.jn.easyjson.core.tree.bind;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.tree.JsonIOException;
import com.jn.easyjson.core.node.JsonNullNode;
import com.jn.easyjson.core.tree.stream.JsonReader;
import com.jn.easyjson.core.tree.stream.JsonToken;
import com.jn.easyjson.core.tree.stream.JsonWriter;

import java.io.*;

/**
 * Converts Java objects to and from JSON.
 * <p>
 * <h3>Defining a type's JSON form</h3>
 * By default JSON converts application classes to JSON using its built-in type
 * adapters. If JSON's default JSON conversion isn't appropriate for a type,
 * extend this class to customize the conversion. Here's an example of a type
 * adapter for an (X,Y) coordinate point: <pre>   {@code
 * <p>
 *   public class PointAdapter extends TypeAdapter<Point> {
 *     public Point read(JsonReader reader) throws IOException {
 *       if (reader.peek() == JsonToken.NULL) {
 *         reader.nextNull();
 *         return null;
 *       }
 *       String xy = reader.nextString();
 *       String[] parts = xy.split(",");
 *       int x = Integer.parseInt(parts[0]);
 *       int y = Integer.parseInt(parts[1]);
 *       return new Point(x, y);
 *     }
 *     public void write(JsonWriter writer, Point value) throws IOException {
 *       if (value == null) {
 *         writer.nullValue();
 *         return;
 *       }
 *       String xy = value.getX() + "," + value.getY();
 *       writer.value(xy);
 *     }
 *   }}</pre>
 * With this type adapter installed, JSON will convert {@code Points} to JSON as
 * strings like {@code "5,8"} rather than objects like {@code {"x":5,"y":8}}. In
 * this case the type adapter binds a rich Java class to a compact JSON value.
 * <p>
 * <p>The {@link #read(JsonReader) read()} method must read exactly one value
 * and {@link #write(JsonWriter, Object) write()} must write exactly one value.
 * For primitive types this is means readers should make exactly one call to
 * {@code nextBoolean()}, {@code nextDouble()}, {@code nextInt()}, {@code
 * nextLong()}, {@code nextString()} or {@code nextNull()}. Writers should make
 * exactly one call to one of <code>value()</code> or <code>nullValue()</code>.
 * For arrays, type adapters should start with a call to {@code beginArray()},
 * convert all elements, and finish with a call to {@code endArray()}. For
 * objects, they should start with {@code beginObject()}, convert the object,
 * and finish with {@code endObject()}. Failing to convert a value or converting
 * too many values may cause the application to crash.
 * <p>
 * <p>Type adapters should be prepared to read null from the stream and write it
 * to the stream. Alternatively, they should use {@link #nullSafe()} method while
 * registering the type adapter with JSON. If your {@code JSON} instance
 * has been configured to {@link JSONBuilder#serializeNulls()}, these nulls will be
 * written to the final document. Otherwise the value (and the corresponding name
 * when writing to a JSON object) will be omitted automatically. In either case
 * your type adapter must handle null.
 * <p>
 * <p>To use a custom type adapter with JSON, you must <i>register</i> it with a
 * {@link JSONBuilder}: <pre>   {@code
 * <p>
 *   JSONBuilder builder = new JSONBuilder();
 *   builder.registerTypeAdapter(Point.class, new PointAdapter());
 *   // if PointAdapter didn't check for nulls in its read/write methods, you should instead use
 *   // builder.registerTypeAdapter(Point.class, new PointAdapter().nullSafe());
 *   ...
 *   JSON JSON = builder.create();
 * }</pre>
 *
 * @since 2.1
 */
// non-Javadoc:
//
// <h3>JSON Conversion</h3>
// <p>A type adapter registered with JSON is automatically invoked while serializing
// or deserializing JSON. However, you can also use type adapters directly to serialize
// and deserialize JSON. Here is an example for deserialization: <pre>   {@code
//
//   String json = "{'origin':'0,0','points':['1,2','3,4']}";
//   TypeAdapter<Graph> graphAdapter = JSON.getAdapter(Graph.class);
//   Graph graph = graphAdapter.fromJson(json);
// }</pre>
// And an example for serialization: <pre>   {@code
//
//   Graph graph = new Graph(...);
//   TypeAdapter<Graph> graphAdapter = JSON.getAdapter(Graph.class);
//   String json = graphAdapter.toJson(graph);
// }</pre>
//
// <p>Type adapters are <strong>type-specific</strong>. For example, a {@code
// TypeAdapter<Date>} can convert {@code Date} instances to JSON and JSON to
// instances of {@code Date}, but cannot convert any other types.
//
public abstract class TypeAdapter<T> {

    /**
     * Writes one JSON value (an array, object, string, number, boolean or null)
     * for {@code value}.
     *
     * @param value the Java object to write. May be null.
     */
    public abstract void write(JsonWriter out, T value) throws IOException;

    /**
     * Converts {@code value} to a JSON document and writes it to {@code out}.
     * Unlike JSON's similar toJson}
     * method, this write is strict. Create a {@link
     * JsonWriter#setLenient(boolean) lenient} {@code JsonWriter} and call
     * {@link #write(JsonWriter, Object)} for lenient
     * writing.
     *
     * @param value the Java object to convert. May be null.
     * @since 2.2
     */
    public final void toJson(Writer out, T value) throws IOException {
        JsonWriter writer = new JsonWriter(out);
        write(writer, value);
    }

    /**
     * This wrapper method is used to make a type adapter null tolerant. In general, a
     * type adapter is required to handle nulls in write and read methods. Here is how this
     * is typically done:<br>
     * <pre>   {@code
     *
     * JSON JSON = new JSONBuilder().registerTypeAdapter(Foo.class,
     *   new TypeAdapter<Foo>() {
     *     public Foo read(JsonReader in) throws IOException {
     *       if (in.peek() == JsonToken.NULL) {
     *         in.nextNull();
     *         return null;
     *       }
     *       // read a Foo from in and return it
     *     }
     *     public void write(JsonWriter out, Foo src) throws IOException {
     *       if (src == null) {
     *         out.nullValue();
     *         return;
     *       }
     *       // write src as JSON to out
     *     }
     *   }).create();
     * }</pre>
     * You can avoid this boilerplate handling of nulls by wrapping your type adapter with
     * this method. Here is how we will rewrite the above example:
     * <pre>   {@code
     *
     * JSON JSON = new JSONBuilder().registerTypeAdapter(Foo.class,
     *   new TypeAdapter<Foo>() {
     *     public Foo read(JsonReader in) throws IOException {
     *       // read a Foo from in and return it
     *     }
     *     public void write(JsonWriter out, Foo src) throws IOException {
     *       // write src as JSON to out
     *     }
     *   }.nullSafe()).create();
     * }</pre>
     * Note that we didn't need to check for nulls in our type adapter after we used nullSafe.
     */
    public final TypeAdapter<T> nullSafe() {
        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    TypeAdapter.this.write(out, value);
                }
            }

            @Override
            public T read(JsonReader reader) throws IOException {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return null;
                }
                return TypeAdapter.this.read(reader);
            }
        };
    }

    /**
     * Converts {@code value} to a JSON document. Unlike JSON's similar {@link
     * JSON#toJson(Object) toJson} method, this write is strict. Create a {@link
     * JsonWriter#setLenient(boolean) lenient} {@code JsonWriter} and call
     * {@link #write(JsonWriter, Object)} for lenient
     * writing.
     *
     * @param value the Java object to convert. May be null.
     * @since 2.2
     */
    public final String toJson(T value) {
        StringWriter stringWriter = new StringWriter();
        try {
            toJson(stringWriter, value);
        } catch (IOException e) {
            throw new AssertionError(e); // No I/O writing to a StringWriter.
        }
        return stringWriter.toString();
    }

    /**
     * Converts {@code value} to a JSON tree.
     *
     * @param value the Java object to convert. May be null.
     * @return the converted JSON tree. May be {@link JsonNullNode}.
     * @since 2.2
     */
    public final JsonTreeNode toJsonTree(T value) {
        try {
            JsonTreeWriter jsonWriter = new JsonTreeWriter();
            write(jsonWriter, value);
            return jsonWriter.get();
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }

    /**
     * Reads one JSON value (an array, object, string, number, boolean or null)
     * and converts it to a Java object. Returns the converted object.
     *
     * @return the converted Java object. May be null.
     */
    public abstract T read(JsonReader in) throws IOException;

    /**
     * Converts the JSON document in {@code in} to a Java object. Unlike JSON's
     * similar  fromJson} method, this
     * read is strict. Create a {@link JsonReader#setLenient(boolean) lenient}
     * {@code JsonReader} and call {@link #read(JsonReader)} for lenient reading.
     *
     * @return the converted Java object. May be null.
     * @since 2.2
     */
    public final T fromJson(Reader in) throws IOException {
        JsonReader reader = new JsonReader(in);
        return read(reader);
    }

    /**
     * Converts the JSON document in {@code json} to a Java object. Unlike JSON's
     * similar {@link JSON#fromJson(String, Class) fromJson} method, this read is
     * strict. Create a {@link JsonReader#setLenient(boolean) lenient} {@code
     * JsonReader} and call {@link #read(JsonReader)} for lenient reading.
     *
     * @return the converted Java object. May be null.
     * @since 2.2
     */
    public final T fromJson(String json) throws IOException {
        return fromJson(new StringReader(json));
    }

    /**
     * Converts {@code jsonTree} to a Java object.
     *
     * @param jsonTree the Java object to convert. May be {@link JsonNullNode}.
     * @since 2.2
     */
    public final T fromJsonTree(JsonTreeNode jsonTree) {
        try {
            JsonReader jsonReader = new JsonTreeReader(jsonTree);
            return read(jsonReader);
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }
}
