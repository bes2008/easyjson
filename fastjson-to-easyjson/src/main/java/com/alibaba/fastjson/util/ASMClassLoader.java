/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.fastjson.util;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.parser.deserializer.*;
import com.alibaba.fastjson.serializer.*;

import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

public class ASMClassLoader extends ClassLoader {

    private static java.security.ProtectionDomain DOMAIN;

    private static Map<String, Class<?>> classMapping = new HashMap<String, Class<?>>();

    static {
        DOMAIN = (java.security.ProtectionDomain) java.security.AccessController.doPrivileged(new PrivilegedAction<Object>() {

            public Object run() {
                return ASMClassLoader.class.getProtectionDomain();
            }
        });

        Class<?>[] jsonClasses = new Class<?>[]{JSON.class,
                JSONObject.class,
                JSONArray.class,
                JSONPath.class,
                JSONAware.class,
                JSONException.class,
                JSONPathException.class,
                JSONReader.class,
                JSONStreamAware.class,
                JSONWriter.class,
                TypeReference.class,

                FieldInfo.class,
                TypeUtils.class,
                IOUtils.class,
                IdentityHashMap.class,
                ParameterizedTypeImpl.class,
                JavaBeanInfo.class,

                ObjectSerializer.class,
                JavaBeanSerializer.class,
                SerializeFilterable.class,
                SerializeBeanInfo.class,
                JSONSerializer.class,
                SerializeWriter.class,
                SerializeFilter.class,
                Labels.class,
                LabelFilter.class,
                ContextValueFilter.class,
                AfterFilter.class,
                BeforeFilter.class,
                NameFilter.class,
                PropertyFilter.class,
                PropertyPreFilter.class,
                ValueFilter.class,
                SerializerFeature.class,
                ContextObjectSerializer.class,
                SerialContext.class,
                SerializeConfig.class,

                JavaBeanDeserializer.class,
                ParserConfig.class,
                DefaultJSONParser.class,
                JSONLexer.class,
                JSONLexerBase.class,
                ParseContext.class,
                JSONToken.class,
                SymbolTable.class,
                Feature.class,
                JSONScanner.class,
                JSONReaderScanner.class,

                AutowiredObjectDeserializer.class,
                ObjectDeserializer.class,
                ExtraProcessor.class,
                ExtraProcessable.class,
                ExtraTypeProvider.class,
                BeanContext.class,
                FieldDeserializer.class,
                DefaultFieldDeserializer.class,
        };

        for (Class<?> clazz : jsonClasses) {
            classMapping.put(clazz.getName(), clazz);
        }
    }

    public ASMClassLoader() {
        super(getParentClassLoader());
    }

    public ASMClassLoader(ClassLoader parent) {
        super(parent);
    }

    static ClassLoader getParentClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            try {
                contextClassLoader.loadClass(JSON.class.getName());
                return contextClassLoader;
            } catch (ClassNotFoundException e) {
                // skip
            }
        }
        return JSON.class.getClassLoader();
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> mappingClass = classMapping.get(name);
        if (mappingClass != null) {
            return mappingClass;
        }

        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
            throw e;
        }
    }

    public Class<?> defineClassPublic(String name, byte[] b, int off, int len) throws ClassFormatError {
        Class<?> clazz = defineClass(name, b, off, len, DOMAIN);

        return clazz;
    }

    public boolean isExternalClass(Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();

        if (classLoader == null) {
            return false;
        }

        ClassLoader current = this;
        while (current != null) {
            if (current == classLoader) {
                return false;
            }

            current = current.getParent();
        }

        return true;
    }

}
