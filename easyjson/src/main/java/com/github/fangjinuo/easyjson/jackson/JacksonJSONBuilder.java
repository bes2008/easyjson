package com.github.fangjinuo.easyjson.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;
import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.fangjinuo.easyjson.core.JSON;
import com.github.fangjinuo.easyjson.core.JSONBuilder;
import com.github.fangjinuo.easyjson.jackson.deserializer.Deserializers;
import com.github.fangjinuo.easyjson.jackson.deserializer.EnumDeserializer;
import com.github.fangjinuo.easyjson.jackson.serializer.EnumSerializer;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class JacksonJSONBuilder extends JSONBuilder {
    private static boolean moduleRegistered = false;
    private static MyObjectMapper objectMapper = new MyObjectMapper();

    static {
        makesureModuleRegister();
    }

    static class MyObjectMapper extends ObjectMapper{
        public MyObjectMapper() {
        }

        public MyObjectMapper(ObjectMapper src) {
            super(src);
        }

        @Override
        public DeserializationConfig getDeserializationConfig() {
            return super.getDeserializationConfig();
        }

        public void setDescrializationConfig (DeserializationConfig config){
            this._deserializationConfig = config;
        }

        public void setSerializationConfig(SerializationConfig config){
            this._serializationConfig = config;
        }



    }


    private static void makesureModuleRegister() {
        if (!moduleRegistered) {
            synchronized (JacksonJSONBuilder.class) {
                if(!moduleRegistered) {
                    SimpleModule module = new SimpleModule();
                    SimpleDeserializers simpleDeserializers = new Deserializers();
                    module.setDeserializers(simpleDeserializers);
                    // enum
                    module.addDeserializer(Enum.class, new EnumDeserializer<Enum>());
                    module.addSerializer(Enum.class, new EnumSerializer<Enum>());

                    objectMapper.registerModule(module);
                    moduleRegistered=true;

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

    @Override
    public JSON build() {
        makesureModuleRegister();
        MyObjectMapper mapper = new MyObjectMapper(objectMapper);

        if (serializeNulls) {
            //        objectMapper.configure(SerializationFeature);
        }

        configEnum(mapper);
        JacksonAdapter jsonHandler = new JacksonAdapter();
        jsonHandler.setObjectMapper(mapper);
        JSON json = new JSON();
        json.setJsonHandler(jsonHandler);
        return json;
    }
}
