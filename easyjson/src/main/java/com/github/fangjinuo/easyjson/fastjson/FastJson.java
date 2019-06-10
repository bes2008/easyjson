package com.github.fangjinuo.easyjson.fastjson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.serializer.JSONSerializer;

public class FastJson {
    private JSONSerializer serializer;
    private DefaultJSONParser deserializer;

    public FastJson(JSONSerializer serializer, DefaultJSONParser deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public JSONSerializer getSerializer() {
        return serializer;
    }

    public DefaultJSONParser getDeserializer() {
        return deserializer;
    }
}
