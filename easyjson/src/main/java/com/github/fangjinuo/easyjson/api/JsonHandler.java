package com.github.fangjinuo.easyjson.api;

import java.lang.reflect.Type;

public interface JsonHandler {
    <T> T deserialize(String json, Type typeOfT);

    String serialize(Object src, Type typeOfT);

}
