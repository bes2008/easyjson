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

package com.jn.easyjson.gson.typeadapter;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Adapts types whose static type is only 'Object'. Uses getClass() on
 * serialization and a primitive/Map/List on deserialization.
 */
public final class ObjectTypeAdapter extends TypeAdapter<Object> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() == Object.class) {
                TypeAdapter delegate = com.google.gson.internal.bind.ObjectTypeAdapter.FACTORY.create(gson, type);
                return (TypeAdapter) new ObjectTypeAdapter(gson, delegate);
            }
            return null;
        }
    };

    private final Gson gson;
    private TypeAdapter delegate;

    ObjectTypeAdapter(Gson gson, TypeAdapter delegate) {
        this.gson = gson;
        this.delegate = delegate;
    }

    ObjectTypeAdapter(Gson gson) {
        this(gson, null);
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        switch (token) {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<Object>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(read(in));
                }
                in.endArray();
                return list;

            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap<String, Object>();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), read(in));
                }
                in.endObject();
                return map;

            case STRING:
                return in.nextString();

            case NUMBER:
                Double dValue = in.nextDouble();
                int iValue = dValue.intValue();
                if (iValue == dValue) {
                    return iValue;
                }

                long lValue = dValue.longValue();
                if (lValue == dValue) {
                    return lValue;
                }

                float fValue = dValue.floatValue();
                if (fValue == dValue) {
                    return fValue;
                }
                return dValue;
            case BOOLEAN:
                return in.nextBoolean();

            case NULL:
                in.nextNull();
                return null;

            default:
                throw new IllegalStateException();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(JsonWriter out, Object value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>) gson.getAdapter(value.getClass());
        if (typeAdapter instanceof com.google.gson.internal.bind.ObjectTypeAdapter) {
            out.beginObject();
            out.endObject();
            return;
        }

        typeAdapter.write(out, value);
    }

    @Override
    public String toString() {
        return "com.jn.easyjson.gson.typeadapter.ObjectTypeAdapter{" +
                "gson=" + gson +
                ", delegate=" + delegate +
                '}';
    }
}
