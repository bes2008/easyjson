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

import com.alibaba.fastjson.serializer.*;
import com.jn.easyjson.fastjson.codec.Typed;
import com.jn.langx.util.Emptys;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FastJsonSerializerBuilder {
    private SerializeConfig config;
    private List<SerializeFilter> filters = new ArrayList<SerializeFilter>();
    private List<SerializerFeature> features = new ArrayList<SerializerFeature>();

    public FastJsonSerializerBuilder config(SerializeConfig config) {
        this.config = config;
        return this;
    }

    public SerializeConfig config() {
        return this.config;
    }

    public FastJsonSerializerBuilder addFilter(SerializeFilter filter) {
        if (filter != null) {
            this.filters.add(filter);
        }
        return this;
    }

    public FastJsonSerializerBuilder addFeature(SerializerFeature feature) {
        if (feature != null) {
            this.features.add(feature);
        }
        return this;
    }

    public FastJsonSerializerBuilder apply(ObjectSerializer serializer) {
        if (serializer instanceof Typed) {
            List<Type> applyTo = ((Typed) serializer).applyTo();
            if (Emptys.isNotEmpty(applyTo)) {
                for (Type type : applyTo) {
                    apply(type, serializer);
                }
            }
        }
        return this;
    }

    public FastJsonSerializerBuilder apply(Type type, ObjectSerializer serializer) {
        config.put(type, serializer);
        return this;
    }

    public JSONSerializer build() {
        if (config == null) {
            throw new RuntimeException();
        }
        SerializeWriter out = new SerializeWriter(null, com.alibaba.fastjson.JSON.DEFAULT_GENERATE_FEATURE, features.toArray(new SerializerFeature[features.size()]));
        JSONSerializer serializer = new JSONSerializer(out, config);
        if (filters != null) {
            for (SerializeFilter filter : filters) {
                serializer.addFilter(filter);
            }
        }
        return serializer;
    }
}
