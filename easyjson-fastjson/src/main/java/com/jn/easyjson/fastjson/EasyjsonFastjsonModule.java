package com.jn.easyjson.fastjson;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.spi.Module;
import com.alibaba.fastjson.util.TypeUtils;
import com.jn.easyjson.core.util.JSONs;
import com.jn.easyjson.fastjson.codec.MultiValueMapCodec;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.Map;

public class EasyjsonFastjsonModule implements Module {
    @Override
    public ObjectDeserializer createDeserializer(ParserConfig config, Class type) {
        ObjectDeserializer deserializer = null;
        if(type.isEnum()) {
            deserializer = createEnumDeserializer(config, type);
        }else if(Reflects.isSubClassOrEquals(MultiValueMap.class, type)){
            config.putDeserializer(type, MultiValueMapCodec.INSTANCE);
            deserializer = MultiValueMapCodec.INSTANCE;
        }
        return deserializer;
    }

    @Override
    public ObjectSerializer createSerializer(SerializeConfig config, Class type) {
        if (type.isEnum()) {
            createEnumSerializer(config, type);
        } else if (Reflects.isSubClassOrEquals(MultiValueMap.class, type)) {
            createMultiValueMapSerializer(config, type);
        } else if (Reflects.isSubClassOrEquals(Map.Entry.class, type) && JSONs.hasOtherPropertiesForMapEntry(type)){
            createMapEntrySubclass(config, type);
        }
        return config.get(type);
    }

    private void createMapEntrySubclass(SerializeConfig config, Class type){
        ObjectSerializer serializer = config.createJavaBeanSerializer(type);
        config.put(type, serializer);
    }

    private ObjectDeserializer createEnumDeserializer(ParserConfig config, Class clazz) {
        ObjectDeserializer deserializer = null;
        if (clazz.isEnum()) {
            if (config.isJacksonCompatible()) {
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (TypeUtils.isJacksonCreator(method)) {
                        deserializer = config.createJavaBeanDeserializer(clazz, clazz);
                        config.putDeserializer(clazz, deserializer);
                        return deserializer;
                    }
                }
            }
            Class<?> deserClass = null;
            JSONType jsonType = Reflects.getAnnotation(clazz, JSONType.class);
            if (jsonType != null) {
                deserClass = jsonType.deserializer();
                try {
                    deserializer = (ObjectDeserializer) deserClass.newInstance();
                    config.putDeserializer(clazz, deserializer);
                    return deserializer;
                } catch (Throwable error) {
                    // skip
                }
            }

            deserializer = new EnumDeserializer(clazz);
        }
        return deserializer;
    }

    private void createEnumSerializer(SerializeConfig config, Class type) {
        if (type.isEnum()) {
            JSONType jsonType = TypeUtils.getAnnotation(type, JSONType.class);
            if (jsonType != null && jsonType.serializeEnumAsJavaBean()) {
                config.put(type, config.createJavaBeanSerializer(type));
            } else {
                config.put(type, config.get(Enum.class));
            }
        }
    }

    private void createMultiValueMapSerializer(SerializeConfig config, Class type) {
        config.put(type, MultiValueMapCodec.INSTANCE);
    }
}
