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

package com.jn.easyjson.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.jn.easyjson.jackson.Jacksons;
import com.jn.langx.util.reflect.type.Primitives;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static com.jn.easyjson.jackson.JacksonConstants.SERIALIZE_LONG_USING_STRING_ATTR_KEY;
import static com.jn.easyjson.jackson.JacksonConstants.SERIALIZE_NUMBER_USING_STRING_ATTR_KEY;

public class NumberDeserializer extends JsonDeserializer<Number> implements ContextualDeserializer {

    private Class<? extends Number> clazz;

    public NumberDeserializer(){}

    public NumberDeserializer(Class clazz){
        this.clazz = clazz;
    }

    @Override
    public Class<?> handledType() {
        return clazz;
    }

    @Override
    public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken curr = p.getCurrentToken();
        if (curr == JsonToken.VALUE_STRING) {
            String v = p.getValueAsString();
            if (Primitives.isLong(clazz)) {
                return Long.parseLong(v);
            }
            if (Primitives.isDouble(clazz)) {
                return Double.parseDouble(v);
            }
            if (Primitives.isInteger(clazz)) {
                return Integer.parseInt(v);
            }
            if (Primitives.isFloat(clazz)) {
                return Float.parseFloat(v);
            }
            if (Primitives.isShort(clazz)) {
                return Short.parseShort(v);
            }
            if (Primitives.isByte(clazz)) {
                return Byte.parseByte(v);
            }
            if (clazz == BigDecimal.class) {
                return p.getDecimalValue();
            }
            if (clazz == BigInteger.class) {
                return p.getBigIntegerValue();
            }
        }
        if (!curr.isNumeric()) {
            return 0;
        }
        Number n = null;
        if (Primitives.isDouble(clazz)) {
            n = p.getDoubleValue();
        } else if (Primitives.isLong(clazz)) {
            n = p.getLongValue();
        } else if (Primitives.isFloat(clazz)) {
            n = p.getFloatValue();
        } else if (Primitives.isInteger(clazz)) {
            n = p.getIntValue();
        } else if (Primitives.isShort(clazz)) {
            n = p.getShortValue();
        } else if (Primitives.isByte(clazz)) {
            n = p.getByteValue();
        } else if (clazz == BigDecimal.class) {
            n = p.getDecimalValue();
        } else if (clazz == BigInteger.class) {
            n = p.getBigIntegerValue();
        }
        if (n != null) {
            return n;
        }
        return 0;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty beanProperty, Class<?> type) throws JsonMappingException {
        if (handledType() == null || (type != null && handledType() != type)) {
            if (Number.class.isAssignableFrom(type)) {
                if (Jacksons.getBooleanAttr(context, SERIALIZE_LONG_USING_STRING_ATTR_KEY) || Jacksons.getBooleanAttr(context, SERIALIZE_NUMBER_USING_STRING_ATTR_KEY)) {
                    NumberDeserializer d = new NumberDeserializer();
                    d.clazz = (Class<Number>) type;
                    return d;
                } else {
                    if (type.getName().startsWith("java.")) {
                        return NumberDeserializers.find(type, type.getName());
                    }
                }
            }
        }
        return null;
    }
}
