package com.github.fangjinuo.easyjson.fastjson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;

public class FastJsonParserBuilder {
    private ParserConfig config;
    private int featureValues;

    public FastJsonParserBuilder config(ParserConfig config) {
        this.config = config;
        return this;
    }

    public FastJsonParserBuilder featureValues(int featureValues) {
        this.featureValues = featureValues;
        return this;
    }

    public DefaultJSONParser build(String jsonString) {
        return new DefaultJSONParser(jsonString, config, featureValues);
    }
}
