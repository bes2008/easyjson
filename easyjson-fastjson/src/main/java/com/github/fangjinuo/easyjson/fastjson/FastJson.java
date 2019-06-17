/*
 *  Copyright 2019 the original author or authors.
 *
 *  Licensed under the LGPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.github.fangjinuo.easyjson.fastjson;

import com.github.fangjinuo.easyjson.core.tree.JsonTreeSerializerBuilder;
import com.github.fangjinuo.easyjson.fastjson.codec.FastJsonParserBuilder;
import com.github.fangjinuo.easyjson.fastjson.codec.FastJsonSerializerBuilder;

public class FastJson {
    private FastJsonSerializerBuilder serializerBuilder;
    private FastJsonParserBuilder deserializerBuilder;
    private JsonTreeSerializerBuilder jsonTreeSerializerBuilder;

    public FastJson(FastJsonSerializerBuilder serializerBuilder, FastJsonParserBuilder deserializerBuilder, JsonTreeSerializerBuilder jsonTreeSerializerBuilder) {
        this.serializerBuilder = serializerBuilder;
        this.deserializerBuilder = deserializerBuilder;
        this.jsonTreeSerializerBuilder = jsonTreeSerializerBuilder;
    }

    public JsonTreeSerializerBuilder getJsonTreeSerializerBuilder() {
        return jsonTreeSerializerBuilder;
    }

    public FastJsonSerializerBuilder getSerializerBuilder() {
        return serializerBuilder;
    }

    public FastJsonParserBuilder getDeserializerBuilder() {
        return deserializerBuilder;
    }
}
