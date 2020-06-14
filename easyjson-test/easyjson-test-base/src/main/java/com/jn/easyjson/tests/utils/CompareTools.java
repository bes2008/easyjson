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
    public static void assertFieldValueEquals(Object actual, Object expected) {
        assertFieldValueEquals(null, actual, expected);
    }

    private static void assertFieldValueEquals(String owner, Object actual, Object expected) {
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
            assertListValueEquals(owner, aList, eList);
        } else if (Collection.class.isAssignableFrom(clazz)) { // 集合类
            List<Object> aList = new ArrayList<>();
            aList.addAll((Collection<?>) actual);
            List<Object> eList = new ArrayList<>();
            eList.addAll((Collection<?>) expected);
            assertListValueEquals(owner, aList, eList);
        } else if (Map.class.isAssignableFrom(clazz)) { // MAP类
            // TODO
        } else { // 其他类比较字段
            // 遍历所有字段
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
                desc = (owner == null ? clazz.getSimpleName() : owner) + "." + field.getName();
                // 如果发生异常了, 异常必须相等, 否则字段值必须相等
                if (aException != null || eException != null) {
                    System.out.println("actual   " + desc + " = " + aException.toString());
                    System.out.println("expected " + desc + " = " + eException.toString());
                    Assert.assertEquals(aException.getClass(), eException.getClass(), "Get " + desc + " exception");
                    Assert.assertEquals(aException.getMessage(), eException.getMessage(), "Get " + desc + " exception");
                    continue;
                }
                assertFieldValueEquals(desc, aValue, eValue);
            }
        }
    }

    private static void assertListValueEquals(String owner, List<?> aList, List<?> eList) {
        String desc = owner == null ? "" : owner;
        // 空集合
        if (aList.isEmpty() && eList.isEmpty()) {
            System.out.println("actual   " + desc + " = " + "[]");
            System.out.println("expected " + desc + " = " + "[]");
            return;
        }
        Assert.assertEquals(aList.size(), eList.size(), (owner == null ? "" : owner + " ") + "size");

        for (int i = 0; i < aList.size(); i++) {
            Object aItem = aList.get(i);
            Object eItem = eList.get(i);
            assertFieldValueEquals(desc + '[' + i + ']', aItem, eItem);
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
