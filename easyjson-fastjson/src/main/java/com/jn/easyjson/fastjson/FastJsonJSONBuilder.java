/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.easyjson.fastjson;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.annotation.DependOn;
import com.jn.easyjson.core.codec.dialect.DialectIdentify;
import com.jn.easyjson.core.exclusion.ExclusionConfiguration;
import com.jn.easyjson.core.tree.JsonTreeSerializerBuilder;
import com.jn.easyjson.fastjson.codec.BooleanCodec;
import com.jn.easyjson.fastjson.codec.DateCodec;
import com.jn.easyjson.fastjson.codec.NumberCodec;
import com.jn.easyjson.fastjson.ext.EasyJsonParserConfig;
import com.jn.easyjson.fastjson.ext.EasyJsonSerializeConfig;
import com.jn.langx.annotation.Name;
import com.jn.langx.util.reflect.Reflects;

@Name("fastjson")
@DependOn("com.alibaba.fastjson.JSON")
public class FastJsonJSONBuilder extends JSONBuilder {

    public static final DialectIdentify FASTJSON = new DialectIdentify();
    static {
        FASTJSON.setId("fastjson");
        try {
            Class clazz = Class.forName("com.alibaba.fastjson.JSON");
            FASTJSON.setLibUrl(Reflects.getCodeLocation(clazz).toString());
        }catch (Throwable ex){
            // ignore it
        }
    }
    public FastJsonJSONBuilder() {
        super();
        dialectIdentify(FASTJSON);
    }

    public FastJsonJSONBuilder(ExclusionConfiguration exclusionConfiguration) {
        super(exclusionConfiguration);
        dialectIdentify(FASTJSON);
    }

    @Override
    public JSON build() {
        FastJsonSerializerBuilder serializerBuilder = buildSerializer();
        FastJsonParserBuilder deserializerBuilder = buildDeserializer();
        JsonTreeSerializerBuilder jsonTreeSerializerBuilder = buildJsonTreeWriter();

        // boolean
        BooleanCodec booleanCodec = new BooleanCodec();
        booleanCodec.setUsing1_0(serializeBooleanUsing1_0());
        booleanCodec.setUsingOnOff(serializeBooleanUsingOnOff());
        serializerBuilder.apply(booleanCodec);
        deserializerBuilder.apply(booleanCodec);

        // number
        NumberCodec numberCodec = new NumberCodec();
        numberCodec.setLongUsingString(serializeLongAsString());
        numberCodec.setUsingString(serializeNumberAsString());
        serializerBuilder.apply(numberCodec);
        deserializerBuilder.apply(numberCodec);

        // date
        DateCodec dateCodec = new DateCodec();
        dateCodec.setDateFormat(serializeUseDateFormat());
        dateCodec.setUsingToString(serializeDateUsingToString());
        serializerBuilder.apply(dateCodec);
        deserializerBuilder.apply(dateCodec);

        FastJson fastJson = new FastJson(serializerBuilder, deserializerBuilder, jsonTreeSerializerBuilder);
        FastJsonAdapter jsonHandler = new FastJsonAdapter();
        jsonHandler.setFastJson(fastJson);
        return new JSON().setJsonHandler(jsonHandler);
    }

    private JsonTreeSerializerBuilder buildJsonTreeWriter() {
        return new JsonTreeSerializerBuilder().setPrettyFormat(prettyFormat()).setSerializeNulls(serializeNulls());
    }

    private FastJsonSerializerBuilder buildSerializer() {
        SerializeConfig config = new EasyJsonSerializeConfig(this);
        FastJsonSerializerBuilder builder = new FastJsonSerializerBuilder();
        builder.config(config);
        builder.addFeature(SerializerFeature.DisableCircularReferenceDetect);
        builder.addFeature(SerializerFeature.SkipTransientField);
        builder.addFeature(SerializerFeature.IgnoreErrorGetter);
        if(!serializeNonFieldGetter()) {
            builder.addFeature(SerializerFeature.IgnoreNonFieldGetter);
        }

        // SerializerFeature.WriteNullStringAsEmpty ==> ""
        // SerializerFeature.WriteNullListAsEmpty ==>[]

        if (serializeNulls()) {
            builder.addFeature(SerializerFeature.WriteMapNullValue);
        }

        if (serializeEnumUsingToString()) {
            builder.addFeature(SerializerFeature.WriteEnumUsingToString);
        }
        if (prettyFormat()) {
            builder.addFeature(SerializerFeature.PrettyFormat);
        }
        return builder;
    }

    private FastJsonParserBuilder buildDeserializer() {
        ParserConfig config = new EasyJsonParserConfig(this);
        int featureValues = com.alibaba.fastjson.JSON.DEFAULT_PARSER_FEATURE;
        FastJsonParserBuilder builder = new FastJsonParserBuilder().config(config).defaultFeatureValues(featureValues);
        builder.addFeature(Feature.DisableCircularReferenceDetect);
        return builder;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        FastJsonJSONBuilder result = new FastJsonJSONBuilder(this.getExclusionConfiguration());
        this.copyTo(result);
        return result;
    }
}
