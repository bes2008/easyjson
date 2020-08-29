package com.jn.easyjson.core.factory;

import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.JSONFactory;

public class JsonFactorys {
    private JsonFactorys(){}

    public static JSONFactory getJSONFactory(){
        return getJSONFactory(JsonScope.SINGLETON);
    }

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

    public static JSONFactory getJSONFactory(JsonFactoryProperties properties) {
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        jsonBuilder.prettyFormat(properties.isPrettyFormat())
                .serializeNulls(properties.isSerializeNulls())
                .serializeEnumUsingIndex(properties.isSerializeEnumUsingIndex())
                .serializeEnumUsingToString(properties.isSerializeEnumUsingToString())
                .serializeDateUsingPattern(properties.getDatePattern())
                .serializeDateUsingToString(properties.getSerializeDateUsingToString())
                .serializeLongAsString(properties.isSerializeLongAsString());
        return getJSONFactory(jsonBuilder, properties.getJsonScope());
    }
}
