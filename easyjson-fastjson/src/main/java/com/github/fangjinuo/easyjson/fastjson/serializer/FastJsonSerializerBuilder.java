package com.github.fangjinuo.easyjson.fastjson.serializer;

import com.alibaba.fastjson.serializer.*;
import com.github.fangjinuo.easyjson.fastjson.FastJson;

import java.util.ArrayList;
import java.util.List;

public class FastJsonSerializerBuilder {
    private SerializeConfig config;
    private List<SerializeFilter> filters = new ArrayList<SerializeFilter>();
    private List<SerializerFeature> features = new ArrayList<SerializerFeature>();

    public FastJsonSerializerBuilder config(SerializeConfig config) {
        this.config = config;
        return this;
    }

    public FastJsonSerializerBuilder addFilter(SerializeFilter filter) {
        if (filter != null) {
            this.filters.add(filter);
        }
        return this;
    }

    public FastJsonSerializerBuilder addFeature(SerializerFeature feature) {
        if (feature != null) {
            this.features.add(feature);
        }
        return this;
    }

    public JSONSerializer build() {
        if (config == null) {
            throw new RuntimeException();
        }
        SerializeWriter out = new SerializeWriter(null, com.alibaba.fastjson.JSON.DEFAULT_GENERATE_FEATURE, features.toArray(new SerializerFeature[features.size()]));
        JSONSerializer serializer = new JSONSerializer(out, config);
        if (filters != null) {
            for (SerializeFilter filter : filters) {
                serializer.addFilter(filter);
            }
        }
        return serializer;
    }
}
