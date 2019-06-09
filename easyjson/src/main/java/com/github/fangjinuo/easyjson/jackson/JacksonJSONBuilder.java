package com.github.fangjinuo.easyjson.jackson;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.github.fangjinuo.easyjson.core.JSON;
import com.github.fangjinuo.easyjson.core.JSONBuilder;
import com.github.fangjinuo.easyjson.core.type.Types;
import com.github.fangjinuo.easyjson.jackson.deserializer.BooleanDeserializer;
import com.github.fangjinuo.easyjson.jackson.deserializer.Deserializers;
import com.github.fangjinuo.easyjson.jackson.deserializer.EnumDeserializer;
import com.github.fangjinuo.easyjson.jackson.deserializer.NumberDeserializer;
import com.github.fangjinuo.easyjson.jackson.serializer.BooleanSerializer;
import com.github.fangjinuo.easyjson.jackson.serializer.EnumSerializer;
import com.github.fangjinuo.easyjson.jackson.serializer.NumberSerializer;

public class JacksonJSONBuilder extends JSONBuilder {
    private static boolean moduleRegistered = false;
    private static MyObjectMapper objectMapper = new MyObjectMapper();

    static {
        makesureModuleRegister();
    }

    static class MyObjectMapper extends ObjectMapper {
        public MyObjectMapper() {
            super();
            setDefaultDeserializationContext(new DefaultDeserializationContext.Impl(MyBeanDeserializerFactory.instance));
            setSerializerFactory(MyBeanSerializerFactory.instance);
        }

        public MyObjectMapper(ObjectMapper src) {
            super(src);
            setDefaultDeserializationContext(new DefaultDeserializationContext.Impl(MyBeanDeserializerFactory.instance));
            setSerializerFactory(MyBeanSerializerFactory.instance);
        }

        @Override
        public DeserializationConfig getDeserializationConfig() {
            return super.getDeserializationConfig();
        }

        public void setDescrializationConfig(DeserializationConfig config) {
            this._deserializationConfig = config;
        }

        public void setSerializationConfig(SerializationConfig config) {
            this._serializationConfig = config;
        }

        public void setDefaultDeserializationContext(DefaultDeserializationContext context) {
            this._deserializationContext = context;
        }
    }

    static class MyBeanSerializerFactory extends BeanSerializerFactory {
        public static MyBeanSerializerFactory instance = new MyBeanSerializerFactory(null);

        public MyBeanSerializerFactory(SerializerFactoryConfig config) {
            super(config);
        }

