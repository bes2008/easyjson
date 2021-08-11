package com.jn.easyjson.gson.bean.fieldnaming;

import com.google.gson.FieldNamingStrategy;
import com.jn.easyjson.core.bean.propertynaming.BeanPropertyNamingPolicy;

import java.lang.reflect.Field;

public class GsonFieldNamingStrategyAdapter implements FieldNamingStrategy {
    private BeanPropertyNamingPolicy delegate;

    public GsonFieldNamingStrategyAdapter(BeanPropertyNamingPolicy policy) {
        this.delegate = policy;
    }

    @Override
    public String translateName(Field f) {
        return this.delegate.translateName(f.getName());
    }
}
