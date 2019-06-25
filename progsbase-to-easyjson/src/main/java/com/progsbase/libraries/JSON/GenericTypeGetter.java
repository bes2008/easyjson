package com.progsbase.libraries.JSON;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericTypeGetter <T> {
    public Type getType(){
        ParameterizedType genericSuperclass = (ParameterizedType)getClass().getGenericSuperclass();
        return genericSuperclass.getActualTypeArguments()[0];
    }
}
