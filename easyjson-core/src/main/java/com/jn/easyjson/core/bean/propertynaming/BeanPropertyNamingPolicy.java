package com.jn.easyjson.core.bean.propertynaming;

import com.jn.langx.Named;

/**
 * @since 3.2.2
 */
public interface BeanPropertyNamingPolicy extends Named {

    /**
     * Translates the field name into its JSON field name representation.
     *
     * @param property the field object that we are translating
     * @return the translated field name.
     * @since 3.2.0
     */
    String translateName(String property);
}
