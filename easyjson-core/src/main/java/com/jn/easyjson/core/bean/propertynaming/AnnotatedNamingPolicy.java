package com.jn.easyjson.core.bean.propertynaming;

import java.lang.reflect.Field;

public class AnnotatedNamingPolicy implements BeanPropertyNamingPolicy {
    @Override
    public String translateName(Field field, String fieldName) {
        return null;
    }

    @Override
    public String getName() {
        return "annotated";
    }
}
