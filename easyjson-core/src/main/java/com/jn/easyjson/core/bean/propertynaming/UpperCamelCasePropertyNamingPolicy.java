package com.jn.easyjson.core.bean.propertynaming;

import com.jn.langx.util.Strings;

import java.lang.reflect.Field;

/**
 * @since 3.2.2
 */
public class UpperCamelCasePropertyNamingPolicy implements BeanPropertyNamingPolicy {
    @Override
    public String translateName(Field field, String fieldName) {
        return Strings.upperCaseFirstLetter(fieldName);
    }

    @Override
    public String getName() {
        return "UpperCamelCase";
    }
}
