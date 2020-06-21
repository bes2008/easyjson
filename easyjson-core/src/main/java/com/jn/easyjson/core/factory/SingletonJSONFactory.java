package com.jn.easyjson.core.factory;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONFactory;

public class SingletonJSONFactory implements JSONFactory {
    private JSONBuilder jsonBuilder;
    private JSON json;

    public SingletonJSONFactory() {
    }

    public SingletonJSONFactory(JSONBuilder jsonBuilder) {
        setJsonBuilder(jsonBuilder);
    }

    public void setJsonBuilder(JSONBuilder jsonBuilder) {
        this.jsonBuilder = jsonBuilder;
    }

    @Override
    public JSON get() {
        if (json != null) {
            return json;
        } else {
            synchronized (this) {
                if (json == null) {
                    json = this.jsonBuilder.build();
                }
            }
        }
        return json;
    }
}
