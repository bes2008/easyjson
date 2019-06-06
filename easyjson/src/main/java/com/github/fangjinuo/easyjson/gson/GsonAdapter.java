package com.github.fangjinuo.easyjson.gson;

import com.github.fangjinuo.easyjson.api.JsonHandler;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonAdapter implements JsonHandler {

    private Gson gson;

    public String serialize(Object src, Type typeOfT) {
        return gson.toJson(src, typeOfT);
    }

    public <T> T deserialize(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

}
