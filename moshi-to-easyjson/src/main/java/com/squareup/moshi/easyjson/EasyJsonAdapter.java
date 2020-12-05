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

package com.squareup.moshi.easyjson;

import com.jn.easyjson.core.JSONBuilder;
import com.jn.langx.annotation.Nullable;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import okio.Buffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class EasyJsonAdapter<E> extends JsonAdapter<E> {
    private JSONBuilder jsonBuilder;
    private Type type;
    private JsonAdapter delegate;

    @Nullable
    @Override
    public E fromJson(JsonReader reader) throws IOException {
        String jsonString = reader.getJsonString();
        return jsonBuilder.build().fromJson(jsonString, type);
    }


    @Override
    public void toJson(JsonWriter writer, @Nullable E value) throws IOException {
        String jsonString = jsonBuilder.build().toJson(value);
        Buffer buffer = new Buffer().readFrom(new ByteArrayInputStream(jsonString.getBytes()));
        writer.value(buffer);
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public JsonAdapter getDelegate() {
        return delegate;
    }

    public void setDelegate(JsonAdapter delegate) {
        this.delegate = delegate;
    }

    public JSONBuilder getJsonBuilder() {
        return jsonBuilder;
    }

    public void setJsonBuilder(JSONBuilder jsonBuilder) {
        this.jsonBuilder = jsonBuilder;
    }

    @Override
    public JsonAdapter<E> lenient() {
        JSONBuilder newBuilder = JSONBuilder.clone(jsonBuilder);
        newBuilder.setLenient(true);

        return wrap(this, newBuilder);
    }

    @Override
    public JsonAdapter<E> serializeNulls() {
        return serializeNulls(true);
    }

    private JsonAdapter<E> serializeNulls(boolean serializeNulls) {
        JSONBuilder newBuilder = JSONBuilder.clone(jsonBuilder);
        newBuilder.serializeNulls(serializeNulls);
        return wrap(this, newBuilder);
    }

    @Override
    public JsonAdapter<E> nullSafe() {
        return serializeNulls();
    }

    @Override
    public JsonAdapter<E> nonNull() {
        return serializeNulls(false);
    }

    @Override
    public JsonAdapter<E> failOnUnknown() {
        return wrap(this, JSONBuilder.clone(jsonBuilder));
    }

    private static EasyJsonAdapter wrap(EasyJsonAdapter jsonAdapter, JSONBuilder jsonBuilder) {
        EasyJsonAdapter wrapper = new EasyJsonAdapter();
        wrapper.delegate = jsonAdapter;
        wrapper.type = jsonAdapter.type;
        wrapper.setJsonBuilder(jsonBuilder);
        return wrapper;
    }

    public boolean isLenient() {
        return jsonBuilder.isLenient();
    }

}
