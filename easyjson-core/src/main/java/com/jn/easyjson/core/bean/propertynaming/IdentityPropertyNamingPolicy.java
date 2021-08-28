package com.jn.easyjson.core.bean.propertynaming;

import java.lang.reflect.Field;

/**
 * @since 3.2.2
 */
public class IdentityPropertyNamingPolicy implements BeanPropertyNamingPolicy {
    @Override
    public String translateName(Field field,String fieldName) {
        return fieldName;
    }

    @Override
    public String getName() {
        return "identity";
    }
}
