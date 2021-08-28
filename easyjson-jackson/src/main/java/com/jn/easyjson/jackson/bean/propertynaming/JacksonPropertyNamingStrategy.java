package com.jn.easyjson.jackson.bean.propertynaming;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.jn.easyjson.core.bean.propertynaming.BeanPropertyNamingPolicy;

import java.lang.reflect.Member;

/**
 * @since 3.2.2
 */
public class JacksonPropertyNamingStrategy extends PropertyNamingStrategy {
    private BeanPropertyNamingPolicy delegate;

    public JacksonPropertyNamingStrategy(BeanPropertyNamingPolicy delegate) {
        this.delegate = delegate;
    }

    @Override
    public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
        return translate(field.getAnnotated(), defaultName);
    }

    @Override
    public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return translate(method.getAnnotated(), defaultName);
    }

    @Override
    public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return translate(method.getAnnotated(), defaultName);
    }

    @Override
    public String nameForConstructorParameter(MapperConfig<?> config, AnnotatedParameter ctorParam, String defaultName) {
        return defaultName;
    }

    public String translate(Member member, String propertyName) {
        return delegate.translateName(member, propertyName);
    }
}
