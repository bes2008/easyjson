package com.google.gson.easyjson;

import com.google.gson.internal.Excluder;
import com.jn.easyjson.core.exclusion.Exclusion;
import com.jn.langx.util.reflect.FieldAttributes;
import com.jn.langx.util.reflect.MethodAttributes;

public class GsonExcluderExclusion implements Exclusion {
    private Excluder excluder;

    public GsonExcluderExclusion(Excluder excluder){
        this.excluder = excluder;
    }

    @Override
    public boolean shouldSkipMethod(MethodAttributes m, boolean serializePhrase) {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f, boolean serializePhrase) {
        return this.excluder.excludeField(f.get(), serializePhrase);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz, boolean serializePhrase) {
        return this.excluder.excludeClass(clazz, serializePhrase);
    }
}
