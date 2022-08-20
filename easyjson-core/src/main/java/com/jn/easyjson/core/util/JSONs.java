package com.jn.easyjson.core.util;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.JsonArrayNode;
import com.jn.easyjson.core.node.JsonNodeNavigator;
import com.jn.easyjson.core.node.JsonObjectNode;
import com.jn.easyjson.core.node.JsonTreeNodes;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JSONs {
    private static JSON json = JSONBuilderProvider.simplest();
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
    public static Map toMap(String jsonString) {
        return JSONs.<Map>toJavaObject(jsonString);
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
    public static JsonObjectNode parseObject(String jsonString){
        return (JsonObjectNode) parse(jsonString);
    }

    /**
     * @since 3.2.22
     */
    public static JsonArrayNode parseArray(String jsonString){
        return (JsonArrayNode) parse(jsonString);
    }

    /**
     * @since 3.2.22
     */
    public static JsonTreeNode parse(String jsonString){
        return json.fromJson(jsonString);
    }


}
