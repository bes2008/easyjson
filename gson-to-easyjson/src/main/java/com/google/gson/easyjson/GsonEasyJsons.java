package com.google.gson.easyjson;

import com.jn.easyjson.core.codec.dialect.DialectIdentify;
import com.jn.langx.util.reflect.Reflects;

public class GsonEasyJsons {
    public static final String JSON_IDENTIFY_STRING = "gson";

    public static final DialectIdentify GSON;

    static {
        GSON = new DialectIdentify(JSON_IDENTIFY_STRING, Reflects.getCodeLocation(GsonEasyJsons.class).toString());
    }

}
