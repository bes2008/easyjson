package com.jn.easyjson.gson.typeadapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.jn.easyjson.gson.GsonJSONBuilder;
import com.jn.langx.util.collection.Collects;

import java.util.List;

public class BytesTypeAdapterFactory extends EasyjsonAbstractTypeAdapterFactory{

    private static final List supportedClasses = Collects.newArrayList(byte[].class, Byte[].class);

    public BytesTypeAdapterFactory(GsonJSONBuilder jsonBuilder){
        super(jsonBuilder);
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

}
