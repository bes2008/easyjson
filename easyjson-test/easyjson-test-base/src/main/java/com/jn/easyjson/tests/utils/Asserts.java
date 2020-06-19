package com.jn.easyjson.tests.utils;

import java.util.List;
import com.gitee.qdbp.tools.utils.ConvertTools;
import com.jn.easyjson.core.JSONBuilderProvider;

/**
 * 测试结果比对工具
 *
 * @author zhaohuihua
 * @version 20200614
 */
public class Asserts {

    /** 比较两个JSON字符串是否一致 **/
    public static void assertJsonEquals(String actual, String expected) {
        Object aObject = parseJsonString(actual);
        Object eObject = parseJsonString(expected);
        assertDeepEquals(aObject, eObject);
    }

    private static Object parseJsonString(String jsonString) {
        jsonString = jsonString.trim();
        String dialect = JSONBuilderProvider.create().dialectIdentify().getId();
        if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
            if ("gson".equals(dialect)) {
                return ParseJsonUseGson.parseObjectString(jsonString);
            } else if ("jackson".equals(dialect)) {
                return ParseJsonUseJackson.parseObjectString(jsonString);
            } else if ("fastjson".equals(dialect)) {
                return ParseJsonUseFastjson.parseObjectString(jsonString);
            }
            throw new RuntimeException("Can't find any supported JSON libraries : [gson, jackson, fastjson]");
        }
        if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
            if ("gson".equals(dialect)) {
                return ParseJsonUseGson.parseArrayString(jsonString);
            } else if ("jackson".equals(dialect)) {
                return ParseJsonUseJackson.parseArrayString(jsonString);
            } else if ("fastjson".equals(dialect)) {
                return ParseJsonUseFastjson.parseArrayString(jsonString);
            }
            throw new RuntimeException("Can't find any supported JSON libraries : [gson, jackson, fastjson]");
        }
        throw new IllegalArgumentException("Unsupported json string: " + jsonString);
    }

    /** 比对两个对象是否一致 **/
    public static void assertEquals(Object actual, Object expected) {
        assertDeepEquals(actual, expected);
    }

    /** 比对两个对象是否深度一致 **/
    public static void assertDeepEquals(Object actual, Object expected) {
        String owner = "Root object";
        if (expected != null) {
            owner = expected.getClass().getSimpleName();
        } else if (actual != null) {
            owner = actual.getClass().getSimpleName();
        }
        DeepEqualsAssertion assertion = new DeepEqualsAssertion();
        assertion.assertDeepEquals(actual, expected, owner);
        List<String> errors = assertion.getErrors();
        if (!errors.isEmpty()) {
            String msg = "caught " + errors.size() + " exceptions!\n" + ConvertTools.joinToString(errors, "\n");
            throw new AssertionError(msg);
        }
    }
}
