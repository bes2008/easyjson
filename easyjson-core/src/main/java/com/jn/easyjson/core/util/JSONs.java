package com.jn.easyjson.core.util;

import com.jn.langx.util.Strings;

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

}
