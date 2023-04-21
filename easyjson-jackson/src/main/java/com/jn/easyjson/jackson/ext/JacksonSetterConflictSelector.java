package com.jn.easyjson.jackson.ext;

import com.jn.langx.Named;
import com.jn.langx.annotation.Nullable;

public interface JacksonSetterConflictSelector extends Named {
    @Nullable
    public Class select(Class beanClass, Class parameterOfSetter1, Class parameterOfSetter2);
}
