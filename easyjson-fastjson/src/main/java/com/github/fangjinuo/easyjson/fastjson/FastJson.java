package com.github.fangjinuo.easyjson.fastjson;

import com.github.fangjinuo.easyjson.fastjson.codec.FastJsonParserBuilder;
import com.github.fangjinuo.easyjson.fastjson.codec.FastJsonSerializerBuilder;

public class FastJson {
    private FastJsonSerializerBuilder serializerBuilder;
    private FastJsonParserBuilder deserializerBuilder;

    public FastJson(FastJsonSerializerBuilder serializerBuilder, FastJsonParserBuilder deserializerBuilder) {
        this.serializerBuilder = serializerBuilder;
        this.deserializerBuilder = deserializerBuilder;
    }

    public FastJsonSerializerBuilder getSerializerBuilder() {
        return serializerBuilder;
    }

    public FastJsonParserBuilder getDeserializerBuilder() {
        return deserializerBuilder;
    }
}
