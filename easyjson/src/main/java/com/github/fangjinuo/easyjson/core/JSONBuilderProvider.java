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

package com.github.fangjinuo.easyjson.core;

public class JSONBuilderProvider {
    private static Class<JSONBuilder> jsonBuilderClass;

    private static final String GSON_CLASS = "com.google.gson.Gson";
    private static final String JACKSON_CLASS = "com.fasterxml.jackson.databind.ObjectMapper";
    private static final String FASTJSON_CLASS = "com.alibaba.fastjson.JSON";

    static {
        findJSONBuilderImplClass();
    }

    public static JSONBuilder create() {
        if (jsonBuilderClass != null) {
            try {
                return jsonBuilderClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Can't find any supported JSON libraries : [gson, jackson, fastjson]");
    }

    public static JSONBuilder createIfExistsGson() {
        return createIfExists("com.github.fangjinuo.easyjson.gson.GsonJSONBuilder");
    }

    public static JSONBuilder createIfExists(String jsonBuilderImplClazz) {
        if (hasClass(jsonBuilderImplClazz)) {
            Class<JSONBuilder> builderClass = loadClass(jsonBuilderImplClazz);
            try {
                return builderClass.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static void findJSONBuilderImplClass() {
        if (hasClass(GSON_CLASS)) {
            jsonBuilderClass = loadClass("com.github.fangjinuo.easyjson.gson.GsonJSONBuilder");
            if (isJSONBuilderImplClass(jsonBuilderClass)) {
                return;
            }
            jsonBuilderClass = null;
        }
        if (hasClass(JACKSON_CLASS)) {
            // TODO jackson
        }
        if (hasClass(FASTJSON_CLASS)) {
            // TODO fastjson
        }
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
