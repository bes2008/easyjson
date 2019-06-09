package com.github.fangjinuo.easyjson.jackson.ext;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.github.fangjinuo.easyjson.core.type.Types;
import com.github.fangjinuo.easyjson.jackson.serializer.DateSerializer;
import com.github.fangjinuo.easyjson.jackson.serializer.NumberSerializer;

import java.util.Date;

public class EasyJsonBeanSerializerFactory extends BeanSerializerFactory {
    public static EasyJsonBeanSerializerFactory instance = new EasyJsonBeanSerializerFactory(null);

    public EasyJsonBeanSerializerFactory(SerializerFactoryConfig config) {
        super(config);
    }

    public SerializerFactory withConfig(SerializerFactoryConfig config) {
        if (_factoryConfig == config) {
            return this;
        }
        if (getClass() != EasyJsonBeanSerializerFactory.class) {
            throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName()
                    + ") has not properly overridden method 'withAdditionalSerializers': cannot instantiate subtype with "
                    + "additional serializer definitions");
        }
        return new EasyJsonBeanSerializerFactory(config);
    }

    protected JsonSerializer<?> _createSerializer2(SerializerProvider prov,
                                                   JavaType type, BeanDescription beanDesc, boolean staticTyping)
            throws JsonMappingException {
        Class<?> rawType = type.getRawClass();
        String clsName = rawType.getName();
        JsonSerializer<?> ser = null;
        if (Types.isPrimitive(rawType) || clsName.startsWith("java.")) {
            if (Number.class.isAssignableFrom(rawType)) {
                ser = new NumberSerializer().createContextual(prov, null, type);
                if (ser != null) {
                    return ser;
                }
            }
        }
        if (Date.class.isAssignableFrom(rawType)) {
            ser = new DateSerializer().createContextual(prov, null, type);
            if (ser != null) {
                return ser;
            }
        }
        return super._createSerializer2(prov, type, beanDesc, staticTyping);
    }
}
