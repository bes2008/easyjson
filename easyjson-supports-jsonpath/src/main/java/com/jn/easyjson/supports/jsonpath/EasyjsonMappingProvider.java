package com.jn.easyjson.supports.jsonpath;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.jn.easyjson.core.util.JSONs;

import java.lang.reflect.Type;

public class EasyjsonMappingProvider implements MappingProvider {
    @Override
    public <T> T map(Object source, Class<T> targetType, Configuration configuration) {
        return map(targetType, source, configuration);
    }

    @Override
    public <T> T map(Object source, TypeRef<T> targetType, Configuration configuration) {
        return map(targetType.getType(), source, configuration);
    }

    private <T> T map(Type targetType, Object source, Configuration configuration) {
        if (source == null) {
            return null;
        }
        return JSONs.parse(JSONs.toJson(source), targetType);
    }
}
