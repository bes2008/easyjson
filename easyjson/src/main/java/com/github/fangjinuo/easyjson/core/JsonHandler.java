package com.github.fangjinuo.easyjson.core;

import java.lang.reflect.Type;

public interface JsonHandler {
    <T> T deserialize(String json, Type typeOfT) throws JsonException;

    String serialize(Object src, Type typeOfT) throws JsonException;

}
