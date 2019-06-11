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

package com.github.fangjinuo.easyjson.gson;

import com.github.fangjinuo.easyjson.api.JsonException;
import com.github.fangjinuo.easyjson.api.JsonHandler;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonAdapter implements JsonHandler {

    private Gson gson;

    public String serialize(Object src, Type typeOfT) throws JsonException {
        return gson.toJson(src, typeOfT);
    }

    public <T> T deserialize(String json, Type typeOfT) throws JsonException {
        return gson.fromJson(json, typeOfT);
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
