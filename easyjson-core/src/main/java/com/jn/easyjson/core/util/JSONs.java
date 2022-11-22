package com.jn.easyjson.core.util;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.JsonArrayNode;
import com.jn.easyjson.core.node.JsonNodeNavigator;
import com.jn.easyjson.core.node.JsonObjectNode;
import com.jn.easyjson.core.node.JsonTreeNodes;
import com.jn.easyjson.core.tree.JsonParseException;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.reflect.Reflects;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JSONs extends JsonTreeNodes {
    private static JSON json = JSONBuilderProvider.create()
            .enableDecodeHex(true)
            .enableUnescapeEscapeCharacter(true)
            .build();
    private static JSON prettyJson = JSONBuilderProvider.create()
            .prettyFormat(true)
            .enableDecodeHex(true)
            .enableUnescapeEscapeCharacter(true)
            .build();

    public static JsonNodeNavigator JSON_NODE_NAVIGATOR = new JsonNodeNavigator();

    /**
     * 不保证一定定判断正确
     *
     * @param string
     * @return
     */
    public static final boolean isJsonArrayOrObject(String string) {
        return isJsonArray(string) || isJsonObject(string);
    }

    public static final boolean isJsonObject(String string) {
        if (Strings.isBlank(string)) {
            return false;
        }
        if (Strings.startsWith(string, "{") && Strings.endsWith(string, "}")) {
            return true;
        }
        return false;
    }

    public static final boolean isJsonArray(String string) {
        if (Strings.isBlank(string)) {
            return false;
        }
        if (Strings.startsWith(string, "[") && Strings.endsWith(string, "]")) {
            return true;
        }
        return false;
    }

    public static final boolean isJsonString(String string) {
        if (Strings.isBlank(string)) {
            return false;
        }
        if (Strings.startsWith(string, "\"") && Strings.endsWith(string, "\"")) {
            return true;
        }
        return false;
    }

    private static final List<String> MAP_ENTRY_DEFAULT_SETTERS_GETTERS = Collects.immutableArrayList("setKey", "getKey", "setValue", "getValue");

    public static boolean hasOtherPropertiesForMapEntry(Class<? extends Map.Entry> clazz) {
        Collection<Method> methods = Reflects.findGetterOrSetter(clazz, false);
        List<String> otherProperties = Pipeline.of(methods)
                .map(new Function<Method, String>() {
                    @Override
                    public String apply(Method method) {
                        return method.getName();
                    }
                })
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) {
                        return !MAP_ENTRY_DEFAULT_SETTERS_GETTERS.contains(s);
                    }
                })
                .asList();
        return Objs.isNotEmpty(otherProperties);
    }

    /**
     * @since 3.2.20
     */
    public static Map<String,Object> toMap(String jsonString) {
        return JSONs.<Map>toJavaObject(jsonString);
    }

    /**
     * @since 4.0.1
     */
    public static Map<String,Object> toMap(JsonObjectNode objectNode){
        return (Map)JSONs.toJavaObject(objectNode);
    }
    /**
     * @since 3.2.26
     */
    public static List toList(String jsonString) {
        return JSONs.<List>toJavaObject(jsonString);
    }

    /**
     * @since 4.0.1
     */
    public static List toList(JsonArrayNode arrayNode){
        return (List)JSONs.toJavaObject(arrayNode);
    }

    /**
     * @since 3.2.20
     */
    public static <T> T toJavaObject(String jsonString) {
        JsonTreeNode treeNode = json.fromJson(jsonString);
        return (T) JsonTreeNodes.toJavaObject(treeNode);
    }

    /**
     * @since 3.2.22
     */
    public static <T> T parse(String jsonString, Type t) {
        return json.fromJson(jsonString, t);
    }

    /**
     * @since 3.2.26
     */
    public static <T> T parse(InputStream jsonString, Type t) {
        return json.fromJson(new InputStreamReader(jsonString), t);
    }

    /**
     * @since 3.2.26
     */
    public static <T> T parse(Reader jsonString, Type t) {
        return json.fromJson(jsonString, t);
    }

    /**
     * @since 3.2.22
     */
    public static JsonTreeNode parse(String jsonString) {
        return json.fromJson(jsonString);
    }

    /**
     * @since 3.2.26
     */
    public static JsonTreeNode parse(Reader reader) {
        try {
            String jsonString = IOs.readAsString(reader);
            return json.fromJson(jsonString);
        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }

    /**
     * @since 3.2.26
     */
    public static JsonTreeNode parse(InputStream inputStream) {
        try {
            String jsonString = IOs.readAsString(inputStream);
            return json.fromJson(jsonString);
        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }

    /**
     * @since 3.2.22
     */
    public static JsonObjectNode parseObject(String jsonString) {
        return (JsonObjectNode) parse(jsonString);
    }

    /**
     * @since 3.2.26
     */
    public static JsonObjectNode parseObject(InputStream inputStream) {
        return (JsonObjectNode) parse(inputStream);
    }

    /**
     * @since 3.2.26
     */
    public static JsonObjectNode parseObject(Reader inputStream) {
        return (JsonObjectNode) parse(inputStream);
    }

    /**
     * @since 3.2.22
     */
    public static JsonArrayNode parseArray(String jsonString) {
        return (JsonArrayNode) parse(jsonString);
    }

    /**
     * @since 3.2.26
     */
    public static JsonArrayNode parseArray(InputStream inputStream) {
        return (JsonArrayNode) parse(inputStream);
    }

    /**
     * @since 3.2.26
     */
    public static JsonArrayNode parseArray(Reader inputStream) {
        return (JsonArrayNode) parse(inputStream);
    }

    /**
     * @since 3.2.23
     */
    public static String toJson(Object obj) {
        return json.toJson(obj);
    }

    /**
     * @since 3.2.27
     */
    public static String toJson(Object obj, boolean pretty) {
        return pretty ? prettyJson.toJson(obj) : toJson(obj);
    }

}
