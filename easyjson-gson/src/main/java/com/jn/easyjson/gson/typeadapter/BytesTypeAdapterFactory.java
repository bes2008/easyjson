package com.jn.easyjson.gson.typeadapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.jn.easyjson.core.JSONBuilderAware;
import com.jn.easyjson.gson.GsonJSONBuilder;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;

import java.util.List;

public class BytesTypeAdapterFactory implements TypeAdapterFactory, JSONBuilderAware<GsonJSONBuilder> {
    private GsonJSONBuilder jsonBuilder;

    private static final List supportedClasses = Collects.newArrayList(byte[].class, Byte[].class);
    public BytesTypeAdapterFactory(){}

    public BytesTypeAdapterFactory(GsonJSONBuilder jsonBuilder){
        setJSONBuilder(jsonBuilder);
    }
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (jsonBuilder != null && jsonBuilder.serializeBytesAsBase64String()) {
            if (supportedClasses.contains(type.getRawType())) {
                BytesTypeAdapter adapter = new BytesTypeAdapter();
                adapter.setJSONBuilder(jsonBuilder);
                return adapter;
            }
        }
        return null;
    }

    @Override
    public void setJSONBuilder(GsonJSONBuilder jsonBuilder) {
        Preconditions.checkNotNull(jsonBuilder);
        this.jsonBuilder = jsonBuilder;
    }

    @Override
    public GsonJSONBuilder getJSONBuilder() {
        return jsonBuilder;
    }
}