        public SerializerFactory withConfig(SerializerFactoryConfig config) {
            if (_factoryConfig == config) {
                return this;
            }
            if (getClass() != MyBeanSerializerFactory.class) {
                throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName()
                        + ") has not properly overridden method 'withAdditionalSerializers': cannot instantiate subtype with "
                        + "additional serializer definitions");
            }
            return new MyBeanSerializerFactory(config);
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
            return super._createSerializer2(prov, type, beanDesc, staticTyping);
        }
    }

    static class MyBeanDeserializerFactory extends BeanDeserializerFactory {
        public static MyBeanDeserializerFactory instance = new MyBeanDeserializerFactory(new DeserializerFactoryConfig());

        public MyBeanDeserializerFactory(DeserializerFactoryConfig config) {
            super(config);
        }

        public DeserializerFactory withConfig(DeserializerFactoryConfig config) {
            if (_factoryConfig == config) {
                return this;
            }
            ClassUtil.verifyMustOverride(MyBeanDeserializerFactory.class, this, "withConfig");
            return new MyBeanDeserializerFactory(config);
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
            return super.findStdDeserializer(ctxt, type, beanDesc);
        }
    }

    private static void makesureModuleRegister() {
        if (!moduleRegistered) {
            synchronized (JacksonJSONBuilder.class) {
                if (!moduleRegistered) {
                    SimpleModule module = new SimpleModule();
                    SimpleDeserializers simpleDeserializers = new Deserializers();
                    module.setDeserializers(simpleDeserializers);

                    // boolean
                    module.addSerializer(Boolean.class, new BooleanSerializer());
                    module.addDeserializer(Boolean.class, new BooleanDeserializer());

                    // enum
                    module.addDeserializer(Enum.class, new EnumDeserializer<Enum>());
                    module.addSerializer(Enum.class, new EnumSerializer<Enum>());

                    objectMapper.registerModule(module);
                    moduleRegistered = true;

                }
            }
        }
    }

    private void configEnum(MyObjectMapper objectMapper) {
        // Enum: jackson default priority: ordinal() > toString() > name()
        // Our EnumSerializer priority: ordinal() > toString() > field > name()

        // step 1 : clear old config:
        SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
        serializationConfig = serializationConfig.withoutAttribute(EnumSerializer.WRITE_ENUM_USING_FIELD_ATTR_KEY);

        DeserializationConfig deserializationConfig = objectMapper.getDeserializationConfig();
        deserializationConfig = deserializationConfig.withoutAttribute(EnumDeserializer.READ_ENUM_USING_INDEX_ATTR_KEY);
        deserializationConfig = deserializationConfig.withoutAttribute(EnumDeserializer.READ_ENUM_USING_FIELD_ATTR_KEY);

        // ordinal()
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, serializeEnumUsingValue);
        deserializationConfig = deserializationConfig.withAttribute(EnumDeserializer.READ_ENUM_USING_INDEX_ATTR_KEY, serializeEnumUsingValue);

        // toString()
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, serializeEnumUsingToString);
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, serializeEnumUsingToString);

        // field
        if (serializeEnumUsingField != null) {
            serializationConfig = serializationConfig.withAttribute(EnumSerializer.WRITE_ENUM_USING_FIELD_ATTR_KEY, serializeEnumUsingField);
            deserializationConfig = deserializationConfig.withAttribute(EnumDeserializer.READ_ENUM_USING_FIELD_ATTR_KEY, serializeEnumUsingField);
        }
        objectMapper.setDescrializationConfig(deserializationConfig);
        objectMapper.setSerializationConfig(serializationConfig);

    }

    private void configBoolean(MyObjectMapper objectMapper) {
        SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
        serializationConfig = serializationConfig.withAttribute(BooleanSerializer.WRITE_BOOLEAN_USING_1_0_ATTR_KEY, serializeBooleanUsing1_0);
        serializationConfig = serializationConfig.withAttribute(BooleanSerializer.WRITE_BOOLEAN_USING_ON_OFF_ATTR_KEY, serializeBooleanUsingOnOff);

        DeserializationConfig deserializationConfig = objectMapper.getDeserializationConfig();
        deserializationConfig = deserializationConfig.withAttribute(BooleanDeserializer.READ_BOOLEAN_USING_1_0_ATTR_KEY, serializeBooleanUsing1_0);
        deserializationConfig = deserializationConfig.withAttribute(BooleanDeserializer.READ_BOOLEAN_USING_ON_OFF_ATTR_KEY, serializeBooleanUsingOnOff);

        objectMapper.setSerializationConfig(serializationConfig);
        objectMapper.setDescrializationConfig(deserializationConfig);
    }

    private void configNumber(MyObjectMapper objectMapper) {
        SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
        serializationConfig = serializationConfig.withAttribute(NumberSerializer.WRITE_LONG_AS_STRING_ATTR_KEY, serializeLongAsString);
        serializationConfig = serializationConfig.withAttribute(NumberSerializer.WRITE_NUMBER_AS_STRING_ATTR_KEY, serializeNumberAsString);
        DeserializationConfig deserializationConfig = objectMapper.getDeserializationConfig();
        deserializationConfig = deserializationConfig.withAttribute(NumberDeserializer.READ_LONG_USING_STRING_ATTR_KEY, serializeLongAsString);
        deserializationConfig = deserializationConfig.withAttribute(NumberDeserializer.READ_NUMBER_USING_STRING_ATTR_KEY, serializeNumberAsString);
        objectMapper.setSerializationConfig(serializationConfig);
        objectMapper.setDescrializationConfig(deserializationConfig);
    }


    @Override
    public JSON build() {
        makesureModuleRegister();
        MyObjectMapper mapper = new MyObjectMapper(objectMapper);

        if (serializeNulls) {
            //  objectMapper.configure(SerializationFeature);
        }

        configBoolean(mapper);
        configNumber(mapper);
        configEnum(mapper);

        JacksonAdapter jsonHandler = new JacksonAdapter();
        jsonHandler.setObjectMapper(mapper);
        JSON json = new JSON();
        json.setJsonHandler(jsonHandler);
        return json;
    }
}
