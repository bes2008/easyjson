package com.jn.easyjson.tests.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import com.jn.langx.util.reflect.Reflects;

/**
 * 测试结果比对工具
 *
 * @author zhaohuihua
 * @version 20200614
 */
public class CompareTools {

    /** 比对两个对象的字段值是否一致 **/
    public static void assertDeepEquals(Object actual, Object expected) {
        assertDeepEquals(null, actual, expected);
    }

    private static void assertDeepEquals(String owner, Object actual, Object expected) {
        String desc = (owner == null ? "" : owner);
        // 都为空
        if (actual == null && expected == null) {
            System.out.println("actual   " + desc + " = " + actual);
            System.out.println("expected " + desc + " = " + expected);
            return;
        }
        // 先比对class是否相等
        Assert.assertEquals(actual.getClass(), expected.getClass(), "class");
        Class<?> clazz = actual.getClass();
        if (isPrimitive(clazz)) { // 基本类型
            System.out.println("actual   " + desc + " = " + actual);
            System.out.println("expected " + desc + " = " + expected);
            Assert.assertEquals(actual, expected, desc + " value");
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
        } else if (Map.class.isAssignableFrom(clazz)) { // MAP类
            // TODO
        } else { // 其他类型, 比较字段值
            assertFieldValueEquals(owner == null ? clazz.getSimpleName() : owner, actual, expected);
        }
    }

    private static void assertListValueEquals(String owner, List<?> aList, List<?> eList) {
        // 空集合
        if (aList.isEmpty() && eList.isEmpty()) {
            System.out.println("actual   " + owner + " = " + "[]");
            System.out.println("expected " + owner + " = " + "[]");
            return;
        }
        Assert.assertEquals(aList.size(), eList.size(), owner + " size");

        for (int i = 0; i < aList.size(); i++) {
            Object aItem = aList.get(i);
            Object eItem = eList.get(i);
            assertDeepEquals(owner + '[' + i + ']', aItem, eItem);
        }
    }
    
    private static void assertFieldValueEquals(String owner, Object actual, Object expected) {
        // 遍历所有字段
        Class<?> clazz = actual.getClass();
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
                System.out.println("actual   " + desc + " = " + aException.toString());
                System.out.println("expected " + desc + " = " + eException.toString());
                Assert.assertEquals(aException.getClass(), eException.getClass(), "Get " + desc + " exception");
                Assert.assertEquals(aException.getMessage(), eException.getMessage(), "Get " + desc + " exception");
                continue;
            }
            assertDeepEquals(desc, aValue, eValue);
        }
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
