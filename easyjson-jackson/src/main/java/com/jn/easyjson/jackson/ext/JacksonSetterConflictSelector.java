package com.jn.easyjson.jackson.ext;

import com.jn.langx.Named;
import com.jn.langx.annotation.Nullable;

public interface JacksonSetterConflictSelector extends Named {
    @Nullable
    public Class select(Class class1, Class class2);
}
