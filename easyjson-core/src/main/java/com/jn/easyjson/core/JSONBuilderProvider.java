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
import com.jn.easyjson.core.annotation.Name;
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
    private static final Map<String, Class<? extends JSONBuilder>> registry = new HashMap<String, Class<? extends JSONBuilder>>();

    static {
        findJSONBuilderImplClasses();
    }

    public static JSONBuilder create(String name) {
        if (name == null) {
            return create();
        }
        Class<? extends JSONBuilder> clazz = registry.get(name);
        return create(clazz);
    }

    public static JSONBuilder create() {
        return create(defaultJsonBuilderClass);
    }

    public static JSON simplest() {
        return create().serializeNulls(true).build();
    }

    private static JSONBuilder create(Class<? extends JSONBuilder> defaultJsonBuilderClass) {
        if (defaultJsonBuilderClass != null) {
            try {
                return defaultJsonBuilderClass.newInstance();
            } catch (Throwable e) {
                logger.error("Can't create a default json builder, {}", defaultJsonBuilderClass.getCanonicalName());
            }
        }
        throw new RuntimeException("Can't find any supported JSON libraries : [gson, jackson, fastjson], check you classpath has one of these jar pairs: [fastjson, easyjson-fastjson], [gson, easyjson-gson], [jackson, easyjson-jackson]");
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
                if (dependency == null || dependency.trim().isEmpty()) {
                    logger.warn("Won't registry json builder {}, because of can't find its dependency class {}", jsonBuildClass.getCanonicalName(), "NULL");
                    continue;
                }
                if (hasClass(dependency)) {
                    logger.info("Registry a json builder {} for {}", jsonBuildClass.getCanonicalName(), name);
                    registry.put(name, jsonBuildClass);
                    if (defaultJsonBuilderClass == null) {
                        defaultJsonBuilderClass = jsonBuildClass;
                    }
                } else {
                    logger.warn("Won't registry json builder {}, because of can't find its dependency class {}", jsonBuildClass.getCanonicalName(), dependency);
                }
            } catch (Throwable ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    private static String parseDependencyClass(Class<? extends JSONBuilder> jsonBuilderClass) {
        DependOn dependOn = (DependOn) Reflects.getAnnotation(jsonBuilderClass, DependOn.class);
        String dependency = null;
        if (dependOn != null && dependOn.value() != null && !dependOn.value().trim().isEmpty()) {
            return dependOn.value().trim();
        }
        return null;
    }

    private static String parseName(Class<? extends JSONBuilder> clazz) {
        Name nameAnno = (Name) Reflects.getAnnotation(clazz, Name.class);
        String name = null;
        if (nameAnno != null && nameAnno.value() != null && !nameAnno.value().trim().isEmpty()) {
            return nameAnno.value().trim();
        }
        name = clazz.getName();
        String name2 = name.replaceFirst("jsonbuilder", "");
        if (!name2.trim().isEmpty()) {
            return name2;
        }
        return name;
    }

    private static Class loadClass(String clazz) {
        try {
            return Class.forName(clazz);
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
