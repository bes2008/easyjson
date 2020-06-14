package com.jn.easyjson.core.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Chars;
import com.jn.langx.util.Strings;
import com.jn.langx.util.reflect.Modifiers;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Members {

    public static String extractFieldName(Method method){
        if(isGetterOrSetter(method)){
            String methodName = method.getName();
            String fieldName = null;
            if(methodName.startsWith("set") || methodName.startsWith("get")){
                fieldName = methodName.substring(3);
            }
            if(methodName.startsWith("is")){
                fieldName = methodName.substring(2);
            }
            return Chars.toLowerCase(fieldName.charAt(0)) + (fieldName.length()>1 ? fieldName.substring(1) :"");
        }
        return null;
    }

    public static boolean isSetter(@NonNull Method method) {
        if(isGetterOrSetter(method)){
            String methodName = method.getName();
            if(methodName.startsWith("set")){
                return true;
            }
        }
        return false;
    }

    public static boolean isGetter(@NonNull Method method) {
        if(isGetterOrSetter(method)){
            String methodName = method.getName();
            if(methodName.startsWith("get") || methodName.startsWith("is")){
                return true;
            }
        }
        return false;
    }

    public static boolean isGetterOrSetter(@NonNull Method method) {
        if (method == null) {
            return false;
        }
        if (!Modifiers.isPublic(method) || Modifiers.isStatic(method) || Modifiers.isAbstract(method)) {
            return false;
        }

        String methodName = method.getName();
        String fieldName = null;
        if (methodName.startsWith("set")) {
            if (method.getParameterTypes().length != 1) {
                return false;
            }
            fieldName = methodName.substring(3);
        } else if (methodName.startsWith("get")) {
            if (method.getParameterTypes().length != 0) {
                return false;
            }
            fieldName = methodName.substring(3);
        } else if (methodName.startsWith("is")) {
            if (method.getParameterTypes().length != 0) {
                return false;
            }
            fieldName = methodName.substring(2);
        }
        if (Strings.isEmpty(fieldName)) {
            return false;
        }
        fieldName = fieldName.substring(0, 1).toLowerCase() + (fieldName.length() <= 1 ? "" : fieldName.substring(1));
        Class beanClass = method.getDeclaringClass();
        Field field = Reflects.getAnyField(beanClass, fieldName);
        if (field == null) {
            return false;
        }
        return true;
    }
}
