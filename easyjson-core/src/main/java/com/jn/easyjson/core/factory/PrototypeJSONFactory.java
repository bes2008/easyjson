package com.jn.easyjson.core.factory;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONFactory;

public class PrototypeJSONFactory implements JSONFactory {
    private JSONBuilder jsonBuilder;

    public PrototypeJSONFactory(JSONBuilder jsonBuilder){
        setJSONBuilder(jsonBuilder);
    }

    @Override
    public void setJSONBuilder(JSONBuilder jsonBuilder) {
        this.jsonBuilder = jsonBuilder;
    }

    @Override
    public JSONBuilder getJSONBuilder() {
        return jsonBuilder;
    }

    @Override
    public JSON get() {
        return jsonBuilder.build();
    }
}
