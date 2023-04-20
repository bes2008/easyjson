package com.jn.easyjson.core;

import com.jn.langx.NamedCustomizer;
import com.jn.langx.Ordered;

public interface JSONBuilderCustomizer extends Ordered, NamedCustomizer<JSONBuilder> {
    @Override
    void customize(JSONBuilder jsonBuilder);
}
