package com.github.fangjinuo.easyjson.core.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author jinuo.fang
 */
public class Types {

    public static ParameterizedType getListParameterizedType(Type elementType){
        return getParameterizedType(List.class, elementType);
    }

    public static ParameterizedType getMapParameterizedType(Type keyType, Type valueType){
        return getParameterizedType(Map.class, keyType, valueType);
    }

    public static ParameterizedType getParameterizedType(Type rawType){
        return new ParameterizedTypeImpl(null, rawType);
    }

    public static ParameterizedType getParameterizedType(Type rawType, Type... typeArguments){
        return getParameterizedTypeWithOwnerType(null, rawType, typeArguments);
    }

    public static ParameterizedType getParameterizedTypeWithOwnerType(Type ownerType, Type rawType, Type... typeArguments){
        return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
    }

}
