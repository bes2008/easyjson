package com.github.fangjinuo.easyjson.gson;

import com.github.fangjinuo.easyjson.core.JSON;
import com.github.fangjinuo.easyjson.core.JSONBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonJSONBuilder extends JSONBuilder {
    public JSON build() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if(serializeNulls){
            gsonBuilder.serializeNulls();
        }
        if(prettyFormat){
            gsonBuilder.setPrettyPrinting();
        }
        Gson gson = gsonBuilder.create();
        JSON json = new JSON();
        GsonAdapter gsonAdapter = new GsonAdapter();
        gsonAdapter.setGson(gson);
        json.setJsonHandler(gsonAdapter);
        return json;
    }
}
