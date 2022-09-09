package com.jn.easyjson.core.factory;

import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.JSONFactory;
import com.jn.easyjson.core.exclusion.Exclusion;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.PrimitiveArrays;

import java.util.HashMap;
import java.util.Map;

public class JsonFactorys {
    private JsonFactorys() {
    }

    private static JSONBuilder GLOBAL_JSON_BUILDER = null;

    public static void setGlobalJsonBuilder(JSONBuilder jsonBuilder) {
        GLOBAL_JSON_BUILDER = jsonBuilder;
    }

    private static Map<JsonFactoryProperties, JSONFactory> cache = new HashMap<JsonFactoryProperties, JSONFactory>();

    public static JSONFactory getJSONFactory() {
        return getJSONFactory(JsonScope.SINGLETON);
    }

    public static JSONFactory getJSONFactory(JsonScope jsonScope) {
        return getJSONFactory(getJsonBuilder(null)
                        .serializeNulls(true)
                        .enableIgnoreAnnotation(),
                jsonScope);
    }

    public static JSONFactory getJSONFactory(JSONBuilder jsonBuilder, JsonScope jsonScope) {
        if (jsonScope == JsonScope.SINGLETON) {
            return new SingletonJSONFactory(getJsonBuilder(jsonBuilder));
        } else {
            return new PrototypeJSONFactory(getJsonBuilder(jsonBuilder));
        }
    }

    private static JSONBuilder getJsonBuilder(@Nullable JSONBuilder jsonBuilder) {
        if (jsonBuilder != null) {
            return jsonBuilder;
        }
        jsonBuilder = GLOBAL_JSON_BUILDER;
        if (jsonBuilder != null) {
            return jsonBuilder;
        }

        return JSONBuilderProvider.create();
    }

    public static JSONFactory getJSONFactory(JsonFactoryProperties properties) {
        return getJSONFactory(properties, null);
    }

    public static JSONFactory getJSONFactory(JsonFactoryProperties properties, JSONBuilder jsonBuilder) {
        JsonScope jsonScope = properties.getJsonScope();
        if (jsonScope != null) {
            // 创建新的 JSONFactory
            jsonBuilder = getJsonBuilder(jsonBuilder);
            if (jsonBuilder != GLOBAL_JSON_BUILDER) {
                jsonBuilder = JSONBuilderProvider.create();
                jsonBuilder.prettyFormat(properties.isPrettyFormat())
                        .serializeNulls(properties.isSerializeNulls())
                        .serializeEnumUsingIndex(properties.isSerializeEnumUsingIndex())
                        .serializeEnumUsingToString(properties.isSerializeEnumUsingToString())
                        .serializeDateUsingPattern(properties.getDatePattern())
                        .serializeDateUsingToString(properties.getSerializeDateUsingToString())
                        .serializeLongAsString(properties.isSerializeLongAsString())
                        .proxyDialectIdentify(properties.getProxyDialectIdentify())
                        .enableCustomConfiguration(properties.isEnableCustomConfiguration())
                        .excludeFieldsWithAppendModifiers(PrimitiveArrays.unwrap(Collects.asArray(properties.getExclusiveFieldModifiers(), Integer.class), false))
                        .addExclusionStrategies(Collects.asArray(properties.getExclusions(), Exclusion.class));
            }
            return getJSONFactory(jsonBuilder, properties.getJsonScope());
        } else {
            // 复用已有的 JSONFactory
            JSONFactory factory = cache.get(properties);
            if (factory == null) {
                synchronized (cache) {
                    factory = cache.get(properties);
                    if (factory == null) {
                        properties.setJsonScope(JsonScope.SINGLETON);
                        factory = getJSONFactory(properties);
                        cache.put(properties, factory);
                    }
                }
            }
            //System.out.println(factory);
            return factory;
        }
    }


}
