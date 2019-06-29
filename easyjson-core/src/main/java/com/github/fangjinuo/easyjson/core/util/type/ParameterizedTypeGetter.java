package com.github.fangjinuo.easyjson.core.util.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Usage:
 *
 * <pre>
 *  List:
 *  ParameterizedType type0 = new ParameterizedTypeGetter<List<Person>>(){}.getType();
 *  ParameterizedType type1 = Types.getParameterizedType(List.class, Person);
 *  ParameterizedType type2 = new ParameterizedType(null, List.class, Person);
 *
 *  type0 equivalent to type1
 *  type0 equivalent to type2
 *
 * </pre>
 * @param <T>
 */
public class ParameterizedTypeGetter <T> {
    public Type getType(){
        ParameterizedType genericSuperclass = (ParameterizedType)getClass().getGenericSuperclass();
        return genericSuperclass.getActualTypeArguments()[0];
    }
}
