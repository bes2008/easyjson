/*
 *  Copyright 2019 the original author or authors.
 *
 *  Licensed under the LGPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

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
package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.spi.Module;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;

import java.io.Closeable;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class ParserConfig {

    public static final String DENY_PROPERTY = "fastjson.parser.deny";
    public static final String AUTOTYPE_ACCEPT = "fastjson.parser.autoTypeAccept";
    public static final String AUTOTYPE_SUPPORT_PROPERTY = "fastjson.parser.autoTypeSupport";

    public static final String[] DENYS;
    private static final String[] AUTO_TYPE_ACCEPT_LIST;
    public static final boolean AUTO_SUPPORT;

    static {
        {
            String property = IOUtils.getStringProperty(DENY_PROPERTY);
            DENYS = splitItemsFormProperty(property);
        }
        {
            String property = IOUtils.getStringProperty(AUTOTYPE_SUPPORT_PROPERTY);
            AUTO_SUPPORT = "true".equals(property);
        }
        {
            String property = IOUtils.getStringProperty(AUTOTYPE_ACCEPT);
            String[] items = splitItemsFormProperty(property);
            if (items == null) {
                items = new String[0];
            }
            AUTO_TYPE_ACCEPT_LIST = items;
        }
    }

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    public static final ParserConfig global = new ParserConfig();

    private final IdentityHashMap<Type, ObjectDeserializer> deserializers = new IdentityHashMap<Type, ObjectDeserializer>();
    private final ConcurrentMap<String, Class<?>> typeMapping = new ConcurrentHashMap<String, Class<?>>(16, 0.75f, 1);

    private boolean asmEnable = false;

    public final SymbolTable symbolTable = new SymbolTable(4096);

    public PropertyNamingStrategy propertyNamingStrategy;

    protected ClassLoader defaultClassLoader;


    private static boolean awtError = false;
    private static boolean jdk8Error = false;
    private static boolean jodaError = false;
    private static boolean guavaError = false;

    private boolean autoTypeSupport = AUTO_SUPPORT;
    private long[] denyHashCodes;
    private long[] acceptHashCodes;


    private boolean jacksonCompatible = false;

    public boolean compatibleWithJavaBean = TypeUtils.compatibleWithJavaBean;
    private List<Module> modules = new ArrayList<Module>();

    {
        denyHashCodes = new long[]{
                -8720046426850100497L,
                -8165637398350707645L,
                -8109300701639721088L,
                -8083514888460375884L,
                -7966123100503199569L,
                -7921218830998286408L,
                -7768608037458185275L,
                -7766605818834748097L,
                -6835437086156813536L,
                -6179589609550493385L,
                -5194641081268104286L,
                -4837536971810737970L,
                -4082057040235125754L,
                -3935185854875733362L,
                -2753427844400776271L,
                -2364987994247679115L,
                -2262244760619952081L,
                -1872417015366588117L,
                -1589194880214235129L,
                -254670111376247151L,
                -190281065685395680L,
                33238344207745342L,
                313864100207897507L,
                1073634739308289776L,
                1203232727967308606L,
                1459860845934817624L,
                1502845958873959152L,
                3547627781654598988L,
                3730752432285826863L,
                3794316665763266033L,
                4147696707147271408L,
                4904007817188630457L,
                5347909877633654828L,
                5450448828334921485L,
                5688200883751798389L,
                5751393439502795295L,
                5944107969236155580L,
                6742705432718011780L,
                7017492163108594270L,
                7179336928365889465L,
                7442624256860549330L,
                8389032537095247355L,
                8409640769019589119L,
                8838294710098435315L
        };

        long[] hashCodes = new long[AUTO_TYPE_ACCEPT_LIST.length + 1];
        for (int i = 0; i < AUTO_TYPE_ACCEPT_LIST.length; i++) {
            hashCodes[i] = TypeUtils.fnv1a_64(AUTO_TYPE_ACCEPT_LIST[i]);
        }
        hashCodes[hashCodes.length - 1] = -6293031534589903644L;

        Arrays.sort(hashCodes);
        acceptHashCodes = hashCodes;
    }

    public ParserConfig() {
        this(false);
    }

    public ParserConfig(boolean fieldBase) {
        this(null, null, fieldBase);
    }

    public ParserConfig(ClassLoader parentClassLoader) {
        this(null, parentClassLoader, false);
    }

    private ParserConfig(Object asmFactory, ClassLoader parentClassLoader, boolean fieldBased) {
        if (asmFactory == null) {
            asmEnable = false;
        }

        initDeserializers();

        addItemsToDeny(DENYS);
        addItemsToAccept(AUTO_TYPE_ACCEPT_LIST);

    }

    private void initDeserializers() {
    }

    private static String[] splitItemsFormProperty(final String property) {
        if (property != null && property.length() > 0) {
            return property.split(",");
        }
        return null;
    }

    public void configFromPropety(Properties properties) {
        {
            String property = properties.getProperty(DENY_PROPERTY);
            String[] items = splitItemsFormProperty(property);
            addItemsToDeny(items);
        }
        {
            String property = properties.getProperty(AUTOTYPE_ACCEPT);
            String[] items = splitItemsFormProperty(property);
            addItemsToAccept(items);
        }
        {
            String property = properties.getProperty(AUTOTYPE_SUPPORT_PROPERTY);
            if ("true".equals(property)) {
                this.autoTypeSupport = true;
            } else if ("false".equals(property)) {
                this.autoTypeSupport = false;
            }
        }
    }

    private void addItemsToDeny(final String[] items) {
        if (items == null) {
            return;
        }

        for (int i = 0; i < items.length; ++i) {
            String item = items[i];
            this.addDeny(item);
        }
    }

    private void addItemsToAccept(final String[] items) {
        if (items == null) {
            return;
        }

        for (int i = 0; i < items.length; ++i) {
            String item = items[i];
            this.addAccept(item);
        }
    }

    public boolean isAutoTypeSupport() {
        return autoTypeSupport;
    }

    public void setAutoTypeSupport(boolean autoTypeSupport) {
        this.autoTypeSupport = autoTypeSupport;
    }

    public boolean isAsmEnable() {
        return asmEnable;
    }

    public void setAsmEnable(boolean asmEnable) {
        this.asmEnable = asmEnable;
    }

    /**
     * @deprecated
     */
    public IdentityHashMap<Type, ObjectDeserializer> getDerializers() {
        return deserializers;
    }

    public IdentityHashMap<Type, ObjectDeserializer> getDeserializers() {
        return deserializers;
    }

    public ObjectDeserializer getDeserializer(Type type) {
        return null;
    }

    public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
        return null;
    }


    public void putDeserializer(Type type, ObjectDeserializer deserializer) {
    }


    /**
     * @deprecated internal method, dont call
     */
    public boolean isPrimitive(Class<?> clazz) {
        return isPrimitive2(clazz);
    }

    /**
     * @deprecated internal method, dont call
     */
    public static boolean isPrimitive2(Class<?> clazz) {
        return clazz.isPrimitive() //
                || clazz == Boolean.class //
                || clazz == Character.class //
                || clazz == Byte.class //
                || clazz == Short.class //
                || clazz == Integer.class //
                || clazz == Long.class //
                || clazz == Float.class //
                || clazz == Double.class //
                || clazz == BigInteger.class //
                || clazz == BigDecimal.class //
                || clazz == String.class //
                || clazz == java.util.Date.class //
                || clazz == java.sql.Date.class //
                || clazz == java.sql.Time.class //
                || clazz == java.sql.Timestamp.class //
                || clazz.isEnum() //
                ;
    }

    /**
     * fieldName,field ，先生成fieldName的快照，减少之后的findField的轮询
     *
     * @param clazz
     * @param fieldCacheMap :map&lt;fieldName ,Field&gt;
     */
    public static void parserAllFieldToCache(Class<?> clazz, Map</**fieldName*/String, Field> fieldCacheMap) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldCacheMap.containsKey(fieldName)) {
                fieldCacheMap.put(fieldName, field);
            }
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            parserAllFieldToCache(clazz.getSuperclass(), fieldCacheMap);
        }
    }

    public static Field getFieldFromCache(String fieldName, Map<String, Field> fieldCacheMap) {
        Field field = fieldCacheMap.get(fieldName);

        if (field == null) {
            field = fieldCacheMap.get("_" + fieldName);
        }

        if (field == null) {
            field = fieldCacheMap.get("m_" + fieldName);
        }

        if (field == null) {
            char c0 = fieldName.charAt(0);
            if (c0 >= 'a' && c0 <= 'z') {
                char[] chars = fieldName.toCharArray();
                chars[0] -= 32; // lower
                String fieldNameX = new String(chars);
                field = fieldCacheMap.get(fieldNameX);
            }

            if (fieldName.length() > 2) {
                char c1 = fieldName.charAt(1);
                if (fieldName.length() > 2
                        && c0 >= 'a' && c0 <= 'z'
                        && c1 >= 'A' && c1 <= 'Z') {
                    for (Map.Entry<String, Field> entry : fieldCacheMap.entrySet()) {
                        if (fieldName.equalsIgnoreCase(entry.getKey())) {
                            field = entry.getValue();
                            break;
                        }
                    }
                }
            }
        }

        return field;
    }

    public ClassLoader getDefaultClassLoader() {
        return defaultClassLoader;
    }

    public void setDefaultClassLoader(ClassLoader defaultClassLoader) {
        this.defaultClassLoader = defaultClassLoader;
    }

    public void addDeny(String name) {
        if (name == null || name.length() == 0) {
            return;
        }

        long hash = TypeUtils.fnv1a_64(name);
        if (Arrays.binarySearch(this.denyHashCodes, hash) >= 0) {
            return;
        }

        long[] hashCodes = new long[this.denyHashCodes.length + 1];
        hashCodes[hashCodes.length - 1] = hash;
        System.arraycopy(this.denyHashCodes, 0, hashCodes, 0, this.denyHashCodes.length);
        Arrays.sort(hashCodes);
        this.denyHashCodes = hashCodes;
    }

    public void addAccept(String name) {
        if (name == null || name.length() == 0) {
            return;
        }

        long hash = TypeUtils.fnv1a_64(name);
        if (Arrays.binarySearch(this.acceptHashCodes, hash) >= 0) {
            return;
        }

        long[] hashCodes = new long[this.acceptHashCodes.length + 1];
        hashCodes[hashCodes.length - 1] = hash;
        System.arraycopy(this.acceptHashCodes, 0, hashCodes, 0, this.acceptHashCodes.length);
        Arrays.sort(hashCodes);
        this.acceptHashCodes = hashCodes;
    }

    public Class<?> checkAutoType(Class type) {
        if (deserializers.get(type) != null) {
            return type;
        }

        return checkAutoType(type.getName(), null, JSON.DEFAULT_PARSER_FEATURE);
    }

    public Class<?> checkAutoType(String typeName, Class<?> expectClass) {
        return checkAutoType(typeName, expectClass, JSON.DEFAULT_PARSER_FEATURE);
    }

    public Class<?> checkAutoType(String typeName, Class<?> expectClass, int features) {
        if (typeName == null) {
            return null;
        }

        if (typeName.length() >= 192 || typeName.length() < 3) {
            throw new JSONException("autoType is not support. " + typeName);
        }

        final boolean expectClassFlag;
        if (expectClass == null) {
            expectClassFlag = false;
        } else {
            if (expectClass == Object.class
                    || expectClass == Serializable.class
                    || expectClass == Cloneable.class
                    || expectClass == Closeable.class
                    || expectClass == EventListener.class
                    || expectClass == Iterable.class
                    || expectClass == Collection.class
                    ) {
                expectClassFlag = false;
            } else {
                expectClassFlag = true;
            }
        }

        String className = typeName.replace('$', '.');
        Class<?> clazz = null;

        final long BASIC = 0xcbf29ce484222325L;
        final long PRIME = 0x100000001b3L;

        final long h1 = (BASIC ^ className.charAt(0)) * PRIME;
        if (h1 == 0xaf64164c86024f1aL) { // [
            throw new JSONException("autoType is not support. " + typeName);
        }

        if ((h1 ^ className.charAt(className.length() - 1)) * PRIME == 0x9198507b5af98f0L) {
            throw new JSONException("autoType is not support. " + typeName);
        }

        final long h3 = (((((BASIC ^ className.charAt(0))
                * PRIME)
                ^ className.charAt(1))
                * PRIME)
                ^ className.charAt(2))
                * PRIME;

        if (autoTypeSupport || expectClassFlag) {
            long hash = h3;
            for (int i = 3; i < className.length(); ++i) {
                hash ^= className.charAt(i);
                hash *= PRIME;
                if (Arrays.binarySearch(acceptHashCodes, hash) >= 0) {
                    clazz = TypeUtils.loadClass(typeName, defaultClassLoader, true);
                    if (clazz != null) {
                        return clazz;
                    }
                }
                if (Arrays.binarySearch(denyHashCodes, hash) >= 0 && TypeUtils.getClassFromMapping(typeName) == null) {
                    throw new JSONException("autoType is not support. " + typeName);
                }
            }
        }

        if (clazz == null) {
            clazz = TypeUtils.getClassFromMapping(typeName);
        }

        return clazz;
    }

    public void clearDeserializers() {
        this.deserializers.clear();
        this.initDeserializers();
    }

    public boolean isJacksonCompatible() {
        return jacksonCompatible;
    }

    public void setJacksonCompatible(boolean jacksonCompatible) {
        this.jacksonCompatible = jacksonCompatible;
    }

    public void register(String typeName, Class type) {
        typeMapping.putIfAbsent(typeName, type);
    }

    public void register(Module module) {
        this.modules.add(module);
    }
}
