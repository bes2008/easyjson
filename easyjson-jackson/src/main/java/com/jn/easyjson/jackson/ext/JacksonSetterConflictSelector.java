package com.jn.easyjson.jackson.ext;

import com.jn.langx.Named;
import com.jn.langx.annotation.Nullable;

import java.lang.reflect.Method;

public interface JacksonSetterConflictSelector extends Named {
    @Nullable
    public Method select(Method setter1, final Method setter2);
}
