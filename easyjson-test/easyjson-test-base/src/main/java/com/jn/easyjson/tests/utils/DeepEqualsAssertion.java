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

import com.jn.langx.util.Numbers;
import com.jn.langx.util.reflect.Reflects;

/**
 * 深度判断两个对象是否相等
 *
 * @author zhaohuihua
 * @version 20200619
 */
public class DeepEqualsAssertion {

    private static enum Exit {
        YES, NO
    }

    private static class NotExist {

        public static NotExist instance = new NotExist();
    }

    private List<String> errors = new ArrayList<>();

    public List<String> getErrors() {
        return errors;
    }

    // TODO 优化ToString, 判断list/map/超长字符串等
    private String objectToString(Object object) {
        if (object instanceof NotExist) {
            return "#NotExist#";
        }
        return object.toString() + "(" + object.getClass().getSimpleName() + ")";
    }

    private void assertValueEquals(Object actual, Object expected, String owner) {
        if (equals(actual, expected)) {
            return;
        }
        String aString = objectToString(actual);
        String eString = objectToString(expected);
        if (aString.length() < 30 && eString.length() < 30) {
            errors.add(owner + " value, expected = " + eString + ", actual = " + aString);
        } else {
            errors.add(owner + " value, expected = " + eString);
            errors.add(owner + " value, actual   = " + aString);
        }
    }

    /** 容器类Size是否一致 **/
    private Exit assertSizeEquals(int actual, int expected, String owner) {
        if (actual == expected) {
            return Exit.NO;
        }
        errors.add(owner + " size, expected = " + expected + ", actual = " + actual);
        return Exit.NO; // 不退出, 继续比较内容
    }

    /** 异常是否一致 **/
    private Exit assertExceptionEquals(Throwable actual, Throwable expected, String owner) {
        if (actual == null && expected == null) {
            return Exit.NO;
        }
        if (actual.getClass() == expected.getClass()) {
            return Exit.YES;
        }
        String aString = objectToString(actual);
        String eString = objectToString(expected);
        if (aString.length() < 30 && eString.length() < 30) {
            errors.add(owner + " exception, expected = " + eString + ", actual = " + aString);
        } else {
            errors.add(owner + " exception, expected = " + eString);
            errors.add(owner + " exception, actual   = " + aString);
        }
        return Exit.YES; // 不退出, 继续比较内容
    }

    /** Null状态是否一致, 要么同时为空, 要么同时不为空 **/
    private Exit assertNullStateEquals(Object actual, Object expected, String owner) {
        if (actual == null && expected == null) {
            return Exit.YES;
        }
        if (actual != null && expected != null) {
            if (actual instanceof NotExist && expected instanceof NotExist) {
                return Exit.YES;
            }
            if (!(actual instanceof NotExist) && !(expected instanceof NotExist)) {
                return Exit.NO;
            }
            String aString = objectToString(actual);
            String eString = objectToString(expected);
            if (expected instanceof NotExist) {
                errors.add(owner + ", expected = " + eString + ", but actual is " + aString);
            } else {
                errors.add(owner + ", actual = " + aString + ", but expected is " + eString);
            }
            return Exit.YES;
        }
        if (expected == null) {
            errors.add(owner + ", expected is null, but actual is " + objectToString(actual));
        } else {
            errors.add(owner + ", actual is null, but expected is " + objectToString(expected));
        }
        return Exit.YES;
    }

    /** Empty状态是否一致, 要么同时为空, 要么同时不为空 **/
    private Exit assertEmptyStateEquals(Map<?, ?> actual, Map<?, ?> expected, String owner) {
        if (actual.isEmpty() && expected.isEmpty()) {
            return Exit.YES;
        }
        if (!actual.isEmpty() && !expected.isEmpty()) {
            return Exit.NO;
        }
        if (expected == null) {
            errors.add(owner + ", expected is empty map, but actual is " + objectToString(actual));
        } else {
            errors.add(owner + ", actual is empty map, but expected is " + objectToString(expected));
        }
        return Exit.YES;
    }

    /** Empty状态是否一致, 要么同时为空, 要么同时不为空 **/
    private Exit assertEmptyStateEquals(List<?> actual, List<?> expected, String owner) {
        if (actual.isEmpty() && expected.isEmpty()) {
            return Exit.YES;
        }
        if (!actual.isEmpty() && !expected.isEmpty()) {
            return Exit.NO;
        }
        if (expected == null) {
            errors.add(owner + ", expected is empty collection, but actual is " + objectToString(actual));
        } else {
            errors.add(owner + ", actual is empty collection, but expected is " + objectToString(expected));
        }
        return Exit.YES;
    }

    /** 判断class是否相同 **/
    private Exit assertClassEquals(Class<?> actual, Class<?> expected, String owner) {
        if (actual != expected) {
            return Exit.NO;
        }
        String aClass = actual.getSimpleName();
        String eClass = expected.getSimpleName();
        if (aClass.equals(eClass)) {
            aClass = actual.getName();
            eClass = expected.getName();
        }
        errors.add(owner + " class, expected = " + eClass + ", actual = " + aClass);
        return Exit.YES;
    }

