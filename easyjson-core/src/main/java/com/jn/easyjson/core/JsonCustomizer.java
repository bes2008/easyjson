package com.jn.easyjson.core;

import com.jn.langx.Customizer;

public interface JsonCustomizer extends Customizer<JSON> {
    void customize(JSON json);
}
