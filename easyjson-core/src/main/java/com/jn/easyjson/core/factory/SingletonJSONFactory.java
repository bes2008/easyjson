package com.jn.easyjson.core.factory;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONFactory;

public class SingletonJSONFactory implements JSONFactory {
    private JSONBuilder jsonBuilder;
    private JSON json;

    public SingletonJSONFactory(JSONBuilder jsonBuilder) {
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
        if (json == null) {
            synchronized (this) {
                json = this.jsonBuilder.build();
            }
        }
        return json;
    }
}
