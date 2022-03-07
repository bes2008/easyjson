package com.jn.easyjson.core.util;

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

    private static final List<String> MAP_ENTRY_DEFAULT_SETTERS_GETTERS = Collects.immutableArrayList("setKey","getKey","setValue","getValue");
    public static boolean hasOtherPropertiesForMapEntry(Class<? extends Map.Entry> clazz){
        Collection<Method> methods = Reflects.findGetterOrSetter(clazz, false);
        List<String> otherProperties= Pipeline.of(methods)
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

}
