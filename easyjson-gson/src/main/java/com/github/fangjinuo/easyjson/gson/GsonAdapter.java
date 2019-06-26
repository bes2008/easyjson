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

package com.github.fangjinuo.easyjson.gson;

import com.github.fangjinuo.easyjson.core.JsonException;
import com.github.fangjinuo.easyjson.core.JsonHandler;
import com.github.fangjinuo.easyjson.core.JsonTreeNode;
import com.github.fangjinuo.easyjson.gson.node.GsonBasedJsonTreeNodeMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

public class GsonAdapter implements JsonHandler {
    private GsonBasedJsonTreeNodeMapper mapper = new GsonBasedJsonTreeNodeMapper();
    private Gson gson;

    @Override
    public String serialize(Object src, Type typeOfT) throws JsonException {
        if(src instanceof JsonTreeNode){
            JsonElement element = mapper.mapping((JsonTreeNode) src);
            return gson.toJson(element, element.getClass());
        }
        return gson.toJson(src, typeOfT);
    }

    @Override
    public <T> T deserialize(String json, Type typeOfT) throws JsonException {
        return gson.fromJson(json, typeOfT);
    }

    @Override
    public JsonTreeNode deserialize(String json) throws JsonException {
        JsonElement node = new JsonParser().parse(json);
        return mapper.create(node);
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
