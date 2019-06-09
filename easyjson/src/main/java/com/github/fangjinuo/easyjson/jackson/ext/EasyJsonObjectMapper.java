package com.github.fangjinuo.easyjson.jackson.ext;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;

public class EasyJsonObjectMapper extends ObjectMapper {
    public EasyJsonObjectMapper() {
        super();
        setDefaultDeserializationContext(new DefaultDeserializationContext.Impl(EasyJsonBeanDeserializerFactory.instance));
        setSerializerFactory(EasyJsonBeanSerializerFactory.instance);
    }

    public EasyJsonObjectMapper(ObjectMapper src) {
        super(src);
        setDefaultDeserializationContext(new DefaultDeserializationContext.Impl(EasyJsonBeanDeserializerFactory.instance));
        setSerializerFactory(EasyJsonBeanSerializerFactory.instance);
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
