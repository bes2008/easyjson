package com.github.fangjinuo.easyjson.fastjson;

import com.github.fangjinuo.easyjson.core.JsonException;
import com.github.fangjinuo.easyjson.core.JsonHandler;

import java.lang.reflect.Type;

public class FastJsonAdapter implements JsonHandler {
    @Override
    public <T> T deserialize(String json, Type typeOfT) throws JsonException {
        return null;
    }

    @Override
    public String serialize(Object src, Type typeOfT) throws JsonException {
        return null;
    }
}
