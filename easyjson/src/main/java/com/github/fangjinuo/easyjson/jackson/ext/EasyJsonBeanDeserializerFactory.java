package com.github.fangjinuo.easyjson.jackson.ext;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.github.fangjinuo.easyjson.core.type.Types;
import com.github.fangjinuo.easyjson.jackson.deserializer.DateDeserializer;
import com.github.fangjinuo.easyjson.jackson.deserializer.NumberDeserializer;

import java.util.Date;

public class EasyJsonBeanDeserializerFactory extends BeanDeserializerFactory {
    public static EasyJsonBeanDeserializerFactory instance = new EasyJsonBeanDeserializerFactory(new DeserializerFactoryConfig());

    public EasyJsonBeanDeserializerFactory(DeserializerFactoryConfig config) {
        super(config);
    }

    public DeserializerFactory withConfig(DeserializerFactoryConfig config) {
        if (_factoryConfig == config) {
            return this;
        }
        ClassUtil.verifyMustOverride(EasyJsonBeanDeserializerFactory.class, this, "withConfig");
        return new EasyJsonBeanDeserializerFactory(config);
    }

    protected JsonDeserializer<?> findStdDeserializer(DeserializationContext ctxt,
                                                      JavaType type, BeanDescription beanDesc)
            throws JsonMappingException {
        // note: we do NOT check for custom deserializers here, caller has already
        // done that
        Class<?> rawType = type.getRawClass();
        String clsName = rawType.getName();
        if (Types.isPrimitive(rawType) || clsName.startsWith("java.")) {
            // Primitives/wrappers, other Numbers:
            JsonDeserializer<?> deser = null;
            // code block append by easyjson [start]
            if (Number.class.isAssignableFrom(rawType)) {
                deser = new NumberDeserializer().createContextual(ctxt, null, Types.getPrimitiveWrapClass(rawType));
                if (deser != null) {
                    return deser;
                }
            }
            // code block append by easyjson [end]
        }
        if(Date.class.isAssignableFrom(rawType)){
            JsonDeserializer<?> deser = new DateDeserializer().createContextual(ctxt, null, rawType);
            if(deser!=null){
                return deser;
            }
        }
        return super.findStdDeserializer(ctxt, type, beanDesc);
    }
}
