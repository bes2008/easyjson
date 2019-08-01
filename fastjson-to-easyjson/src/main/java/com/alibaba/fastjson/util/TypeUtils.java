/*
 * Copyright 1999-2017 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jn.easyjson.core.util.type.Types;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class TypeUtils {

    public static boolean compatibleWithJavaBean = false;
    /**
     * 根据field name的大小写输出输入数据
     */
    public static boolean compatibleWithFieldName = false;
    private static boolean setAccessibleEnable = true;
    private static boolean optionalClassInited = false;
    private static Class<?> optionalClass;
    private static boolean transientClassInited = false;
    private static Class<? extends Annotation> transientClass;

    private static Class<? extends Annotation> classOneToMany = null;
    private static boolean classOneToManyError = false;
    private static Class<? extends Annotation> classManyToMany = null;
    private static boolean classManyToManyError = false;

    private static Method methodHibernateIsInitialized = null;
    private static boolean methodHibernateIsInitializedError = false;
    private static volatile Class kotlin_metadata;
    private static volatile boolean kotlin_metadata_error;
    private static volatile boolean kotlinClassKlassError;
    private static volatile Constructor kotlinClassConstructor;
    private static volatile Method kotlinClassGetConstructors;
    private static volatile Method kotlinFunctionGetParameters;
    private static volatile Method kotlinParameterGetName;
    private static volatile boolean kotlinError;
    private static volatile Map<Class, String[]> kotlinIgnores;
    private static volatile boolean kotlinIgnoresError;
    private static ConcurrentMap<String, Class<?>> mappings = new ConcurrentHashMap<String, Class<?>>(256, 0.75f, 1);
    private static Class<?> pathClass;
    private static boolean pathClassError = false;

    private static Class<? extends Annotation> classJacksonCreator = null;
    private static boolean classJacksonCreatorError = false;

    private static volatile Class classClob = null;
    private static volatile boolean classClobError = false;

    private static volatile Class class_XmlAccessType = null;
    private static volatile Class classXmlAccessorType = null;
    private static volatile boolean classXmlAccessorType_error = false;
    private static volatile Method methodXmlAccessorTypeValue = null;
    private static volatile Field fieldXmlAccessTypeField = null;
    private static volatile Object fieldXmlAccessTypeFieldValue = null;

    static {
        try {
            TypeUtils.compatibleWithJavaBean = "true".equals(IOUtils.getStringProperty(IOUtils.FASTJSON_COMPATIBLEWITHJAVABEAN));
            TypeUtils.compatibleWithFieldName = "true".equals(IOUtils.getStringProperty(IOUtils.FASTJSON_COMPATIBLEWITHFIELDNAME));
        } catch (Throwable e) {
            // skip
        }
    }

    static {
        addBaseClassMappings();
    }


    public static boolean isXmlField(Class clazz) {
        if (classXmlAccessorType == null && !classXmlAccessorType_error) {
            try {
                classXmlAccessorType = Class.forName("javax.xml.bind.annotation.XmlAccessorType");
            } catch (Throwable ex) {
                classXmlAccessorType_error = true;
            }
        }

        if (classXmlAccessorType == null) {
            return false;
        }

        Annotation annotation = clazz.getAnnotation(classXmlAccessorType);
        if (annotation == null) {
            return false;
        }

        if (methodXmlAccessorTypeValue == null && !classXmlAccessorType_error) {
            try {
                methodXmlAccessorTypeValue = classXmlAccessorType.getMethod("value");
            } catch (Throwable ex) {
                classXmlAccessorType_error = true;
            }
        }

        if (methodXmlAccessorTypeValue == null) {
            return false;
        }

        Object value = null;
        if (!classXmlAccessorType_error) {
            try {
                value = methodXmlAccessorTypeValue.invoke(annotation);
            } catch (Throwable ex) {
                classXmlAccessorType_error = true;
            }
        }
        if (value == null) {
            return false;
        }

        if (class_XmlAccessType == null && !classXmlAccessorType_error) {
            try {
                class_XmlAccessType = Class.forName("javax.xml.bind.annotation.XmlAccessType");
                fieldXmlAccessTypeField = class_XmlAccessType.getField("FIELD");
                fieldXmlAccessTypeFieldValue = fieldXmlAccessTypeField.get(null);
            } catch (Throwable ex) {
                classXmlAccessorType_error = true;
            }
        }

        return value == fieldXmlAccessTypeFieldValue;
    }

    public static Annotation getXmlAccessorType(Class clazz) {
        if (classXmlAccessorType == null && !classXmlAccessorType_error) {

            try {
                classXmlAccessorType = Class.forName("javax.xml.bind.annotation.XmlAccessorType");
            } catch (Throwable ex) {
                classXmlAccessorType_error = true;
            }
        }

        if (classXmlAccessorType == null) {
            return null;
        }

        return clazz.getAnnotation(classXmlAccessorType);
    }

