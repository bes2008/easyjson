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
import com.jn.easyjson.gson.node.GsonBasedJsonTreeNodeMapper;

import java.io.Reader;
import java.lang.reflect.Type;

public class GsonAdapter extends JsonHandlerAdapter<Gson> {
    private GsonBasedJsonTreeNodeMapper mapper = new GsonBasedJsonTreeNodeMapper();

    @Override
    public String serialize(Object src, Type typeOfT) throws JsonException {
        if (src instanceof JsonTreeNode) {
            JsonElement element = mapper.mapping((JsonTreeNode) src);
            return getDelegate().toJson(element, element.getClass());
        }
        return getDelegate().toJson(src, typeOfT);
    }

    @Override
    public <T> T deserialize(String json, Type typeOfT) throws JsonException {
        return getDelegate().fromJson(json, typeOfT);
    }

    @Override
    public <T> T deserialize(Reader reader, Type typeOfT) throws JsonException {
        return getDelegate().fromJson(reader, typeOfT);
    }

    @Override
    public JsonTreeNode deserialize(String json) throws JsonException {
        JsonElement node = new JsonParser().parse(json);
        return mapper.create(node);
    }

    public void setGson(Gson gson) {
        setDelegate(gson);
    }
}
