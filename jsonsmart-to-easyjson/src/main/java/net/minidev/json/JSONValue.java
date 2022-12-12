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

package net.minidev.json;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.JsonArrayNode;
import com.jn.easyjson.core.node.JsonObjectNode;
import com.jn.easyjson.core.util.JSONs;
import net.minidev.json.parser.ParseException;
import net.minidev.json.reader.JsonWriterI;
import net.minidev.json.writer.JsonReaderI;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 * JSONValue is the helper class In most of case you should use those static
 * methode to user JSON-smart
 * <p>
 * <p>
 * The most commonly use methode are {@link #parse(String)}
 * {@link #toJSONString(Object)}
 *
 * @author Uriel Chemouni &lt;uchemouni@gmail.com&gt;
 */
public class JSONValue {
    /**
     * Global default compression type
     */
    public static JSONStyle COMPRESSION = JSONStyle.NO_COMPRESS;


    private static String readToString(InputStream in) {
        if (in == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = new byte[1024];
        int length = 0;
        try {
            while ((length = in.read(bytes)) != -1) {
                stringBuilder.append(new String(bytes, 0, length));
            }
        } catch (Throwable ex) {
            // ignore it
        }
        if (stringBuilder.length() > 0) {
            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    private static String readToString(byte[] in) {
        if (in == null || in.length == 0) {
            return null;
        }
        return new String(in, 0, in.length);
    }

    private static String readToString(Reader in) {
        if (in == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = new char[1024];
        int length = 0;
        try {
            while ((length = in.read(chars)) != -1) {
                stringBuilder.append(new String(chars, 0, length));
            }
        } catch (Throwable ex) {
            // ignore it
        }
        if (stringBuilder.length() > 0) {
            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    private static Object parseObject(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        JsonTreeNode treeNode = JSONs.parse(str);
        return JsonMapper.fromJsonTreeNode(treeNode);
    }

    private static <T> T parseInstance(String str, Class<T> mapTo) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        return JSONs.parse(str, mapTo);
    }

    private static <T> T parseForUpdate(String string, T toUpdate) {
        JSON json = JSONBuilderProvider.create().build();
        String str0 = json.toJson(toUpdate);
        JsonTreeNode treeNode0 = json.fromJson(str0);

        String str1 = json.toJson(string);
        JsonTreeNode treeNode1 = json.fromJson(str1);

        if (treeNode1.isJsonNullNode()) {
            return null;
        }

        if (treeNode1.isJsonPrimitiveNode()) {
            return (T) treeNode1.getAsJsonPrimitiveNode().getValue();
        }

        String mergedString = str0;
        if (treeNode1.isJsonArrayNode()) {
            JsonArrayNode arrayNode0 = treeNode0.getAsJsonArrayNode();
            JsonArrayNode arrayNode1 = treeNode1.getAsJsonArrayNode();
            for (JsonTreeNode jsonTreeNode : arrayNode1) {
                arrayNode0.add(jsonTreeNode);
            }
            mergedString = json.toJson(arrayNode0);
        }

        if (treeNode1.isJsonObjectNode()) {
            JsonObjectNode objectNode0 = treeNode0.getAsJsonObjectNode();
            JsonObjectNode objectNode1 = treeNode1.getAsJsonObjectNode();
            for (Map.Entry<String, JsonTreeNode> entry : objectNode1.propertySet()) {
                objectNode0.addProperty(entry.getKey(), entry.getValue());
            }
            mergedString = json.toJson(objectNode0);
        }

        return (T) json.toJson(mergedString, toUpdate.getClass());
    }

    /**
     * Parse JSON text into java object from the input source. Please use
     * parseWithException() if you don't want to ignore the exception. if you
     * want strict input check use parseStrict()
     *
     * @return Instance of the following: JSONObject, JSONArray, String,
     * java.lang.Number, java.lang.Boolean, null
     * @see #parseWithException(Reader)
     */
    public static Object parse(InputStream in) {
        return parseObject(readToString(in));
    }


    /**
     * Parse JSON text into java object from the input source. Please use
     * parseWithException() if you don't want to ignore the exception. if you
     * want strict input check use parseStrict()
     *
     * @return Instance of the following: JSONObject, JSONArray, String,
     * java.lang.Number, java.lang.Boolean, null
     * @see #parseWithException(Reader)
     */
    public static Object parse(byte[] in) {
        return parseObject(readToString(in));
    }

    /**
     * Parse JSON text into java object from the input source. Please use
     * parseWithException() if you don't want to ignore the exception. if you
     * want strict input check use parseStrict()
     *
     * @return Instance of the following: JSONObject, JSONArray, String,
     * java.lang.Number, java.lang.Boolean, null
     * @see #parseWithException(Reader)
     */
    public static Object parse(Reader in) {
        return parseObject(readToString(in));
    }

    /**
     * Parse input json as a mapTo class
     * <p>
     * mapTo can be a bean
     *
     * @since 2.0
     */
    public static <T> T parse(InputStream in, Class<T> mapTo) {
        return parseInstance(readToString(in), mapTo);
    }


    /**
     * Parse input json as a mapTo class
     * <p>
     * mapTo can be a bean
     *
     * @since 2.0
     */
    public static <T> T parse(byte[] in, Class<T> mapTo) {
        return parseInstance(readToString(in), mapTo);
    }

    /**
     * Parse input json as a mapTo class
     * <p>
     * mapTo can be a bean
     *
     * @since 2.0
     */
    public static <T> T parse(Reader in, Class<T> mapTo) {
        return parseInstance(readToString(in), mapTo);
    }

    /**
     * Parse input json as a mapTo class
     * <p>
     * mapTo can be a bean
     *
     * @since 2.0
     */


    public static <T> T parse(Reader in, T toUpdate) {
        return parseForUpdate(readToString(in), toUpdate);
    }

    /**
     * Parse input json as a mapTo class
     *
     * @since 2.0
     */
    protected static Object parse(Reader in, JsonReaderI mapper) {
        return parseObject(readToString(in));
    }

    /**
     * Parse input json as a mapTo class
     * <p>
     * mapTo can be a bean
     *
     * @since 2.0
     */
    public static <T> T parse(String in, Class<T> mapTo) {
        return parseInstance(in, mapTo);
    }

    /**
     * Parse input json as a mapTo class
     * <p>
     * mapTo can be a bean
     *
     * @since 2.0
     */
    public static <T> T parse(InputStream in, T toUpdate) {
        return parseForUpdate(readToString(in), toUpdate);
    }

    /**
     * Parse input json as a mapTo class
     * <p>
     * mapTo can be a bean
     *
     * @since 2.0
     */
    public static <T> T parse(String in, T toUpdate) {
        return parseForUpdate(in, toUpdate);
    }

    /**
     * Parse input json as a mapTo class
     *
     * @since 2.0
     */
    protected static <T> T parse(byte[] in, JsonReaderI<T> mapper) {
        return parse(readToString(in), mapper);
    }

    /**
     * Parse input json as a mapTo class
     *
     * @since 2.0
     */
    protected static <T> T parse(String in, JsonReaderI<T> mapper) {
        return (T) parseObject(in);
    }

    /**
     * Parse JSON text into java object from the input source. Please use
     * parseWithException() if you don't want to ignore the exception. if you
     * want strict input check use parseStrict()
     *
     * @return Instance of the following: JSONObject, JSONArray, String,
     * java.lang.Number, java.lang.Boolean, null
     * @see #parseWithException(String)
     */
    public static Object parse(String s) {
        return parseObject(s);
    }

    /**
     * Parse Json input to a java Object keeping element order
     *
     * @since 1.0.6.1
     */
    public static Object parseKeepingOrder(Reader in) {
        return parseObject(readToString(in));
    }

    /**
     * Parse Json input to a java Object keeping element order
     *
     * @since 1.0.6.1
     */
    public static Object parseKeepingOrder(String in) {
        return parseObject(in);
    }


    public static String compress(String input, JSONStyle style) {
        JSON json = JSONBuilderProvider.create().serializeNulls(!style.ignoreNull()).prettyFormat(style.indent()).build();
        return json.toJson(json.fromJson(input));
    }

    /**
     * Compress Json input keeping element order
     *
     * @since 1.0.6.1
     * <p>
     * need to be rewrite in 2.0
     */
    public static String compress(String input) {
        return compress(input, JSONStyle.MAX_COMPRESS);
    }

    /**
     * Compress Json input keeping element order
     *
     * @since 1.0.6.1
     */
    public static String uncompress(String input) {
        return compress(input, JSONStyle.NO_COMPRESS);
    }

    /**
     * Parse JSON text into java object from the input source.
     *
     * @return Instance of the following: JSONObject, JSONArray, String,
     * java.lang.Number, java.lang.Boolean, null
     */
    public static Object parseWithException(byte[] in) throws IOException, ParseException {
        return parseObject(readToString(in));
    }

    /**
     * Parse JSON text into java object from the input source.
     *
     * @return Instance of the following: JSONObject, JSONArray, String,
     * java.lang.Number, java.lang.Boolean, null
     */
    public static Object parseWithException(InputStream in) throws IOException, ParseException {
        return parseObject(readToString(in));
    }

    /**
     * Parse JSON text into java object from the input source.
     *
     * @return Instance of the following: JSONObject, JSONArray, String,
     * java.lang.Number, java.lang.Boolean, null
     */
    public static Object parseWithException(Reader in) throws IOException, ParseException {
        return parseObject(readToString(in));
    }

    /**
     * Parse JSON text into java object from the input source.
     *
     * @return Instance of the following: JSONObject, JSONArray, String,
     * java.lang.Number, java.lang.Boolean, null
     */
    public static Object parseWithException(String s) throws ParseException {
        return parseObject(s);
    }

    /**
     * Parse input json as a mapTo class
     * <p>
     * mapTo can be a bean
     *
     * @since 2.0
     */
    public static <T> T parseWithException(String in, Class<T> mapTo) throws ParseException {
        return parseInstance(in, mapTo);
    }

    /**
     * Parse valid RFC4627 JSON text into java object from the input source.
     *
     * @return Instance of the following: JSONObject, JSONArray, String,
     * java.lang.Number, java.lang.Boolean, null
     */
    public static Object parseStrict(Reader in) throws IOException, ParseException {
        return parseObject(readToString(in));
    }

    /**
     * Parse valid RFC4627 JSON text into java object from the input source.
     *
     * @return Instance of the following: JSONObject, JSONArray, String,
     * java.lang.Number, java.lang.Boolean, null
     */
    public static Object parseStrict(String s) throws ParseException {
        return parseObject(s);
    }

    /**
     * Check RFC4627 Json Syntax from input Reader
     *
     * @return if the input is valid
     */
    public static boolean isValidJsonStrict(Reader in) throws IOException {
        return isValidJsonStrict(readToString(in));
    }

    /**
     * check RFC4627 Json Syntax from input String
     *
     * @return if the input is valid
     */
    public static boolean isValidJsonStrict(String s) {
        return isValidJson(s);
    }

    /**
     * Check Json Syntax from input Reader
     *
     * @return if the input is valid
     */
    public static boolean isValidJson(Reader in) throws IOException {
        return isValidJson(readToString(in));
    }

    /**
     * Check Json Syntax from input String
     *
     * @return if the input is valid
     */
    public static boolean isValidJson(String s) {
        try {
            return parse(s) != null;
        } catch (Throwable ex) {
            return false;
        }
    }

    /**
     * Encode an object into JSON text and write it to out.
     * <p>
     * If this object is a Map or a List, and it's also a JSONStreamAware or a
     * JSONAware, JSONStreamAware or JSONAware will be considered firstly.
     * <p>
     *
     * @see JSONObject#writeJSON(Map, Appendable)
     * @see JSONArray#writeJSONString(List, Appendable)
     */
    public static void writeJSONString(Object value, Appendable out) throws IOException {
        writeJSONString(value, out, COMPRESSION);
    }


    /**
     * remap field from java to json.
     *
     * @since 2.1.1
     */
    public static <T> void remapField(Class<T> type, String jsonFieldName, String javaFieldName) {
    }

    /**
     * Register a serializer for a class.
     */
    public static <T> void registerWriter(Class<?> cls, JsonWriterI<T> writer) {
    }

    /**
     * register a deserializer for a class.
     */
    public static <T> void registerReader(Class<T> type, JsonReaderI<T> mapper) {
    }

    /**
     * Encode an object into JSON text and write it to out.
     * <p>
     * If this object is a Map or a List, and it's also a JSONStreamAware or a
     * JSONAware, JSONStreamAware or JSONAware will be considered firstly.
     * <p>
     *
     * @see JSONObject#writeJSON(Map, Appendable)
     * @see JSONArray#writeJSONString(List, Appendable)
     */
    @SuppressWarnings("unchecked")
    public static void writeJSONString(Object value, Appendable out, JSONStyle compression) throws IOException {
        if (value == null) {
            out.append("null");
            return;
        }
        JSON json = JSONBuilderProvider.create().serializeNulls(!compression.ignoreNull()).prettyFormat(compression.indent()).build();
        out.append(json.toJson(value));
    }

    /**
     * Encode an object into JSON text and write it to out.
     * <p>
     * If this object is a Map or a List, and it's also a JSONStreamAware or a
     * JSONAware, JSONStreamAware or JSONAware will be considered firstly.
     * <p>
     *
     * @see JSONObject#writeJSON(Map, Appendable)
     * @see JSONArray#writeJSONString(List, Appendable)
     */
    public static String toJSONString(Object value) {
        return toJSONString(value, COMPRESSION);
    }

    /**
     * Convert an object to JSON text.
     * <p>
     * If this object is a Map or a List, and it's also a JSONAware, JSONAware
     * will be considered firstly.
     * <p>
     * DO NOT call this method from toJSONString() of a class that implements
     * both JSONAware and Map or List with "this" as the parameter, use
     * JSONObject.toJSONString(Map) or JSONArray.toJSONString(List) instead.
     *
     * @return JSON text, or "null" if value is null or it's an NaN or an INF
     * number.
     * @see JSONObject#toJSONString(Map)
     * @see JSONArray#toJSONString(List)
     */
    public static String toJSONString(Object value, JSONStyle compression) {
        StringBuilder sb = new StringBuilder();
        try {
            writeJSONString(value, sb, compression);
        } catch (IOException e) {
            // can not append on a StringBuilder
        }
        return sb.toString();
    }

    public static String escape(String s) {
        return escape(s, COMPRESSION);
    }

    /**
     * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters
     * (U+0000 through U+001F).
     */
    public static String escape(String s, JSONStyle compression) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        compression.escape(s, sb);
        return sb.toString();
    }

    public static void escape(String s, Appendable ap) {
        escape(s, ap, COMPRESSION);
    }

    public static void escape(String s, Appendable ap, JSONStyle compression) {
        if (s == null) {
            return;
        }
        compression.escape(s, ap);
    }
}
