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

package com.github.fangjinuo.easyjson.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

public class JSONBuilderProvider {

    private static Class<? extends JSONBuilder> defaultJsonBuilderClass;
    private static final Map<String, Class<? extends JSONBuilder>> registry=new HashMap<String, Class<? extends JSONBuilder>>();

    private static final String GSON_CLASS = "com.google.gson.Gson";
    private static final String JACKSON_CLASS = "com.fasterxml.jackson.databind.ObjectMapper";
    private static final String FASTJSON_CLASS = "com.alibaba.fastjson.JSON";


    static {
        findJSONBuilderImplClasses();
    }

    public static JSONBuilder create() {
        if (defaultJsonBuilderClass != null) {
            try {
                return defaultJsonBuilderClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Can't find any supported JSON libraries : [gson, jackson, fastjson]");
    }

    private static void findJSONBuilderImplClasses() {
        if (defaultJsonBuilderClass == null) {
            defaultJsonBuilderClass = loadJSONBuilderImplClassIfExist(GSON_CLASS, "com.github.fangjinuo.easyjson.gson.GsonJSONBuilder");
        }
        if (defaultJsonBuilderClass == null) {
            defaultJsonBuilderClass = loadJSONBuilderImplClassIfExist(JACKSON_CLASS, "com.github.fangjinuo.easyjson.jackson.JacksonJSONBuilder");
        }
        if (defaultJsonBuilderClass == null) {
            defaultJsonBuilderClass = loadJSONBuilderImplClassIfExist(FASTJSON_CLASS, "com.github.fangjinuo.easyjson.fastjson.FastJsonJSONBuilder");
        }
        if (defaultJsonBuilderClass == null) {
            ServiceLoader<JSONBuilder> loader = ServiceLoader.load(JSONBuilder.class);
            Iterator<JSONBuilder> iter = loader.iterator();
            while (defaultJsonBuilderClass == null && iter.hasNext()) {
                iter.next();
            }
        }
    }

    private static Class<? extends JSONBuilder> loadJSONBuilderImplClassIfExist(String dependencyClass, String clazz) {
        if (hasClass(dependencyClass)) {
            Class<? extends JSONBuilder> jsonBuilderClass = loadClass(clazz);
            if (isJSONBuilderImplClass(jsonBuilderClass)) {
                return jsonBuilderClass;
            }
        }
        return null;
    }

    private static boolean isJSONBuilderImplClass(Class jsonBuilderClass) {
        if (jsonBuilderClass == null) {
            return false;
        }
        return JSONBuilder.class.isAssignableFrom(jsonBuilderClass) && JSONBuilder.class != jsonBuilderClass;
    }

    private static Class loadClass(String clazz) {
        try {
            return Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static boolean hasClass(String clazz) {
        return loadClass(clazz) != null;
    }
}
