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

package com.jn.easyjson.fastjson;

import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONReaderScanner;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.jn.easyjson.core.JsonException;
import com.jn.easyjson.core.JsonHandler;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.tree.JsonTreeDeserializer;

import java.io.Reader;
import java.lang.reflect.Type;

public class FastJsonAdapter implements JsonHandler {
    private FastJson fastJson;

    @Override
    public <T> T deserialize(String json, Type typeOfT) throws JsonException {
        DefaultJSONParser parser = fastJson.getDeserializerBuilder().build(json);
        T value = parser.parseObject(typeOfT);
        parser.handleResovleTask(value);
        parser.close();
        return value;
    }

    @Override
    public <T> T deserialize(Reader reader, Type typeOfT) throws JsonException {
        JSONReaderScanner jsonReaderScanner = new JSONReaderScanner(reader, fastJson.getDeserializerBuilder().getFeatures());
        JSONReader jsonReader = new JSONReader(jsonReaderScanner);
        return jsonReader.readObject(typeOfT);
    }

    @Override
    public JsonTreeNode deserialize(String json) throws JsonException {
        return new JsonTreeDeserializer().parse(json);
    }

    @Override
    public String serialize(Object src, Type typeOfT) throws JsonException {
        if (src instanceof JsonTreeNode) {
            JsonTreeNode element = (JsonTreeNode) src;
            return fastJson.getJsonTreeSerializerBuilder().build().toJson(element);
        }
        JSONSerializer serializer = fastJson.getSerializerBuilder().build();
        serializer.write(src);
        return serializer.toString();
    }

    public void setFastJson(FastJson fastJson) {
        this.fastJson = fastJson;
    }
}
