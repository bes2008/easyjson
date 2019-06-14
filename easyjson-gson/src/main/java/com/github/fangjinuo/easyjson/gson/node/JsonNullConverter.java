package com.github.fangjinuo.easyjson.gson.node;

import com.github.fangjinuo.easyjson.api.node.JsonNullNode;
import com.google.gson.JsonNull;

public class JsonNullConverter {
    public JsonNull toGson(JsonNullNode nullNode){
        return JsonNull.INSTANCE;
    }

    public JsonNullNode fromGson(JsonNull jsonNull){
        return JsonNullNode.INSTANCE;
    }
}
