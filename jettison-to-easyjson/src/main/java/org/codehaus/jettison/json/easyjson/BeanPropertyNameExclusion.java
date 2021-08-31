package org.codehaus.jettison.json.easyjson;

import com.jn.easyjson.core.exclusion.Exclusion;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.FieldAttributes;
import com.jn.langx.util.reflect.MethodAttributes;
import com.jn.langx.util.reflect.Reflects;

import java.util.List;

public class BeanPropertyNameExclusion implements Exclusion {
    private List<String> propertyNames;

    public BeanPropertyNameExclusion(List<String> propertyNames) {
        this.propertyNames = propertyNames;
    }

    @Override
    public boolean shouldSkipMethod(MethodAttributes m, boolean serializePhrase) {
        String propertyName = Reflects.extractFieldName(m.get());
        return Collects.contains(this.propertyNames, propertyName);
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f, boolean serializePhrase) {
        String propertyName = Reflects.extractFieldName(f.get());
        return Collects.contains(this.propertyNames, propertyName);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz, boolean serializePhrase) {
        return false;
    }
}
