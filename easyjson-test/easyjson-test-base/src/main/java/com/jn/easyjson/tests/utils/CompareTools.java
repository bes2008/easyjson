package com.jn.easyjson.tests.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.Assert;
import com.jn.langx.util.reflect.Reflects;

/**
 * 测试结果比对工具
 *
 * @author zhaohuihua
 * @version 20200614
 */
public class CompareTools {

    /** 比较两个JSON字符串是否一致 **/
    public static void assertJsonEquals(String actual, String expected) {
        Object aObject = parseJsonString(actual);
        Object eObject = parseJsonString(expected);
        assertDeepEquals(aObject, eObject);
    }

    private static Object parseJsonString(String jsonString) {
        if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
            try {
                Class.forName("com.google.gson.Gson");
                return ParseJsonUseGson.parseObjectString(jsonString);
            } catch (ClassNotFoundException e) {
            }
            try {
                Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
                return ParseJsonUseGson.parseObjectString(jsonString);
            } catch (ClassNotFoundException e) {
            }
            try {
                Class.forName("com.alibaba.fastjson.JSON");
                return ParseJsonUseFastjson.parseObjectString(jsonString);
            } catch (ClassNotFoundException e) {
            }
            throw new RuntimeException("Can't find any supported JSON libraries : [gson, jackson, fastjson]");
        }
        if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
            try {
                Class.forName("com.google.gson.Gson");
                return ParseJsonUseGson.parseArrayString(jsonString);
            } catch (ClassNotFoundException e) {
            }
            try {
                Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
                return ParseJsonUseGson.parseArrayString(jsonString);
            } catch (ClassNotFoundException e) {
            }
            try {
                Class.forName("com.alibaba.fastjson.JSON");
                return ParseJsonUseFastjson.parseArrayString(jsonString);
            } catch (ClassNotFoundException e) {
            }
            throw new RuntimeException("Can't find any supported JSON libraries : [gson, jackson, fastjson]");
        }
        throw new IllegalArgumentException("Unsupported json string: " + jsonString);
    }

    /** 比对两个对象是否深度一致 **/
    public static void assertDeepEquals(Object actual, Object expected) {
        assertDeepEquals(null, actual, expected);
    }

    private static void assertDeepEquals(String owner, Object actual, Object expected) {
        String desc = (owner == null ? "" : owner);
        // 期望对象为空值
        if (expected == null) {
            System.out.println("expected " + desc + " = " + expected);
            System.out.println("actual   " + desc + " = " + actual);
            Assert.assertNull(actual, desc);
            return;
        } else if (actual == null) {
            System.out.println("expected " + desc + " = " + expected);
            System.out.println("actual   " + desc + " = " + actual);
            Assert.assertNotNull(actual, desc);
            return;
        }
        // 先比对class是否相等
        Assert.assertEquals(actual.getClass(), expected.getClass(), desc + " class");
        Class<?> clazz = expected.getClass();
        if (isPrimitive(clazz)) { // 基本类型
            System.out.println("expected " + desc + " = " + expected);
            System.out.println("actual   " + desc + " = " + actual);
            Assert.assertEquals(actual, expected, desc + " value");
        } else if (Map.class.isAssignableFrom(clazz)) { // MAP类
            Map<?, ?> aMap = (Map<?, ?>) actual;
            Map<?, ?> eMap = (Map<?, ?>) expected;
            assertMapValueEquals(owner == null ? clazz.getSimpleName() : owner, aMap, eMap);
        } else if (clazz.isArray()) { // 数组类
            List<Object> aList = Arrays.asList((Object[]) actual);
            List<Object> eList = Arrays.asList((Object[]) expected);
            assertListValueEquals(owner == null ? clazz.getSimpleName() : owner, aList, eList);
        } else if (Collection.class.isAssignableFrom(clazz)) { // 集合类
            List<Object> aList = new ArrayList<>();
            aList.addAll((Collection<?>) actual);
            List<Object> eList = new ArrayList<>();
            eList.addAll((Collection<?>) expected);
            assertListValueEquals(owner == null ? clazz.getSimpleName() : owner, aList, eList);
        } else if (Iterable.class.isAssignableFrom(clazz)) { // 迭代类
            List<Object> aList = iterableToList((Iterable<?>) actual);
            List<Object> eList = iterableToList((Iterable<?>) expected);
            assertListValueEquals(owner == null ? clazz.getSimpleName() : owner, aList, eList);
        } else { // 其他类型, 比较字段值
            assertFieldValueEquals(owner == null ? clazz.getSimpleName() : owner, actual, expected);
        }
    }

    private static List<Object> iterableToList(Iterable<?> iterable) {
        List<Object> list = new ArrayList<>();
        Iterator<?> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    private static void assertListValueEquals(String owner, List<?> aList, List<?> eList) {
        // 空集合
        if (aList.isEmpty() && eList.isEmpty()) {
            System.out.println("expected " + owner + " = " + "[]");
            System.out.println("actual   " + owner + " = " + "[]");
            return;
        }
        Assert.assertEquals(aList.size(), eList.size(), owner + " size");

        for (int i = 0; i < aList.size(); i++) {
            Object aItem = aList.get(i);
            Object eItem = eList.get(i);
            assertDeepEquals(owner + '[' + i + ']', aItem, eItem);
        }
    }

    private static void assertMapValueEquals(String owner, Map<?, ?> aMap, Map<?, ?> eMap) {
        // 空Map
        if (aMap.isEmpty() && eMap.isEmpty()) {
            System.out.println("expected " + owner + " = " + "{}");
            System.out.println("actual   " + owner + " = " + "{}");
            return;
        }
        Assert.assertEquals(aMap.size(), eMap.size(), owner + " size");

        Set<Object> keys = new LinkedHashSet<>();
        keys.addAll(aMap.keySet());
        keys.addAll(eMap.keySet());
        for (Object key : keys) {
            String desc = owner + '.' + key;
            Assert.assertEquals(aMap.containsKey(key), eMap.containsKey(key), desc + " contains");
            Object aValue = aMap.get(key);
            Object eValue = eMap.get(key);
            assertDeepEquals(desc, aValue, eValue);
        }
    }

    private static void assertFieldValueEquals(String owner, Object actual, Object expected) {
        // 遍历所有字段
        Class<?> clazz = expected.getClass();
        Collection<Field> fields = Reflects.getAllDeclaredFields(clazz);
        for (Field field : fields) {
            field.setAccessible(true);
            // 获取actual的字段值
            Object aValue = null;
            Exception aException = null;
            try {
                aValue = field.get(actual);
            } catch (Exception e) {
                aException = e;
            }
            // 获取expected的字段值
            Object eValue = null;
            Exception eException = null;
            try {
                eValue = field.get(expected);
            } catch (Exception e) {
                eException = e;
            }
            String desc = owner + "." + field.getName();
            // 如果发生异常了, 异常必须相等, 否则字段值必须相等
            if (aException != null || eException != null) {
                System.out.println("expected " + desc + " = " + eException.toString());
                System.out.println("actual   " + desc + " = " + aException.toString());
                Assert.assertEquals(aException.getClass(), eException.getClass(), "Get " + desc + " exception");
                Assert.assertEquals(aException.getMessage(), eException.getMessage(), "Get " + desc + " exception");
                continue;
            }
            assertDeepEquals(desc, aValue, eValue);
        }
    }

    private static boolean isPrimitive(Class<?> clazz) {
        // @formatter:off
        return clazz.isPrimitive() || clazz.isEnum() || clazz == Boolean.class || clazz == Character.class
                || clazz == String.class || Number.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz);
        // @formatter:on
    }
}
