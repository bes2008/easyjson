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

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.jn.easyjson.core.JsonException;
import com.jn.easyjson.core.JsonHandlerAdapter;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.tree.JsonTreeDeserializer;
import com.jn.langx.text.translate.StringEscapes;
import com.jn.langx.util.Objs;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.unicode.Utf8s;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

public class FastJsonAdapter extends JsonHandlerAdapter<FastJson> {

    @Override
    public <T> T deserialize(String json, Type typeOfT) throws JsonException {
        try {
            return parse(json, typeOfT);
        } catch (JSONException ex) {
            boolean changed = false;
            boolean rejudge = false;
            String json2 = json;
            if (getJsonBuilder().enableDecodeHex()) {
                json2 = Utf8s.convertHexToUnicode(json2);
                rejudge = true;
            }
            if (getJsonBuilder().enableUnescapeEscapeCharacter()) {
                json2 = StringEscapes.unescapeJson(json2);
                rejudge = true;
            }
            if (rejudge) {
                changed = !Objs.equals(json, json2);
            }
            if (changed) {
                return parse(json2, typeOfT);
            }
            throw ex;
        }
    }

    private <T> T parse(String json, Type typeOfT) throws JSONException {
        DefaultJSONParser parser = getDelegate().getDeserializerBuilder().build(json);
        T value = parser.parseObject(typeOfT);
        parser.handleResovleTask(value);
        parser.close();
        return value;
    }

    @Override
    public <T> T deserialize(Reader reader, Type typeOfT) throws JsonException {
        try {
            String json = IOs.readAsString(reader);
            return deserialize(json, typeOfT);
        } catch (IOException ex) {
            throw new JsonException(ex.getMessage(), ex);
        }
    }

    @Override
    public JsonTreeNode deserialize(String json) throws JsonException {
        return new JsonTreeDeserializer().parse(json);
    }

    @Override
    public String serialize(Object src, Type typeOfT) throws JsonException {
        if (src instanceof JsonTreeNode) {
            JsonTreeNode element = (JsonTreeNode) src;
            return getDelegate().getJsonTreeSerializerBuilder().build().toJson(element);
        }
        JSONSerializer serializer = getDelegate().getSerializerBuilder().build();
        serializer.write(src);
        return serializer.toString();
    }

    @Deprecated
    public void setFastJson(FastJson fastJson) {
        setDelegate(fastJson);
    }

}
