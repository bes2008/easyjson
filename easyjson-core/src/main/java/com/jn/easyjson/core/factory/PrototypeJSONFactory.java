package com.jn.easyjson.core.factory;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONFactory;

public class PrototypeJSONFactory implements JSONFactory {
    private JSONBuilder jsonBuilder;

    public PrototypeJSONFactory(){

    }

    public PrototypeJSONFactory(JSONBuilder jsonBuilder){
        setJsonBuilder(jsonBuilder);
    }

    public void setJsonBuilder(JSONBuilder jsonBuilder) {
        this.jsonBuilder = jsonBuilder;
    }

    @Override
    public JSON get() {
        return jsonBuilder.build();
    }
}
