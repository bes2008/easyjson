package com.jn.easyjson.tests.utils;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.testng.Assert;
import com.jn.easyjson.core.JSONBuilderProvider;
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
        Class<?> eClass = expected.getClass();
        Class<?> aClass = actual.getClass();
        if (isIntegralNumber(eClass) && isIntegralNumber(aClass)) { // 整形的数字
            System.out.println("expected " + desc + " = " + expected);
            System.out.println("actual   " + desc + " = " + actual);
            Assert.assertEquals(((Number) actual).longValue(), ((Number) expected).longValue(), desc + " value");
        } else if (Number.class.isAssignableFrom(eClass) && Number.class.isAssignableFrom(aClass)) { // 数字类
            System.out.println("expected " + desc + " = " + expected);
            System.out.println("actual   " + desc + " = " + actual);
            Assert.assertEquals(((Number) actual).doubleValue(), ((Number) expected).doubleValue(), desc + " value");
        } else if (isPrimitive(eClass) && isPrimitive(aClass)) { // 基本类型
            System.out.println("expected " + desc + " = " + expected);
            System.out.println("actual   " + desc + " = " + actual);
            Assert.assertEquals(actual, expected, desc + " value");
        } else if (Map.class.isAssignableFrom(eClass) && Map.class.isAssignableFrom(aClass)) { // MAP类
            Map<?, ?> aMap = (Map<?, ?>) actual;
            Map<?, ?> eMap = (Map<?, ?>) expected;
            assertMapValueEquals(owner == null ? eClass.getSimpleName() : owner, aMap, eMap);
        } else if (eClass.isArray() && aClass.isArray()) { // 数组类
            // 先比对class是否相等
            Class<?> aType = aClass.getComponentType();
            Class<?> eType = eClass.getComponentType();
            if (!aType.isAssignableFrom(eType) && !eType.isAssignableFrom(aType)) {
                Assert.assertEquals(aClass, eClass, desc + " class");
            }
            List<Object> aList = Arrays.asList((Object[]) actual);
            List<Object> eList = Arrays.asList((Object[]) expected);
            assertListValueEquals(owner == null ? eClass.getSimpleName() : owner, aList, eList);
        } else if (Collection.class.isAssignableFrom(eClass) && Collection.class.isAssignableFrom(aClass)) { // 集合类
            List<Object> aList = new ArrayList<>();
            aList.addAll((Collection<?>) actual);
            List<Object> eList = new ArrayList<>();
            eList.addAll((Collection<?>) expected);
            assertListValueEquals(owner == null ? eClass.getSimpleName() : owner, aList, eList);
        } else if (Iterable.class.isAssignableFrom(eClass) && Iterable.class.isAssignableFrom(aClass)) { // 迭代类
            List<Object> aList = iterableToList((Iterable<?>) actual);
            List<Object> eList = iterableToList((Iterable<?>) expected);
            assertListValueEquals(owner == null ? eClass.getSimpleName() : owner, aList, eList);
        } else { // 其他类型, 比较字段值
            // 先比对class是否相等
            if (!aClass.isAssignableFrom(eClass) && !eClass.isAssignableFrom(aClass)) {
                Assert.assertEquals(aClass, eClass, desc + " class");
            }
            assertFieldValueEquals(owner == null ? eClass.getSimpleName() : owner, actual, expected);
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

    /** 是不是整形的数字 **/
    private static boolean isIntegralNumber(Class<?> clazz) {
        // @formatter:off
        return clazz == byte.class || clazz == Byte.class
                || clazz == short.class || clazz == Short.class
                || clazz == int.class || clazz == Integer.class
                || clazz == long.class || clazz == Long.class
                || BigInteger.class.isAssignableFrom(clazz)
                || AtomicLong.class.isAssignableFrom(clazz)
                || AtomicInteger.class.isAssignableFrom(clazz);
        // @formatter:on
    }

    private static boolean isPrimitive(Class<?> clazz) {
        // @formatter:off
        return clazz.isPrimitive()
                || clazz.isEnum()
                || clazz == Boolean.class
                || clazz == Character.class
                || clazz == String.class
                || Number.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz);
        // @formatter:on
    }
}
