package com.jn.easyjson.jackson.bean.propertynaming;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.jn.easyjson.core.bean.propertynaming.BeanPropertyNamingPolicy;

/**
 * @since 3.2.2
 */
public class JacksonPropertyNamingStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase {
    private BeanPropertyNamingPolicy delegate;

    public JacksonPropertyNamingStrategy(BeanPropertyNamingPolicy delegate) {
        this.delegate = delegate;
    }

    @Override
    public String translate(String propertyName) {
        return delegate.translateName(null,propertyName);
    }
}
