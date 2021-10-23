package com.jn.easyjson.fastjson.codec;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.MapSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMaps;
import com.jn.langx.util.reflect.Modifiers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.Types;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class MultiValueMapCodec implements ObjectSerializer, ObjectDeserializer, Typed {
    private MapSerializer mapSerializer = MapSerializer.instance;
    private MapDeserializer mapDeserializer = MapDeserializer.instance;
    public static MultiValueMapCodec INSTANCE = new MultiValueMapCodec();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Type[] keyAndValueTypes = Types.getMapKeyAndValueTypes(type, Types.getRawType(type));
        Type delegateMapType = Types.getMapParameterizedType(keyAndValueTypes[0], keyAndValueTypes[1]);
        Map delegate = mapDeserializer.deserialze(parser, delegateMapType, fieldName);
        MultiValueMap dest = createMultiValueMap(type);
        MultiValueMaps.copy(delegate, dest);
        return (T) dest;
    }

    private MultiValueMap createMultiValueMap(Type type) {
        Class rawClass = Types.getRawType(type);
        if (rawClass.isInterface() || Modifiers.isAbstract(rawClass)) {
            return new LinkedMultiValueMap();
        }
        Constructor constructor = Reflects.getConstructor(rawClass);
        if (constructor != null) {
            return Reflects.<MultiValueMap>newInstance(constructor);
        } else {
            return new LinkedMultiValueMap();
        }
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        mapSerializer.write(serializer, object, fieldName, fieldType, features);
    }

    @Override
    public List<Type> applyTo() {
        return Collects.<Type>newArrayList(MultiValueMap.class);
    }
}
