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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.jn.easyjson.fastjson.codec.Typed;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FastJsonParserBuilder {
    private ParserConfig config;
    private List<Feature> features = new ArrayList<Feature>();
    private int defaultFeatureValues = JSON.DEFAULT_PARSER_FEATURE;

    public FastJsonParserBuilder config(ParserConfig config) {
        this.config = config;
        return this;
    }

    public FastJsonParserBuilder defaultFeatureValues(int value) {
        defaultFeatureValues = value;
        return this;
    }

    public FastJsonParserBuilder addFeature(Feature feature) {
        this.features.add(feature);
        return this;
    }

    public FastJsonParserBuilder apply(ObjectDeserializer deserializer) {
        if (deserializer instanceof Typed) {
            List<Type> applyTo = ((Typed) deserializer).applyTo();
            if (applyTo != null && !applyTo.isEmpty()) {
                for (Type type : applyTo) {
                    apply(type, deserializer);
                }
            }
        }
        return this;
    }

    public FastJsonParserBuilder apply(Type type, ObjectDeserializer deserializer) {
        config.putDeserializer(type, deserializer);
        return this;
    }

    public DefaultJSONParser build(String jsonString) {
        return new DefaultJSONParser(jsonString, config, getFeatures());
    }

    public int getFeatures(){
        int featureValues = defaultFeatureValues;
        for (Feature feature : features) {
            featureValues |= feature.getMask();
        }
        return featureValues;
    }
}
