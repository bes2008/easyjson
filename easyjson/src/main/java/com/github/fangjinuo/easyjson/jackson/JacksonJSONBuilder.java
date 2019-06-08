package com.github.fangjinuo.easyjson.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.fangjinuo.easyjson.core.JSON;
import com.github.fangjinuo.easyjson.core.JSONBuilder;
import com.github.fangjinuo.easyjson.jackson.deserializer.EnumDeserializer;
import com.github.fangjinuo.easyjson.jackson.serializer.EnumSerializer;

public class JacksonJSONBuilder extends JSONBuilder {
    private static boolean moduleRegistered = false;
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        makesureModuleRegister();
    }

    public static void makesureModuleRegister(){
        if(!moduleRegistered){
            synchronized (JacksonJSONBuilder.class){
                SimpleModule module = new SimpleModule();
                objectMapper.registerModule(module);
            }
        }
    }

    private void configEnum(ObjectMapper objectMapper){
        // Enum: jackson default priority: ordinal() > toString() > name()
        // Our EnumSerializer priority: ordinal() > toString() > field > name()

        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, serializeEnumUsingValue);
        objectMapper.getSerializationConfig().withoutAttribute(EnumDeserializer.READ_ENUM_USING_INDEX_ATTR_KEY);
        if(serializeEnumUsingValue){
            objectMapper.getSerializationConfig().withAttribute(EnumDeserializer.READ_ENUM_USING_INDEX_ATTR_KEY,true);
        }

        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, serializeEnumUsingToString);
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, serializeEnumUsingToString);

        objectMapper.getSerializationConfig().withoutAttribute(EnumSerializer.WRITE_ENUM_USING_FIELD_ATTR_KEY);
        if(serializeEnumUsingField!=null) {
            objectMapper.getSerializationConfig().withAttribute(EnumSerializer.WRITE_ENUM_USING_FIELD_ATTR_KEY, serializeEnumUsingField);
        }

    }

    @Override
    public JSON build() {
        ObjectMapper mapper = objectMapper.copy();

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
