package com.github.fangjinuo.easyjson.jackson;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.reflect.Type;

public class Jacksons {
    public static boolean isJacksonJavaType(Type type){
        try {
            JavaType jType = (JavaType)type;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static JavaType toJavaType(Type type){
        return (JavaType)type;
    }
}
