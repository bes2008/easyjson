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

package com.github.fangjinuo.easyjson.fastjson.deserializer;

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
