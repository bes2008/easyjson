package com.jn.easyjson.supports.jsonpath;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import java.util.EnumSet;
import java.util.Set;

public class EasyjsonDefaults implements Configuration.Defaults {
    private static final EasyjsonProvider JSON_PROVIDER= new EasyjsonProvider();
    private static final EasyjsonMappingProvider MAPPING_PROVIDER = new EasyjsonMappingProvider();

    public static final EasyjsonDefaults INSTANCE = new EasyjsonDefaults();



    @Override
    public JsonProvider jsonProvider() {
        return JSON_PROVIDER;
    }

    @Override
    public Set<Option> options() {
        return EnumSet.noneOf(Option.class);
    }

    @Override
    public MappingProvider mappingProvider() {
        return MAPPING_PROVIDER;
    }
}
