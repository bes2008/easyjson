/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.fangjinuo.easyjson.fastjson;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.fangjinuo.easyjson.api.JSON;
import com.github.fangjinuo.easyjson.api.JSONBuilder;
import com.github.fangjinuo.easyjson.api.annotation.DependOn;
import com.github.fangjinuo.easyjson.api.annotation.Name;
import com.github.fangjinuo.easyjson.fastjson.codec.BooleanCodec;
import com.github.fangjinuo.easyjson.fastjson.codec.FastJsonParserBuilder;
import com.github.fangjinuo.easyjson.fastjson.codec.FastJsonSerializerBuilder;

import java.util.ArrayList;
import java.util.List;

@Name("fastjson")
@DependOn("com.alibaba.fastjson.JSON")
public class FastJsonJSONBuilder extends JSONBuilder {
    @Override
    public JSON build() {
        FastJsonSerializerBuilder serializer = buildSerializer();
        FastJsonParserBuilder parser = buildDeserializer();

        // boolean
        BooleanCodec booleanCodec = new BooleanCodec();
        booleanCodec.setUsing1_0(serializeBooleanUsing1_0);
        booleanCodec.setUsingOnOff(serializeBooleanUsingOnOff);
        serializer.apply(booleanCodec);
        parser.apply(booleanCodec);


        FastJson fastJson = new FastJson(serializer, parser);
        FastJsonAdapter jsonHandler = new FastJsonAdapter();
        jsonHandler.setFastJson(fastJson);
        return new JSON().setJsonHandler(jsonHandler);
    }

    private FastJsonSerializerBuilder buildSerializer() {
        SerializeConfig config = new SerializeConfig();
        config.setAsmEnable(false);
        FastJsonSerializerBuilder builder = new FastJsonSerializerBuilder();
        builder.config(config);
        builder.addFeature(SerializerFeature.DisableCircularReferenceDetect);
        return builder;
    }

    private FastJsonParserBuilder buildDeserializer() {
        ParserConfig config = new ParserConfig();
        List<Feature> features = new ArrayList<Feature>();
        int featureValues = com.alibaba.fastjson.JSON.DEFAULT_PARSER_FEATURE;
        for (Feature feature : features) {
            featureValues = Feature.config(featureValues, feature, true);
        }
        FastJsonParserBuilder builder = new FastJsonParserBuilder().config(config).featureValues(featureValues);
        return builder;
    }
}
