package com.jn.easyjson.supports.feign.codec;

import com.jn.easyjson.core.JSONFactory;
import com.jn.easyjson.core.factory.JsonFactorys;
import com.jn.easyjson.core.factory.JsonScope;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;

import java.lang.reflect.Type;

public class EasyjsonEncoder implements Encoder {

    private JSONFactory jsonFactory;

    public EasyjsonEncoder() {
        this.jsonFactory = JsonFactorys.getJSONFactory(JsonScope.SINGLETON);
    }

    public EasyjsonEncoder(JSONFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public void encode(Object o, Type type, RequestTemplate requestTemplate) throws EncodeException {
        requestTemplate.body(jsonFactory.get().toJson(o, type));
    }
}
