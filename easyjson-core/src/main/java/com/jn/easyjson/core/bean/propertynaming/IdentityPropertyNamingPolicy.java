package com.jn.easyjson.core.bean.propertynaming;

import java.lang.reflect.Member;

/**
 * @since 3.2.2
 */
public class IdentityPropertyNamingPolicy implements BeanPropertyNamingPolicy {
    @Override
    public String translateName(Member member, String property) {
        return property;
    }

    @Override
    public String getName() {
        return "identity";
    }
}