    public void assertDeepEquals(Object actual, Object expected, String owner) {
        // 检查Null状态是否一致
        if (assertNullStateEquals(actual, expected, owner) == Exit.YES) {
            return;
        }
        Class<?> eClass = expected.getClass();
        Class<?> aClass = actual.getClass();
        if(Reflects.isSubClass(Number.class, aClass) && Reflects.isSubClass(Number.class, eClass)){
            assertValueEquals(Numbers.toDouble((Number) actual), Numbers.toDouble((Number)expected), owner);
        }else
//        if (isIntegralNumber(eClass) && isIntegralNumber(aClass)) { // 整形的数字
//            assertValueEquals(((Number) actual).longValue(), ((Number) expected).longValue(), owner);
//        } else if (Number.class.isAssignableFrom(eClass) && Number.class.isAssignableFrom(aClass)) { // 数字类
//            assertValueEquals(((Number) actual).doubleValue(), ((Number) expected).doubleValue(), owner);
//        } else 
        if (isPrimitive(eClass) && isPrimitive(aClass)) { // 基本类型
            assertValueEquals(actual, expected, owner);
        } else if (Map.class.isAssignableFrom(eClass) && Map.class.isAssignableFrom(aClass)) { // MAP类
            Map<?, ?> aMap = (Map<?, ?>) actual;
            Map<?, ?> eMap = (Map<?, ?>) expected;
            assertMapValueEquals(aMap, eMap, owner);
        } else if (eClass.isArray() && aClass.isArray()) { // 数组类
            // 先比对class是否相等
            Class<?> aType = aClass.getComponentType();
            Class<?> eType = eClass.getComponentType();
            // 先比对class是否相同
            if (assertClassEquals(aType, eType, owner) != Exit.YES) {
                List<Object> aList = Arrays.asList((Object[]) actual);
                List<Object> eList = Arrays.asList((Object[]) expected);
                assertListValueEquals(aList, eList, owner);
            }
        } else if (Collection.class.isAssignableFrom(eClass) && Collection.class.isAssignableFrom(aClass)) { // 集合类
            List<Object> aList = new ArrayList<>();
            aList.addAll((Collection<?>) actual);
            List<Object> eList = new ArrayList<>();
            eList.addAll((Collection<?>) expected);
            assertListValueEquals(aList, eList, owner);
        } else if (Iterable.class.isAssignableFrom(eClass) && Iterable.class.isAssignableFrom(aClass)) { // 迭代类
            List<Object> aList = iterableToList((Iterable<?>) actual);
            List<Object> eList = iterableToList((Iterable<?>) expected);
            assertListValueEquals(aList, eList, owner);
        } else { // 其他类型, 比较字段值
            assertFieldValueEquals(actual, expected, owner);
        }
    }

    private void assertListValueEquals(List<?> actual, List<?> expected, String owner) {
        // 检查Empty状态是否一致
        if (assertEmptyStateEquals(actual, expected, owner) == Exit.YES) {
            return;
        }
        // 检查Size是否一致
        if (assertSizeEquals(actual.size(), expected.size(), owner) == Exit.YES) {
            return;
        }
        int size = Math.max(actual.size(), expected.size());
        for (int i = 0; i < size; i++) {
            Object aItem = getListItem(actual, i);
            Object eItem = getListItem(expected, i);
            assertDeepEquals(aItem, eItem, owner + '[' + i + ']');
        }
    }

    private void assertMapValueEquals(Map<?, ?> actual, Map<?, ?> expected, String owner) {
        // 检查Empty状态是否一致
        if (assertEmptyStateEquals(actual, expected, owner) == Exit.YES) {
            return;
        }
        // 检查Size是否一致
        if (assertSizeEquals(actual.size(), expected.size(), owner) == Exit.YES) {
            return;
        }

        Set<Object> keys = new LinkedHashSet<>();
        keys.addAll(actual.keySet());
        keys.addAll(expected.keySet());
        for (Object key : keys) {
            String desc = owner + '.' + key;
            Object aValue = getMapValue(actual, key);
            Object eValue = getMapValue(expected, key);
            assertDeepEquals(aValue, eValue, desc);
        }
    }

    private void assertFieldValueEquals(Object actual, Object expected, String owner) {
        // 先比对class是否相同
        if (assertClassEquals(actual.getClass(), expected.getClass(), owner) != Exit.YES) {
            return;
        }
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
            // 先判断异常是否相等
            if (assertExceptionEquals(aException, eException, desc) != Exit.YES) {
                // 再判断值是否相等
                assertDeepEquals(aValue, eValue, desc);
            }
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

    private static Object getListItem(List<?> list, int index) {
        if (index < list.size()) {
            return list.get(index);
        } else {
            return NotExist.instance;
        }
    }

    private static Object getMapValue(Map<?, ?> map, Object key) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return NotExist.instance;
        }
    }

    private static boolean equals(Object actual, Object expected) {
        if (actual == null && expected == null) {
            return true;
        } else if (actual == null ^ expected == null) {
            return false;
        } else {
            return expected.equals(actual) && actual.equals(expected);
        }
    }

    /** 是不是整形的数字 **/
    @SuppressWarnings("unused")
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
