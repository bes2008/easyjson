package com.github.fangjinuo.easyjson.fastjson;

import com.github.fangjinuo.easyjson.api.tree.JsonTreeSerializerBuilder;
import com.github.fangjinuo.easyjson.fastjson.codec.FastJsonParserBuilder;
import com.github.fangjinuo.easyjson.fastjson.codec.FastJsonSerializerBuilder;

public class FastJson {
    private FastJsonSerializerBuilder serializerBuilder;
    private FastJsonParserBuilder deserializerBuilder;
    private JsonTreeSerializerBuilder jsonTreeSerializerBuilder;

    public FastJson(FastJsonSerializerBuilder serializerBuilder, FastJsonParserBuilder deserializerBuilder, JsonTreeSerializerBuilder jsonTreeSerializerBuilder) {
        this.serializerBuilder = serializerBuilder;
        this.deserializerBuilder = deserializerBuilder;
        this.jsonTreeSerializerBuilder = jsonTreeSerializerBuilder;
    }

    public JsonTreeSerializerBuilder getJsonTreeSerializerBuilder() {
        return jsonTreeSerializerBuilder;
    }

    public FastJsonSerializerBuilder getSerializerBuilder() {
        return serializerBuilder;
    }

    public FastJsonParserBuilder getDeserializerBuilder() {
        return deserializerBuilder;
    }
}
