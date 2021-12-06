package com.jn.easyjson.gson.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMaps;
import com.jn.langx.util.reflect.Modifiers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.Types;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MultiValueMapTypeAdapter<K, V> extends TypeAdapter<MultiValueMap<K, V>> {
    private final TypeAdapter mapAdapter;
    private final TypeToken typeToken;

    public MultiValueMapTypeAdapter(TypeAdapter<K> mapAdapter, TypeToken typeToken) {
        this.mapAdapter = mapAdapter;
        this.typeToken = typeToken;
    }

    @Override
    public void write(JsonWriter out, MultiValueMap<K, V> multiValueMap) throws IOException {
        Map<K, List<V>> map = multiValueMap.toMap();
        mapAdapter.write(out, map);
    }

    @Override
    public MultiValueMap<K, V> read(JsonReader in) throws IOException {
        Map<K, Collection<V>> delegate = (Map<K, Collection<V>>) mapAdapter.read(in);
        final MultiValueMap<K, V> multiValueMap = createMultiValueMap();
        MultiValueMaps.copy(delegate, multiValueMap);
        return multiValueMap;
    }



    private MultiValueMap<K, V> createMultiValueMap() {
        Type type = typeToken.getType();
        Class<?> rawTypeOfSrc = Types.getRawType(type);

        if (rawTypeOfSrc.isInterface() || Modifiers.isAbstract(rawTypeOfSrc)) {
            return new LinkedMultiValueMap<K, V>();
        }
        Constructor constructor = Reflects.getConstructor(rawTypeOfSrc);
        if (constructor != null) {
            return Reflects.<MultiValueMap>newInstance(constructor);
        } else {
            return new LinkedMultiValueMap<K, V>();
        }
    }
}
