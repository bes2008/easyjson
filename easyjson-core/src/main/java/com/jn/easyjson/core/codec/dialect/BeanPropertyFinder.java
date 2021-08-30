package com.jn.easyjson.core.codec.dialect;

import com.jn.langx.util.function.Function2;

public interface BeanPropertyFinder extends Function2<Class, String, String> {
    @Override
    String apply(Class declaringClass, String propertyName);
}
