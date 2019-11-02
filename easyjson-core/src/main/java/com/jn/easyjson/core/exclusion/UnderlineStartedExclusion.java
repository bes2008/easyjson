package com.jn.easyjson.core.exclusion;

import com.jn.langx.util.reflect.FieldAttributes;

public class UnderlineStartedExclusion implements Exclusion {
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
}
