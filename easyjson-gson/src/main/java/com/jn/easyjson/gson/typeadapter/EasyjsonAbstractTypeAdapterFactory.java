package com.jn.easyjson.gson.typeadapter;

import com.google.gson.TypeAdapterFactory;
import com.jn.easyjson.gson.GsonJSONBuilder;
import com.jn.langx.util.Preconditions;

public abstract class EasyjsonAbstractTypeAdapterFactory implements TypeAdapterFactory {
    protected GsonJSONBuilder jsonBuilder;
    public EasyjsonAbstractTypeAdapterFactory(GsonJSONBuilder jsonBuilder){
        Preconditions.checkNotNull(jsonBuilder);
        this.jsonBuilder = jsonBuilder;
    }
}
