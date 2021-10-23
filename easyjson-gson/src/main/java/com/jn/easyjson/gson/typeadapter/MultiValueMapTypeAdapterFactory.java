package com.jn.easyjson.gson.typeadapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.reflect.type.Types;

import java.lang.reflect.Type;
import java.util.Map;

public class MultiValueMapTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();
        if (!MultiValueMap.class.isAssignableFrom(rawType)) {
            return null;
        }

        Type type = typeToken.getType();
        Class<?> rawTypeOfSrc = Types.getRawType(type);
        Type[] keyAndValueTypes = Types.getMapKeyAndValueTypes(type, rawTypeOfSrc);

        TypeToken delegateTypeToken = TypeToken.getParameterized(Map.class, keyAndValueTypes);
        TypeAdapter delegateTypeAdapter = gson.getAdapter(delegateTypeToken);


        return new MultiValueMapTypeAdapter(delegateTypeAdapter, typeToken);
    }

}
