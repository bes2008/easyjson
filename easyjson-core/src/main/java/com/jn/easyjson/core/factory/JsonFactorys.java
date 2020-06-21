package com.jn.easyjson.core.factory;

import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.JSONFactory;

public class JsonFactorys {
    public static JSONFactory getJSONFactory(JsonScope jsonScope) {
        return getJSONFactory(JSONBuilderProvider.create()
                        .serializeNulls(true)
                        .enableIgnoreAnnotation(),
                jsonScope);
    }

    public static JSONFactory getJSONFactory(JSONBuilder jsonBuilder, JsonScope jsonScope) {
        if (jsonScope == JsonScope.SINGLETON) {
            return new SingletonJSONFactory(jsonBuilder);
        } else {
            return new PrototypeJSONFactory(jsonBuilder);
        }
    }
}
