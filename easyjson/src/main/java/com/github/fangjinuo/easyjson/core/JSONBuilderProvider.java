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

    public static JSONBuilder createIfExistsGson(){
        return createIfExists("com.github.fangjinuo.easyjson.gson.GsonJSONBuilder");
    }

    public static JSONBuilder createIfExists(String jsonBuilderImplClazz){
        if(hasClass(jsonBuilderImplClazz)){
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
