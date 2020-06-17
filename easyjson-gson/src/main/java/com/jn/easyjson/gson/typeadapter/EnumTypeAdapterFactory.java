/*
 * Copyright 2020 the original author or authors.
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

package com.jn.easyjson.gson.typeadapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.jn.easyjson.gson.GsonJSONBuilder;
import com.jn.langx.util.Preconditions;

public class EnumTypeAdapterFactory implements TypeAdapterFactory {

    private GsonJSONBuilder jsonBuilder;
    public EnumTypeAdapterFactory(GsonJSONBuilder jsonBuilder){
        Preconditions.checkNotNull(jsonBuilder);
        this.jsonBuilder = jsonBuilder;
    }
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if(type.getRawType().isEnum()) {
            EnumTypeAdapter enumTypeAdapter = new EnumTypeAdapter();
            enumTypeAdapter.setUsingValue(jsonBuilder.serializeEnumUsingIndex());
            enumTypeAdapter.setUsingField(jsonBuilder.serializeEnumUsingField());
            enumTypeAdapter.setUsingToString(jsonBuilder.serializeEnumUsingToString());
            enumTypeAdapter.setJSONBuilder(jsonBuilder);
            enumTypeAdapter.setEnumClass(type.getRawType());
            return (TypeAdapter<T>)enumTypeAdapter;
        }
        return null;
    }
}
