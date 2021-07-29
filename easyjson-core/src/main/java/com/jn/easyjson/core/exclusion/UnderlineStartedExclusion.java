package com.jn.easyjson.core.exclusion;

import com.jn.langx.util.reflect.FieldAttributes;
import com.jn.langx.util.reflect.MethodAttributes;

public final class UnderlineStartedExclusion implements Exclusion {
    @Override
    public boolean shouldSkipMethod(MethodAttributes m, boolean serialize) {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes, boolean serialize) {
        String fieldName = fieldAttributes.getName();
        if (fieldName.startsWith("_")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass, boolean serialize) {
        return false;
    }

    @Override
    public int hashCode() {
        return 2;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UnderlineStartedExclusion;
    }
}
