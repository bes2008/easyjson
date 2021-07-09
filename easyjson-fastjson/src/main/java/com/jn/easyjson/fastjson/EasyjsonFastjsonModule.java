package com.jn.easyjson.fastjson;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.spi.Module;
import com.alibaba.fastjson.util.TypeUtils;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.Method;

public class EasyjsonFastjsonModule implements Module {
    @Override
    public ObjectDeserializer createDeserializer(ParserConfig config, Class type) {
        return createEnumDeserializer(config, type);
    }

    private ObjectDeserializer createEnumDeserializer(ParserConfig config, Class clazz) {
        ObjectDeserializer derializer = null;
        if (clazz.isEnum()) {
            if (config.isJacksonCompatible()) {
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (TypeUtils.isJacksonCreator(method)) {
                        derializer = config.createJavaBeanDeserializer(clazz, clazz);
                        config.putDeserializer(clazz, derializer);
                        return derializer;
                    }
                }
            }
            Class<?> deserClass = null;
            JSONType jsonType = Reflects.getAnnotation(clazz, JSONType.class);
            if (jsonType != null) {
                deserClass = jsonType.deserializer();
                try {
                    derializer = (ObjectDeserializer) deserClass.newInstance();
                    config.putDeserializer(clazz, derializer);
                    return derializer;
                } catch (Throwable error) {
                    // skip
                }
            }

            derializer = new EnumDeserializer(clazz);
        }
        return derializer;
    }

    @Override
    public ObjectSerializer createSerializer(SerializeConfig config, Class type) {
        if (type.isEnum()) {
            createEnumSerializer(config, type);
        }
        return config.get(type);
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
}
