package com.jn.easyjson.core.bean.propertynaming;

import com.jn.langx.util.Strings;

/**
 * @since 3.2.2
 */
public class UpperCamelCasePropertyNamingPolicy implements BeanPropertyNamingPolicy {
    @Override
    public String translateName(String property) {
        return Strings.upperCaseFirstLetter(property);
    }

    @Override
    public String getName() {
        return "UpperCamelCase";
    }
}
