package com.github.fangjinuo.easyjson.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
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

    public static boolean getBooleanAttr(DeserializationContext ctx, String key){
        if(ctx ==null || key ==null){
            return false;
        }
        return getBoolean(ctx.getAttribute(key));
    }

    public static boolean getBoolean(Object obj){
        if(obj==null){
            return false;
        }
        return obj.toString().toLowerCase().equals("true");
    }
}
