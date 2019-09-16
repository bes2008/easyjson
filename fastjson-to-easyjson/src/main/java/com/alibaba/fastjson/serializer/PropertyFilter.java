package com.alibaba.fastjson.serializer;

public interface PropertyFilter extends SerializeFilter {

    /**
     * @param object the owner of the property
     * @param name the name of the property
     * @param value the value of the property
     * @return true if the property will be included, false if to be filtered out
     */
    boolean apply(Object object, String name, Object value);
}