//
//    public static boolean isXmlAccessType(Class clazz) {
//        if (class_XmlAccessType == null && !class_XmlAccessType_error) {
//
//            try{
//                class_XmlAccessType = Class.forName("javax.xml.bind.annotation.XmlAccessType");
//            } catch(Throwable ex){
//                class_XmlAccessType_error = true;
//            }
//        }
//
//        if (class_XmlAccessType == null) {
//            return false;
//        }
//
//        return  class_XmlAccessType.isAssignableFrom(clazz);
//    }

    public static boolean isClob(Class clazz) {
        if (classClob == null && !classClobError) {

            try {
                classClob = Class.forName("java.sql.Clob");
            } catch (Throwable ex) {
                classClobError = true;
            }
        }

        if (classClob == null) {
            return false;
        }

        return classClob.isAssignableFrom(clazz);
    }

    public static String castToString(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public static Byte castToByte(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return byteValue((BigDecimal) value);
        }

        if (value instanceof Number) {
            return ((Number) value).byteValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return null;
            }
            return Byte.parseByte(strVal);
        }
        throw new JSONException("can not cast to byte, value : " + value);
    }

    public static Character castToChar(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Character) {
            return (Character) value;
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }
            if (strVal.length() != 1) {
                throw new JSONException("can not cast to char, value : " + value);
            }
            return strVal.charAt(0);
        }
        throw new JSONException("can not cast to char, value : " + value);
    }

    public static Short castToShort(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return shortValue((BigDecimal) value);
        }

        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return null;
            }
            return Short.parseShort(strVal);
        }

        throw new JSONException("can not cast to short, value : " + value);
    }

    public static BigDecimal castToBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }
        String strVal = value.toString();
        if (strVal.length() == 0) {
            return null;
        }
        if (value instanceof Map && ((Map) value).size() == 0) {
            return null;
        }
        return new BigDecimal(strVal);
    }

    public static BigInteger castToBigInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        if (value instanceof Float || value instanceof Double) {
            return BigInteger.valueOf(((Number) value).longValue());
        }
        if (value instanceof BigDecimal) {
            BigDecimal decimal = (BigDecimal) value;
            int scale = decimal.scale();
            if (scale > -1000 && scale < 1000) {
                return ((BigDecimal) value).toBigInteger();
            }
        }
        String strVal = value.toString();
        if (strVal.length() == 0 //
                || "null".equals(strVal) //
                || "NULL".equals(strVal)) {
            return null;
        }
        return new BigInteger(strVal);
    }

    public static Float castToFloat(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return null;
            }
            if (strVal.indexOf(',') != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            return Float.parseFloat(strVal);
        }
        throw new JSONException("can not cast to float, value : " + value);
    }

    public static Double castToDouble(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return null;
            }
            if (strVal.indexOf(',') != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            return Double.parseDouble(strVal);
        }
        throw new JSONException("can not cast to double, value : " + value);
    }

    public static Date castToDate(Object value) {
        return castToDate(value, null);
    }

    public static Date castToDate(Object value, String format) {
        if (value == null) {
            return null;
        }

        if (value instanceof Date) { // 使用频率最高的，应优先处理
            return (Date) value;
        }

        if (value instanceof Calendar) {
            return ((Calendar) value).getTime();
        }

        long longValue = -1;

        if (value instanceof BigDecimal) {
            longValue = longValue((BigDecimal) value);
            return new Date(longValue);
        }

        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
            return new Date(longValue);
        }

        if (value instanceof String) {
            try {
                return new SimpleDateFormat(format).parse((String) value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return new Date(longValue);
    }


    public static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch == '+' || ch == '-') {
                if (i != 0) {
                    return false;
                }
            } else if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }


    public static byte byteValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }

        int scale = decimal.scale();
        if (scale >= -100 && scale <= 100) {
            return decimal.byteValue();
        }

        return decimal.byteValueExact();
    }

    public static short shortValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }

        int scale = decimal.scale();
        if (scale >= -100 && scale <= 100) {
            return decimal.shortValue();
        }

        return decimal.shortValueExact();
    }

    public static int intValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }

        int scale = decimal.scale();
        if (scale >= -100 && scale <= 100) {
            return decimal.intValue();
        }

        return decimal.intValueExact();
    }

    public static long longValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }

        int scale = decimal.scale();
        if (scale >= -100 && scale <= 100) {
            return decimal.longValue();
        }

        return decimal.longValueExact();
    }

    public static Integer castToInt(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return (Integer) value;
        }

        if (value instanceof BigDecimal) {
            return intValue((BigDecimal) value);
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return null;
            }
            if (strVal.indexOf(',') != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            return Integer.parseInt(strVal);
        }

        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue() ? 1 : 0;
        }
        if (value instanceof Map) {
            Map map = (Map) value;
            if (map.size() == 2
                    && map.containsKey("andIncrement")
                    && map.containsKey("andDecrement")) {
                Iterator iter = map.values().iterator();
                iter.next();
                Object value2 = iter.next();
                return castToInt(value2);
            }
        }
        throw new JSONException("can not cast to int, value : " + value);
    }

    public static byte[] castToBytes(Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }
        if (value instanceof String) {
            return IOUtils.decodeBase64((String) value);
        }
        throw new JSONException("can not cast to int, value : " + value);
    }

    public static Boolean castToBoolean(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        if (value instanceof BigDecimal) {
            return intValue((BigDecimal) value) == 1;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return null;
            }
            if ("true".equalsIgnoreCase(strVal) //
                    || "1".equals(strVal)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(strVal) //
                    || "0".equals(strVal)) {
                return Boolean.FALSE;
            }
            if ("Y".equalsIgnoreCase(strVal) //
                    || "T".equals(strVal)) {
                return Boolean.TRUE;
            }
            if ("F".equalsIgnoreCase(strVal) //
                    || "N".equals(strVal)) {
                return Boolean.FALSE;
            }
        }
        throw new JSONException("can not cast to boolean, value : " + value);
    }

    public static <T> T castToJavaBean(Object obj, Class<T> clazz) {
        return cast(obj, clazz, ParserConfig.getGlobalInstance());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})

    public static Locale toLocale(String strVal) {
        String[] items = strVal.split("_");
        if (items.length == 1) {
            return new Locale(items[0]);
        }
        if (items.length == 2) {
            return new Locale(items[0], items[1]);
        }
        return new Locale(items[0], items[1], items[2]);
    }


    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Type type, ParserConfig mapping) {
        if (obj == null) {
            return null;
        }
        if (type instanceof Class) {
            return (T) cast(obj, (Class<T>) type, mapping);
        }
        if (type instanceof ParameterizedType) {
            return (T) cast(obj, (ParameterizedType) type, mapping);
        }
        if (obj instanceof String) {
            String strVal = (String) obj;
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return null;
            }
        }
        if (type instanceof TypeVariable) {
            return (T) obj;
        }
        throw new JSONException("can not cast to : " + type);
    }


    private static void addBaseClassMappings() {
        mappings.put("byte", byte.class);
        mappings.put("short", short.class);
        mappings.put("int", int.class);
        mappings.put("long", long.class);
        mappings.put("float", float.class);
        mappings.put("double", double.class);
        mappings.put("boolean", boolean.class);
        mappings.put("char", char.class);
        mappings.put("[byte", byte[].class);
        mappings.put("[short", short[].class);
        mappings.put("[int", int[].class);
        mappings.put("[long", long[].class);
        mappings.put("[float", float[].class);
        mappings.put("[double", double[].class);
        mappings.put("[boolean", boolean[].class);
        mappings.put("[char", char[].class);
        mappings.put("[B", byte[].class);
        mappings.put("[S", short[].class);
        mappings.put("[I", int[].class);
        mappings.put("[J", long[].class);
        mappings.put("[F", float[].class);
        mappings.put("[D", double[].class);
        mappings.put("[C", char[].class);
        mappings.put("[Z", boolean[].class);
        Class<?>[] classes = new Class[]{
                Object.class,
                java.lang.Cloneable.class,
                loadClass("java.lang.AutoCloseable"),
                java.lang.Exception.class,
                java.lang.RuntimeException.class,
                java.lang.IllegalAccessError.class,
                java.lang.IllegalAccessException.class,
                java.lang.IllegalArgumentException.class,
                java.lang.IllegalMonitorStateException.class,
                java.lang.IllegalStateException.class,
                java.lang.IllegalThreadStateException.class,
                java.lang.IndexOutOfBoundsException.class,
                java.lang.InstantiationError.class,
                java.lang.InstantiationException.class,
                java.lang.InternalError.class,
                java.lang.InterruptedException.class,
                java.lang.LinkageError.class,
                java.lang.NegativeArraySizeException.class,
                java.lang.NoClassDefFoundError.class,
                java.lang.NoSuchFieldError.class,
                java.lang.NoSuchFieldException.class,
                java.lang.NoSuchMethodError.class,
                java.lang.NoSuchMethodException.class,
                java.lang.NullPointerException.class,
                java.lang.NumberFormatException.class,
                java.lang.OutOfMemoryError.class,
                java.lang.SecurityException.class,
                java.lang.StackOverflowError.class,
                java.lang.StringIndexOutOfBoundsException.class,
                java.lang.TypeNotPresentException.class,
                java.lang.VerifyError.class,
                java.lang.StackTraceElement.class,
                java.util.HashMap.class,
                java.util.Hashtable.class,
                java.util.TreeMap.class,
                java.util.IdentityHashMap.class,
                java.util.WeakHashMap.class,
                java.util.LinkedHashMap.class,
                java.util.HashSet.class,
                java.util.LinkedHashSet.class,
                java.util.TreeSet.class,
                java.util.ArrayList.class,
                java.util.concurrent.TimeUnit.class,
                java.util.concurrent.ConcurrentHashMap.class,
                loadClass("java.util.concurrent.ConcurrentSkipListMap"),
                loadClass("java.util.concurrent.ConcurrentSkipListSet"),
                java.util.concurrent.atomic.AtomicInteger.class,
                java.util.concurrent.atomic.AtomicLong.class,
                java.util.Collections.EMPTY_MAP.getClass(),
                java.lang.Boolean.class,
                java.lang.Character.class,
                java.lang.Byte.class,
                java.lang.Short.class,
                java.lang.Integer.class,
                java.lang.Long.class,
                java.lang.Float.class,
                java.lang.Double.class,
                java.lang.Number.class,
                java.lang.String.class,
                java.math.BigDecimal.class,
                java.math.BigInteger.class,
                java.util.BitSet.class,
                java.util.Calendar.class,
                java.util.Date.class,
                java.util.Locale.class,
                java.util.UUID.class,
                java.sql.Time.class,
                java.sql.Date.class,
                java.sql.Timestamp.class,
                java.text.SimpleDateFormat.class,
                com.alibaba.fastjson.JSONObject.class,
                com.alibaba.fastjson.JSONArray.class,
        };
        for (Class clazz : classes) {
            if (clazz == null) {
                continue;
            }
            mappings.put(clazz.getName(), clazz);
        }

        String[] w = new String[]{
                "java.util.Collections$UnmodifiableMap"
        };
        for (String className : w) {
            Class<?> clazz = loadClass(className);
            if (clazz == null) {
                break;
            }
            mappings.put(clazz.getName(), clazz);
        }

        String[] awt = new String[]{
                "java.awt.Rectangle",
                "java.awt.Point",
                "java.awt.Font",
                "java.awt.Color"};
        for (String className : awt) {
            Class<?> clazz = loadClass(className);
            if (clazz == null) {
                break;
            }
            mappings.put(clazz.getName(), clazz);
        }

        String[] spring = new String[]{
                "org.springframework.util.LinkedMultiValueMap",
                "org.springframework.util.LinkedCaseInsensitiveMap",
                "org.springframework.remoting.support.RemoteInvocation",
                "org.springframework.remoting.support.RemoteInvocationResult",
                "org.springframework.security.web.savedrequest.DefaultSavedRequest",
                "org.springframework.security.web.savedrequest.SavedCookie",
                "org.springframework.security.web.csrf.DefaultCsrfToken",
                "org.springframework.security.web.authentication.WebAuthenticationDetails",
                "org.springframework.security.core.context.SecurityContextImpl",
                "org.springframework.security.authentication.UsernamePasswordAuthenticationToken",
                "org.springframework.security.core.authority.SimpleGrantedAuthority",
                "org.springframework.security.core.userdetails.User",
                "org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken",
                "org.springframework.security.oauth2.common.DefaultOAuth2AccessToken",
                "org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken",
                "org.springframework.cache.support.NullValue",
        };
        for (String className : spring) {
            Class<?> clazz = loadClass(className);
            if (clazz == null) {
                continue;
            }
            mappings.put(clazz.getName(), clazz);
        }
    }

    public static void clearClassMapping() {
        mappings.clear();
        addBaseClassMappings();
    }

    public static void addMapping(String className, Class<?> clazz) {
        mappings.put(className, clazz);
    }


    public static boolean isPath(Class<?> clazz) {
        if (pathClass == null && !pathClassError) {
            try {
                pathClass = Class.forName("java.nio.file.Path");
            } catch (Throwable ex) {
                pathClassError = true;
            }
        }
        if (pathClass != null) {
            return pathClass.isAssignableFrom(clazz);
        }
        return false;
    }

    public static Class<?> getClassFromMapping(String className) {
        return mappings.get(className);
    }

    public static Class<?> loadClass(String className) {
        return loadClass(className, null);
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader) {
        return loadClass(className, classLoader, false);
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader, boolean cache) {
        if (className == null || className.length() == 0 || className.length() > 128) {
            return null;
        }

        Class<?> clazz = mappings.get(className);
        if (clazz != null) {
            return clazz;
        }

        if (className.charAt(0) == '[') {
            Class<?> componentType = loadClass(className.substring(1), classLoader);
            return Array.newInstance(componentType, 0).getClass();
        }

        if (className.startsWith("L") && className.endsWith(";")) {
            String newClassName = className.substring(1, className.length() - 1);
            return loadClass(newClassName, classLoader);
        }

        try {
            if (classLoader != null) {
                clazz = classLoader.loadClass(className);
                if (cache) {
                    mappings.put(className, clazz);
                }
                return clazz;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            // skip
        }
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null && contextClassLoader != classLoader) {
                clazz = contextClassLoader.loadClass(className);
                if (cache) {
                    mappings.put(className, clazz);
                }
                return clazz;
            }
        } catch (Throwable e) {
            // skip
        }
        try {
            clazz = Class.forName(className);
            if (cache) {
                mappings.put(className, clazz);
            }
            return clazz;
        } catch (Throwable e) {
            // skip
        }
        return clazz;
    }


    private static String getPropertyNameByCompatibleFieldName(Map<String, Field> fieldCacheMap, String methodName,
                                                               String propertyName, int fromIdx) {
        if (compatibleWithFieldName) {
            if (!fieldCacheMap.containsKey(propertyName)) {
                String tempPropertyName = methodName.substring(fromIdx);
                return fieldCacheMap.containsKey(tempPropertyName) ? tempPropertyName : propertyName;
            }
        }
        return propertyName;
    }

    public static JSONField getSuperMethodAnnotation(final Class<?> clazz, final Method method) {
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            Class<?>[] types = method.getParameterTypes();
            for (Class<?> interfaceClass : interfaces) {
                for (Method interfaceMethod : interfaceClass.getMethods()) {
                    Class<?>[] interfaceTypes = interfaceMethod.getParameterTypes();
                    if (interfaceTypes.length != types.length) {
                        continue;
                    }
                    if (!interfaceMethod.getName().equals(method.getName())) {
                        continue;
                    }
                    boolean match = true;
                    for (int i = 0; i < types.length; ++i) {
                        if (!interfaceTypes[i].equals(types[i])) {
                            match = false;
                            break;
                        }
                    }
                    if (!match) {
                        continue;
                    }
                    JSONField annotation = interfaceMethod.getAnnotation(JSONField.class);
                    if (annotation != null) {
                        return annotation;
                    }
                }
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null) {
            return null;
        }
        if (Modifier.isAbstract(superClass.getModifiers())) {
            Class<?>[] types = method.getParameterTypes();
            for (Method interfaceMethod : superClass.getMethods()) {
                Class<?>[] interfaceTypes = interfaceMethod.getParameterTypes();
                if (interfaceTypes.length != types.length) {
                    continue;
                }
                if (!interfaceMethod.getName().equals(method.getName())) {
                    continue;
                }
                boolean match = true;
                for (int i = 0; i < types.length; ++i) {
                    if (!interfaceTypes[i].equals(types[i])) {
                        match = false;
                        break;
                    }
                }
                if (!match) {
                    continue;
                }
                JSONField annotation = interfaceMethod.getAnnotation(JSONField.class);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        return null;
    }

    private static boolean isJSONTypeIgnore(Class<?> clazz, String propertyName) {
        JSONType jsonType = TypeUtils.getAnnotation(clazz, JSONType.class);
        if (jsonType != null) {
            // 1、新增 includes 支持，如果 JSONType 同时设置了includes 和 ignores 属性，则以includes为准。
            // 2、个人认为对于大小写敏感的Java和JS而言，使用 equals() 比 equalsIgnoreCase() 更好，改动的唯一风险就是向后兼容性的问题
            // 不过，相信开发者应该都是严格按照大小写敏感的方式进行属性设置的
            String[] fields = jsonType.includes();
            if (fields.length > 0) {
                for (int i = 0; i < fields.length; i++) {
                    if (propertyName.equals(fields[i])) {
                        return false;
                    }
                }
                return true;
            } else {
                fields = jsonType.ignores();
                for (int i = 0; i < fields.length; i++) {
                    if (propertyName.equals(fields[i])) {
                        return true;
                    }
                }
            }
        }
        if (clazz.getSuperclass() != Object.class && clazz.getSuperclass() != null) {
            if (isJSONTypeIgnore(clazz.getSuperclass(), propertyName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGenericParamType(Type type) {
        if (type instanceof ParameterizedType) {
            return true;
        }
        if (type instanceof Class) {
            Type superType = ((Class<?>) type).getGenericSuperclass();
            return superType != Object.class && isGenericParamType(superType);
        }
        return false;
    }

    public static Type getGenericParamType(Type type) {
        if (type instanceof ParameterizedType) {
            return type;
        }
        if (type instanceof Class) {
            return getGenericParamType(((Class<?>) type).getGenericSuperclass());
        }
        return type;
    }

    public static Type unwrapOptional(Type type) {
        if (!optionalClassInited) {
            try {
                optionalClass = Class.forName("java.util.Optional");
            } catch (Exception e) {
                // skip
            } finally {
                optionalClassInited = true;
            }
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (parameterizedType.getRawType() == optionalClass) {
                return parameterizedType.getActualTypeArguments()[0];
            }
        }
        return type;
    }

    public static Class<?> getClass(Type type) {
        if (type.getClass() == Class.class) {
            return (Class<?>) type;
        }

        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }

        if (type instanceof TypeVariable) {
            Type boundType = ((TypeVariable<?>) type).getBounds()[0];
            if (boundType instanceof Class) {
                return (Class) boundType;
            }
            return getClass(boundType);
        }

        if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length == 1) {
                return getClass(upperBounds[0]);
            }
        }

        return Object.class;
    }

    public static Field getField(Class<?> clazz, String fieldName, Field[] declaredFields) {
        for (Field field : declaredFields) {
            String itemName = field.getName();
            if (fieldName.equals(itemName)) {
                return field;
            }

            char c0, c1;
            if (fieldName.length() > 2
                    && (c0 = fieldName.charAt(0)) >= 'a' && c0 <= 'z'
                    && (c1 = fieldName.charAt(1)) >= 'A' && c1 <= 'Z'
                    && fieldName.equalsIgnoreCase(itemName)) {
                return field;
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            return getField(superClass, fieldName, superClass.getDeclaredFields());
        }
        return null;
    }

    /**
     * @deprecated
     */
    public static int getSerializeFeatures(Class<?> clazz) {
        JSONType annotation = TypeUtils.getAnnotation(clazz, JSONType.class);
        if (annotation == null) {
            return 0;
        }
        return SerializerFeature.of(annotation.serialzeFeatures());
    }

    public static int getParserFeatures(Class<?> clazz) {
        JSONType annotation = TypeUtils.getAnnotation(clazz, JSONType.class);
        if (annotation == null) {
            return 0;
        }
        return Feature.of(annotation.parseFeatures());
    }

    public static String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    static void setAccessible(AccessibleObject obj) {
        if (!setAccessibleEnable) {
            return;
        }
        if (obj.isAccessible()) {
            return;
        }
        try {
            obj.setAccessible(true);
        } catch (AccessControlException error) {
            setAccessibleEnable = false;
        }
    }

    public static Type getCollectionItemType(Type fieldType) {
        if (fieldType instanceof ParameterizedType) {
            return getCollectionItemType((ParameterizedType) fieldType);
        }
        if (fieldType instanceof Class<?>) {
            return getCollectionItemType((Class<?>) fieldType);
        }
        return Object.class;
    }

    private static Type getCollectionItemType(Class<?> clazz) {
        return clazz.getName().startsWith("java.")
                ? Object.class
                : getCollectionItemType(getCollectionSuperType(clazz));
    }

    private static Type getCollectionItemType(ParameterizedType parameterizedType) {
        Type rawType = parameterizedType.getRawType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (rawType == Collection.class) {
            return getWildcardTypeUpperBounds(actualTypeArguments[0]);
        }
        Class<?> rawClass = (Class<?>) rawType;
        Map<TypeVariable, Type> actualTypeMap = createActualTypeMap(rawClass.getTypeParameters(), actualTypeArguments);
        Type superType = getCollectionSuperType(rawClass);
        if (superType instanceof ParameterizedType) {
            Class<?> superClass = getRawClass(superType);
            Type[] superClassTypeParameters = ((ParameterizedType) superType).getActualTypeArguments();
            return superClassTypeParameters.length > 0
                    ? getCollectionItemType(makeParameterizedType(superClass, superClassTypeParameters, actualTypeMap))
                    : getCollectionItemType(superClass);
        }
        return getCollectionItemType((Class<?>) superType);
    }

    private static Type getCollectionSuperType(Class<?> clazz) {
        Type assignable = null;
        for (Type type : clazz.getGenericInterfaces()) {
            Class<?> rawClass = getRawClass(type);
            if (rawClass == Collection.class) {
                return type;
            }
            if (Collection.class.isAssignableFrom(rawClass)) {
                assignable = type;
            }
        }
        return assignable == null ? clazz.getGenericSuperclass() : assignable;
    }

    private static Map<TypeVariable, Type> createActualTypeMap(TypeVariable[] typeParameters, Type[] actualTypeArguments) {
        int length = typeParameters.length;
        Map<TypeVariable, Type> actualTypeMap = new HashMap<TypeVariable, Type>(length);
        for (int i = 0; i < length; i++) {
            actualTypeMap.put(typeParameters[i], actualTypeArguments[i]);
        }
        return actualTypeMap;
    }

    private static ParameterizedType makeParameterizedType(Class<?> rawClass, Type[] typeParameters, Map<TypeVariable, Type> actualTypeMap) {
        int length = typeParameters.length;
        Type[] actualTypeArguments = new Type[length];
        for (int i = 0; i < length; i++) {
            actualTypeArguments[i] = getActualType(typeParameters[i], actualTypeMap);
        }
        return new ParameterizedTypeImpl(actualTypeArguments, null, rawClass);
    }

    private static Type getActualType(Type typeParameter, Map<TypeVariable, Type> actualTypeMap) {
        if (typeParameter instanceof TypeVariable) {
            return actualTypeMap.get(typeParameter);
        } else if (typeParameter instanceof ParameterizedType) {
            return makeParameterizedType(getRawClass(typeParameter), ((ParameterizedType) typeParameter).getActualTypeArguments(), actualTypeMap);
        } else if (typeParameter instanceof GenericArrayType) {
            return new GenericArrayTypeImpl(getActualType(((GenericArrayType) typeParameter).getGenericComponentType(), actualTypeMap));
        }
        return typeParameter;
    }

    public static java.sql.Timestamp castToTimestamp(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Calendar) {
            return new java.sql.Timestamp(((Calendar) value).getTimeInMillis());
        }
        if (value instanceof java.sql.Timestamp) {
            return (java.sql.Timestamp) value;
        }
        if (value instanceof java.util.Date) {
            return new java.sql.Timestamp(((java.util.Date) value).getTime());
        }
        long longValue = 0;
        if (value instanceof BigDecimal) {
            longValue = longValue((BigDecimal) value);
        } else if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return null;
            }
            if (strVal.endsWith(".000000000")) {
                strVal = strVal.substring(0, strVal.length() - 10);
            } else if (strVal.endsWith(".000000")) {
                strVal = strVal.substring(0, strVal.length() - 7);
            }
            if (isNumber(strVal)) {
                longValue = Long.parseLong(strVal);
            }
        }
        if (longValue <= 0) {
            throw new JSONException("can not cast to Timestamp, value : " + value);
        }
        return new java.sql.Timestamp(longValue);
    }

    public static java.sql.Date castToSqlDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        }
        if (value instanceof java.util.Date) {
            return new java.sql.Date(((java.util.Date) value).getTime());
        }
        if (value instanceof Calendar) {
            return new java.sql.Date(((Calendar) value).getTimeInMillis());
        }

        long longValue = 0;
        if (value instanceof BigDecimal) {
            longValue = longValue((BigDecimal) value);
        } else if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return null;
            }
            if (isNumber(strVal)) {
                longValue = Long.parseLong(strVal);
            }
        }
        if (longValue <= 0) {
            // TODO 忽略 1970-01-01 之前的时间处理？
            throw new JSONException("can not cast to Date, value : " + value);
        }
        return new java.sql.Date(longValue);
    }

    public static long castToLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            if (((String) value).endsWith("l") || ((String) value).endsWith("L")) {
                return Long.parseLong(((String) value).substring(0, ((String) value).length() - 1));
            }
            return Long.parseLong("" + value);
        }
        return 0L;
    }

    private static Type getWildcardTypeUpperBounds(Type type) {
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            Type[] upperBounds = wildcardType.getUpperBounds();
            return upperBounds.length > 0 ? upperBounds[0] : Object.class;
        }
        return type;
    }


    public static Type checkPrimitiveArray(GenericArrayType genericArrayType) {
        return Types.checkPrimitiveArray(genericArrayType);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Collection createCollection(Type type) {
        Class<?> rawClass = getRawClass(type);
        Collection list;
        if (rawClass == AbstractCollection.class //
                || rawClass == Collection.class) {
            list = new ArrayList();
        } else if (rawClass.isAssignableFrom(HashSet.class)) {
            list = new HashSet();
        } else if (rawClass.isAssignableFrom(LinkedHashSet.class)) {
            list = new LinkedHashSet();
        } else if (rawClass.isAssignableFrom(TreeSet.class)) {
            list = new TreeSet();
        } else if (rawClass.isAssignableFrom(ArrayList.class)) {
            list = new ArrayList();
        } else if (rawClass.isAssignableFrom(EnumSet.class)) {
            Type itemType;
            if (type instanceof ParameterizedType) {
                itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                itemType = Object.class;
            }
            list = EnumSet.noneOf((Class<Enum>) itemType);
        } else if (rawClass.isAssignableFrom(Queue.class)) {
            list = new LinkedList();
        } else {
            try {
                list = (Collection) rawClass.newInstance();
            } catch (Exception e) {
                throw new JSONException("create instance error, class " + rawClass.getName());
            }
        }
        return list;
    }

    public static Class<?> getRawClass(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType());
        } else {
            throw new JSONException("TODO");
        }
    }

    public static boolean isProxy(Class<?> clazz) {
        for (Class<?> item : clazz.getInterfaces()) {
            String interfaceName = item.getName();
            if (interfaceName.equals("net.sf.cglib.proxy.Factory") //
                    || interfaceName.equals("org.springframework.cglib.proxy.Factory")) {
                return true;
            }
            if (interfaceName.equals("javassist.util.proxy.ProxyObject") //
                    || interfaceName.equals("org.apache.ibatis.javassist.util.proxy.ProxyObject")
                    ) {
                return true;
            }
            if (interfaceName.equals("org.hibernate.proxy.HibernateProxy")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTransient(Method method) {
        if (method == null) {
            return false;
        }
        if (!transientClassInited) {
            try {
                transientClass = (Class<? extends Annotation>) Class.forName("java.beans.Transient");
            } catch (Exception e) {
                // skip
            } finally {
                transientClassInited = true;
            }
        }
        if (transientClass != null) {
            Annotation annotation = method.getAnnotation(transientClass);
            return annotation != null;
        }
        return false;
    }

    public static boolean isAnnotationPresentOneToMany(Method method) {
        if (method == null) {
            return false;
        }

        if (classOneToMany == null && !classOneToManyError) {
            try {
                classOneToMany = (Class<? extends Annotation>) Class.forName("javax.persistence.OneToMany");
            } catch (Throwable e) {
                // skip
                classOneToManyError = true;
            }
        }
        return classOneToMany != null && method.isAnnotationPresent(classOneToMany);

    }

    public static boolean isAnnotationPresentManyToMany(Method method) {
        if (method == null) {
            return false;
        }

        if (classManyToMany == null && !classManyToManyError) {
            try {
                classManyToMany = (Class<? extends Annotation>) Class.forName("javax.persistence.ManyToMany");
            } catch (Throwable e) {
                // skip
                classManyToManyError = true;
            }
        }
        return classManyToMany != null && (method.isAnnotationPresent(classOneToMany) || method.isAnnotationPresent(classManyToMany));

    }

    public static boolean isHibernateInitialized(Object object) {
        if (object == null) {
            return false;
        }
        if (methodHibernateIsInitialized == null && !methodHibernateIsInitializedError) {
            try {
                Class<?> classHibernate = Class.forName("org.hibernate.Hibernate");
                methodHibernateIsInitialized = classHibernate.getMethod("isInitialized", Object.class);
            } catch (Throwable e) {
                // skip
                methodHibernateIsInitializedError = true;
            }
        }
        if (methodHibernateIsInitialized != null) {
            try {
                Boolean initialized = (Boolean) methodHibernateIsInitialized.invoke(null, object);
                return initialized.booleanValue();
            } catch (Throwable e) {
                // skip
            }
        }
        return true;
    }

    public static double parseDouble(String str) {
        final int len = str.length();
        if (len > 10) {
            return Double.parseDouble(str);
        }

        boolean negative = false;

        long longValue = 0;
        int scale = 0;
        for (int i = 0; i < len; ++i) {
            char ch = str.charAt(i);
            if (ch == '-' && i == 0) {
                negative = true;
                continue;
            }

            if (ch == '.') {
                if (scale != 0) {
                    return Double.parseDouble(str);
                }
                scale = len - i - 1;
                continue;
            }

            if (ch >= '0' && ch <= '9') {
                int digit = ch - '0';
                longValue = longValue * 10 + digit;
            } else {
                return Double.parseDouble(str);
            }
        }

        if (negative) {
            longValue = -longValue;
        }

        switch (scale) {
            case 0:
                return (double) longValue;
            case 1:
                return ((double) longValue) / 10;
            case 2:
                return ((double) longValue) / 100;
            case 3:
                return ((double) longValue) / 1000;
            case 4:
                return ((double) longValue) / 10000;
            case 5:
                return ((double) longValue) / 100000;
            case 6:
                return ((double) longValue) / 1000000;
            case 7:
                return ((double) longValue) / 10000000;
            case 8:
                return ((double) longValue) / 100000000;
            case 9:
                return ((double) longValue) / 1000000000;
            default:
                break;
        }

        return Double.parseDouble(str);
    }

    public static float parseFloat(String str) {
        final int len = str.length();
        if (len >= 10) {
            return Float.parseFloat(str);
        }

        boolean negative = false;

        long longValue = 0;
        int scale = 0;
        for (int i = 0; i < len; ++i) {
            char ch = str.charAt(i);
            if (ch == '-' && i == 0) {
                negative = true;
                continue;
            }

            if (ch == '.') {
                if (scale != 0) {
                    return Float.parseFloat(str);
                }
                scale = len - i - 1;
                continue;
            }

            if (ch >= '0' && ch <= '9') {
                int digit = ch - '0';
                longValue = longValue * 10 + digit;
            } else {
                return Float.parseFloat(str);
            }
        }

        if (negative) {
            longValue = -longValue;
        }

        switch (scale) {
            case 0:
                return (float) longValue;
            case 1:
                return ((float) longValue) / 10;
            case 2:
                return ((float) longValue) / 100;
            case 3:
                return ((float) longValue) / 1000;
            case 4:
                return ((float) longValue) / 10000;
            case 5:
                return ((float) longValue) / 100000;
            case 6:
                return ((float) longValue) / 1000000;
            case 7:
                return ((float) longValue) / 10000000;
            case 8:
                return ((float) longValue) / 100000000;
            case 9:
                return ((float) longValue) / 1000000000;
            default:
                break;
        }

        return Float.parseFloat(str);
    }

    public static long fnv1a_64_lower(String key) {
        long hashCode = 0xcbf29ce484222325L;
        for (int i = 0; i < key.length(); ++i) {
            char ch = key.charAt(i);
            if (ch == '_' || ch == '-') {
                continue;
            }
            if (ch >= 'A' && ch <= 'Z') {
                ch = (char) (ch + 32);
            }
            hashCode ^= ch;
            hashCode *= 0x100000001b3L;
        }
        return hashCode;
    }

    public static long fnv1a_64(String key) {
        long hashCode = 0xcbf29ce484222325L;
        for (int i = 0; i < key.length(); ++i) {
            char ch = key.charAt(i);
            hashCode ^= ch;
            hashCode *= 0x100000001b3L;
        }
        return hashCode;
    }

    public static boolean isKotlin(Class clazz) {
        if (kotlin_metadata == null && !kotlin_metadata_error) {
            try {
                kotlin_metadata = Class.forName("kotlin.Metadata");
            } catch (Throwable e) {
                kotlin_metadata_error = true;
            }
        }
        return kotlin_metadata != null && clazz.isAnnotationPresent(kotlin_metadata);
    }

    public static Constructor getKoltinConstructor(Constructor[] constructors) {
        return getKoltinConstructor(constructors, null);
    }

    public static Constructor getKoltinConstructor(Constructor[] constructors, String[] paramNames) {
        Constructor creatorConstructor = null;
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (paramNames != null && parameterTypes.length != paramNames.length) {
                continue;
            }

            if (parameterTypes.length > 0 && parameterTypes[parameterTypes.length - 1].getName().equals("kotlin.jvm.internal.DefaultConstructorMarker")) {
                continue;
            }
            if (creatorConstructor != null && creatorConstructor.getParameterTypes().length >= parameterTypes.length) {
                continue;
            }
            creatorConstructor = constructor;
        }
        return creatorConstructor;
    }

    public static String[] getKoltinConstructorParameters(Class clazz) {
        if (kotlinClassConstructor == null && !kotlinClassKlassError) {
            try {
                Class classKotlinClazz = Class.forName("kotlin.reflect.jvm.internal.KClassImpl");
                kotlinClassConstructor = classKotlinClazz.getConstructor(Class.class);
            } catch (Throwable e) {
                kotlinClassKlassError = true;
            }
        }
        if (kotlinClassConstructor == null) {
            return null;
        }

        if (kotlinClassGetConstructors == null && !kotlinClassKlassError) {
            try {
                Class classKotlinClazz = Class.forName("kotlin.reflect.jvm.internal.KClassImpl");
                kotlinClassGetConstructors = classKotlinClazz.getMethod("getConstructors");
            } catch (Throwable e) {
                kotlinClassKlassError = true;
            }
        }

        if (kotlinFunctionGetParameters == null && !kotlinClassKlassError) {
            try {
                Class classKotlinFunction = Class.forName("kotlin.reflect.KFunction");
                kotlinFunctionGetParameters = classKotlinFunction.getMethod("getParameters");
            } catch (Throwable e) {
                kotlinClassKlassError = true;
            }
        }

        if (kotlinParameterGetName == null && !kotlinClassKlassError) {
            try {
                Class classKotlinParameter = Class.forName("kotlin.reflect.KParameter");
                kotlinParameterGetName = classKotlinParameter.getMethod("getName");
            } catch (Throwable e) {
                kotlinClassKlassError = true;
            }
        }

        if (kotlinError) {
            return null;
        }

        try {
            Object constructor = null;
            Object kclassImpl = kotlinClassConstructor.newInstance(clazz);
            Iterable it = (Iterable) kotlinClassGetConstructors.invoke(kclassImpl);
            for (Iterator iterator = it.iterator(); iterator.hasNext(); iterator.hasNext()) {
                Object item = iterator.next();
                List parameters = (List) kotlinFunctionGetParameters.invoke(item);
                if (constructor != null && parameters.size() == 0) {
                    continue;
                }
                constructor = item;
            }
            List parameters = (List) kotlinFunctionGetParameters.invoke(constructor);
            String[] names = new String[parameters.size()];
            for (int i = 0; i < parameters.size(); i++) {
                Object param = parameters.get(i);
                names[i] = (String) kotlinParameterGetName.invoke(param);
            }
            return names;
        } catch (Throwable e) {
            e.printStackTrace();
            kotlinError = true;
        }
        return null;
    }

    private static boolean isKotlinIgnore(Class clazz, String methodName) {
        if (kotlinIgnores == null && !kotlinIgnoresError) {
            try {
                Map<Class, String[]> map = new HashMap<Class, String[]>();
                Class charRangeClass = Class.forName("kotlin.ranges.CharRange");
                map.put(charRangeClass, new String[]{"getEndInclusive", "isEmpty"});
                Class intRangeClass = Class.forName("kotlin.ranges.IntRange");
                map.put(intRangeClass, new String[]{"getEndInclusive", "isEmpty"});
                Class longRangeClass = Class.forName("kotlin.ranges.LongRange");
                map.put(longRangeClass, new String[]{"getEndInclusive", "isEmpty"});
                Class floatRangeClass = Class.forName("kotlin.ranges.ClosedFloatRange");
                map.put(floatRangeClass, new String[]{"getEndInclusive", "isEmpty"});
                Class doubleRangeClass = Class.forName("kotlin.ranges.ClosedDoubleRange");
                map.put(doubleRangeClass, new String[]{"getEndInclusive", "isEmpty"});
                kotlinIgnores = map;
            } catch (Throwable error) {
                kotlinIgnoresError = true;
            }
        }
        if (kotlinIgnores == null) {
            return false;
        }
        String[] ignores = kotlinIgnores.get(clazz);
        return ignores != null && Arrays.binarySearch(ignores, methodName) >= 0;
    }

    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotationClass) {
        A a = clazz.getAnnotation(annotationClass);
        if (a != null) {
            return a;
        }

        if (clazz.getAnnotations().length > 0) {
            for (Annotation annotation : clazz.getAnnotations()) {
                a = annotation.annotationType().getAnnotation(annotationClass);
                if (a != null) {
                    return a;
                }
            }
        }
        return null;
    }

    public static boolean isJacksonCreator(Method method) {
        if (method == null) {
            return false;
        }

        if (classJacksonCreator == null && !classJacksonCreatorError) {
            try {
                classJacksonCreator = (Class<? extends Annotation>) Class.forName("com.fasterxml.jackson.annotation.JsonCreator");
            } catch (Throwable e) {
                // skip
                classJacksonCreatorError = true;
            }
        }
        return classJacksonCreator != null && method.isAnnotationPresent(classJacksonCreator);
    }
}
