package com.github.fangjinuo.easyjson.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;

public class FastJson {
    private JSONSerializer serializer;
    private FastJsonParserBuilder deserializerBuilder;

    public FastJson(JSONSerializer serializer, FastJsonParserBuilder deserializerBuilder) {
        this.serializer = serializer;
        this.deserializerBuilder = deserializerBuilder;
    }

    public JSONSerializer getSerializer() {
        return serializer;
    }

    public FastJsonParserBuilder getDeserializerBuilder() {
        return deserializerBuilder;
    }
}
