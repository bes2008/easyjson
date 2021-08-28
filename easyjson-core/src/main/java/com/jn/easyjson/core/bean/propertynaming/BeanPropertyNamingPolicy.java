package com.jn.easyjson.core.bean.propertynaming;

import com.jn.langx.Named;
import com.jn.langx.annotation.Nullable;

import java.lang.reflect.Member;

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
    String translateName(@Nullable Member member, String property);
}
