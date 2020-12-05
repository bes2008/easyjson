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

package com.jn.easyjson.core;

import com.jn.easyjson.core.annotation.DependOn;
import com.jn.easyjson.core.codec.dialect.DialectIdentify;
import com.jn.langx.annotation.Name;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

public class JSONBuilderProvider {
    private static final Logger logger = LoggerFactory.getLogger(JSONBuilderProvider.class);
    private static Class<? extends JSONBuilder> defaultJsonBuilderClass;
    /**
     * key: @Name of the JSON Builder Implement class
     * value: the json builder class
     */
    private static final Map<String, Class<? extends JSONBuilder>> registry = new HashMap<String, Class<? extends JSONBuilder>>();

    static {
        findJSONBuilderImplClasses();
    }

    /**
     * 基于指定的JSON Builder name 创建
     */
    public static JSONBuilder create(@Nullable String name) {
        if (name == null) {
            return create();
        }
        Class<? extends JSONBuilder> clazz = registry.get(name);
        if (clazz == null) {
            logger.warn("Can't find the JSONBuilder: {}", name);
            return null;
        }
        return create(clazz);
    }


    /**
     * 当处于一个JSON，要适配到其他的JSON库时，可以调用这个方法
     *
     * @param name the caller json library name
     */
    public static JSONBuilder adapter(final String name) {
        String found = Collects.findFirst(registry.keySet(), new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return !Objs.equals(value, name);
            }
        });
        if (Emptys.isNotEmpty(found)) {
            return create(found);
        }
        logger.warn("Can't find a suitable JSONBuilder, current json library: {}", name);
        return null;
    }


    public static JSONBuilder create() {
        return create(defaultJsonBuilderClass);
    }

    public static DialectIdentify getDefaultDialectIdentify() {
        try {
            return getDefaultDialectIdentifyOrException();
        } catch (Throwable ex) {
            return null;
        }
    }

    public static DialectIdentify getDefaultDialectIdentifyOrException() {
        return create().dialectIdentify();
    }

    public static JSON simplest() {
        return create().enableIgnoreAnnotation().serializeNulls(true).build();
    }

    private static JSONBuilder create(Class<? extends JSONBuilder> jsonBuilderClass) {
        if (jsonBuilderClass != null) {
            try {
                return jsonBuilderClass.newInstance();
            } catch (Throwable e) {
                logger.error("Can't create a default json builder, {}", jsonBuilderClass.getCanonicalName());
            }
        }
        throw new RuntimeException("Can't find any supported JSON libraries : [gson, jackson, fastjson], \n 1) check you classpath has one of these jar pairs: [fastjson, easyjson-fastjson], [gson, easyjson-gson], [jackson, easyjson-jackson]. \n 2) if any pair found in your classpath, check the jdk version whether is compatible or not");
    }

    private static void findJSONBuilderImplClasses() {
        ServiceLoader<JSONBuilder> loader = ServiceLoader.load(JSONBuilder.class);
        Iterator<JSONBuilder> iter = loader.iterator();
        while (iter.hasNext()) {
            try {
                JSONBuilder jsonBuilder = iter.next();
                Class<? extends JSONBuilder> jsonBuildClass = jsonBuilder.getClass();
                String name = parseName(jsonBuildClass);
                String dependency = parseDependencyClass(jsonBuildClass);
                if (Emptys.isEmpty(dependency)) {
                    logger.warn("Won't register json builder {}, because of can't find its dependency class {}", jsonBuildClass.getCanonicalName(), "NULL");
                    continue;
                }
                if (hasClass(dependency)) {
                    logger.info("Register a json builder {} for {}", jsonBuildClass.getCanonicalName(), name);
                    registry.put(name, jsonBuildClass);
                    if (defaultJsonBuilderClass == null) {
                        defaultJsonBuilderClass = jsonBuildClass;
                    }
                } else {
                    logger.warn("Won't register json builder {}, because of can't find its dependency class {}", jsonBuildClass.getCanonicalName(), dependency);
                }
            } catch (Throwable ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    private static String parseDependencyClass(Class<? extends JSONBuilder> jsonBuilderClass) {
        DependOn dependOn = (DependOn) Reflects.getAnnotation(jsonBuilderClass, DependOn.class);
        if (dependOn != null && Emptys.isNotEmpty(dependOn.value())) {
            return dependOn.value().trim();
        }
        return null;
    }

    private static String parseName(Class<? extends JSONBuilder> clazz) {
        Name nameAnno = Reflects.getAnnotation(clazz, Name.class);
        String name;
        if (nameAnno != null && Strings.isNotBlank(nameAnno.value())) {
            return nameAnno.value().trim();
        }
        name = clazz.getName();
        String name2 = name.replaceFirst("jsonbuilder", "");
        if (Strings.isNotBlank(name2)) {
            return name2;
        }
        return name;
    }

    @SuppressWarnings("rawtypes")
    private static Class loadClass(String clazz) {
        try {
            return ClassLoaders.loadClass(clazz);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static boolean hasClass(String clazz) {
        if (clazz == null) {
            return false;
        }
        return loadClass(clazz) != null;
    }
}
