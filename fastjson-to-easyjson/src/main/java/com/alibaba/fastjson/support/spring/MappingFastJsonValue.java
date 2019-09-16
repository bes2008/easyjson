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

package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * A simple holder for the POJO to serialize via {@link FastJsonHttpMessageConverter} along with further
 * serialization instructions to be passed in to the converter.
 * <p>
 * <p>
 * On the server side this wrapper is added with a {@code ResponseBodyInterceptor} after content negotiation selects the
 * converter to use but before the write.
 * <p>
 * <p>
 * On the client side, simply wrap the POJO and pass it in to the {@code RestTemplate}.
 *
 * @author Jerry.Chen
 * @see JSONPObject
 * @since 1.2.20
 */
@Deprecated
public class MappingFastJsonValue implements JSONSerializable {
    private static final String SECURITY_PREFIX = "/**/";
    private static final int BrowserSecureMask = SerializerFeature.BrowserSecure.mask;

    private Object value;
    private String jsonpFunction;

    /**
     * Create a new instance wrapping the given POJO to be serialized.
     *
     * @param value the Object to be serialized
     */
    public MappingFastJsonValue(Object value) {
        this.value = value;
    }

    /**
     * Modify the POJO to serialize.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Return the POJO that needs to be serialized.
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Set the name of the JSONP function name.
     */
    public void setJsonpFunction(String functionName) {
        this.jsonpFunction = functionName;
    }

    /**
     * Return the configured JSONP function name.
     */
    public String getJsonpFunction() {
        return this.jsonpFunction;
    }

    public void write(JSONSerializer serializer, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter writer = serializer.out;

        if (jsonpFunction == null) {
            serializer.write(value);
            return;
        }


        if ((features & BrowserSecureMask) != 0 || (writer.isEnabled(BrowserSecureMask))) {
            writer.write(SECURITY_PREFIX);
        }

        writer.write(jsonpFunction);
        writer.write('(');
        serializer.write(value);
        writer.write(')');
    }
}
