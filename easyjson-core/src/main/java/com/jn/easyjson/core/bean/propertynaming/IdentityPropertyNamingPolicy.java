package com.jn.easyjson.core.bean.propertynaming;

/**
 * @since 3.2.2
 */
public class IdentityPropertyNamingPolicy implements BeanPropertyNamingPolicy {
    @Override
    public String translateName(String p) {
        return p;
    }

    @Override
    public String getName() {
        return "identity";
    }
}
