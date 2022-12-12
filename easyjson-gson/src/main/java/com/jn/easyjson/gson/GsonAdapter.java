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

package com.jn.easyjson.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jn.easyjson.core.JsonException;
import com.jn.easyjson.core.JsonHandlerAdapter;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.gson.node.GsonJsonMapper;
import com.jn.langx.text.translate.StringEscapes;
import com.jn.langx.util.Objs;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.unicode.Utf8s;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

public class GsonAdapter extends JsonHandlerAdapter<Gson> {

    @Override
    public String serialize(Object src, Type typeOfT) throws JsonException {
        if (src instanceof JsonTreeNode) {
            JsonElement element = GsonJsonMapper.fromJsonTreeNode((JsonTreeNode) src);
            return getDelegate().toJson(element, element.getClass());
        }
        return getDelegate().toJson(src, typeOfT);
    }

    @Override
    public <T> T deserialize(String json, Type typeOfT) throws JsonException {
        try {
            return getDelegate().fromJson(json, typeOfT);
        } catch (JsonException e) {
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
                return getDelegate().fromJson(json, typeOfT);
            }
            throw e;
        }
    }

    @Override
    public <T> T deserialize(Reader reader, Type typeOfT) throws JsonException {
        try {
            String json = IOs.readAsString(reader);
            return deserialize(json, typeOfT);
        } catch (IOException ex) {
            throw new JsonException(ex);
        }
    }

    @Override
    public JsonTreeNode deserialize(String json) throws JsonException {
        try {
            JsonElement node = new JsonParser().parse(json);
            return GsonJsonMapper.toJsonTreeNode(node);
        } catch (Throwable e) {
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
                JsonElement node = new JsonParser().parse(json);
                return GsonJsonMapper.toJsonTreeNode(node);
            }
            throw JsonException.wrapAsJsonException(e);
        }

    }

    @Deprecated
    public void setGson(Gson gson) {
        setDelegate(gson);
    }
}
