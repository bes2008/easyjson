package com.jn.easyjson.supports.jsonpath;

import com.jayway.jsonpath.Configuration;

public class EasyjsonPathStartup {
    public static void startup(){
        Configuration.setDefaults(EasyjsonDefaults.INSTANCE);
    }
}
