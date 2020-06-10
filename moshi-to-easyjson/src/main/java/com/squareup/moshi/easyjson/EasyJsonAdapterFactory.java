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

package com.squareup.moshi.easyjson;

import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

public class EasyJsonAdapterFactory implements JsonAdapter.Factory {

    private JsonAdapter.Factory delegate;

    public EasyJsonAdapterFactory(JsonAdapter.Factory delegate) {
        this.delegate = delegate;
    }

    @Nullable
    @Override
    public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
        JsonAdapter adapterDelegate = delegate.create(type, annotations, moshi);
        if(adapterDelegate==null){
            return null;
        }
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        EasyJsonAdapter adapter = new EasyJsonAdapter();
        adapter.setJsonBuilder(jsonBuilder);
        adapter.setDelegate(adapterDelegate);
        adapter.setType(type);
        return adapter;
    }
}
