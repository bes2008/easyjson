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

package com.github.fangjinuo.easyjson.fastjson.codec;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumberCodec implements ObjectSerializer, ObjectDeserializer, Typed {
    private boolean longUsingString;
    private boolean usingString;

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONLexer lexer = parser.lexer;
        final int token = lexer.token();
        if (token == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }
        Number n = null;
        if (token == JSONToken.LITERAL_STRING) {
            String value = lexer.stringVal();
            if (type == null) {
                if (n == null) {
                    try {
                        n = Integer.parseInt(value);
                    } catch (NumberFormatException ex) {
                    }
                }
                if (n == null) {
                    try {
                        n = Long.parseLong(value);
                    } catch (NumberFormatException ex) {

                    }
                }
                if (n == null) {
                    try {
                        n = Float.parseFloat(value);
                    } catch (NumberFormatException ex) {

                    }
                }
                if (n == null) {
                    try {
                        n = Double.parseDouble(value);
                    } catch (NumberFormatException ex) {

                    }
                }
                if (n == null) {
                    try {
                        n = Byte.parseByte(value);
                    } catch (NumberFormatException ex) {

                    }
                }
                if (n == null) {
                    try {
                        n = Short.parseShort(value);
                    } catch (NumberFormatException ex) {

                    }
                }
            } else {
                if (asLong.contains(type)) {
                    n = Long.parseLong(value);
                }
                if (n == null) {
                    if (asFloat.contains(type)) {
                        n = Float.parseFloat(value);
                    }
                }
                if (n == null) {
                    if (asDouble.contains(type)) {
                        n = Double.parseDouble(value);
                    }
                }
                if (n == null) {
                    if (int.class == type || Integer.class == type) {
                        n = Integer.parseInt(value);
                    }
                }
                if (n == null) {
                    if (byte.class == type || Byte.class == type) {
                        n = Integer.parseInt(value);
                    }
                }
                if (n == null) {
                    if (short.class == type || Short.class == type) {
                        n = Integer.parseInt(value);
                    }
                }
            }
            lexer.nextToken(JSONToken.COMMA);
        }
        if (n != null) {
            return (T) n;
        }
        return NumberDeserializer.instance.deserialze(parser, type, fieldName);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }

    @Override
    public void write(JSONSerializer serializer, Object value, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (value == null) {
            out.writeNull();
            return;
        }

        fieldType = fieldType == null ? value.getClass() : fieldType;
        if (longUsingString) {
            if (asLong.contains(fieldType)) {
                out.write("\"" + value.toString() + "\"");
                return;
            }
        }
        if (usingString) {
            out.write("\"" + value.toString() + "\"");
            return;
        }
        Number n = (Number) value;
        if (asInt.contains(fieldType)) {
            out.writeInt(n.intValue());
            return;
        }
        if (asFloat.contains(fieldType)) {
            out.writeFloat(n.floatValue(), false);
            return;
        }
        if (asDouble.contains(fieldType)) {
            out.writeDouble(n.doubleValue(), false);
            return;
        }
        if (asLong.contains(fieldType)) {
            out.writeLong(n.longValue());
            return;
        }
    }

    private static List<Class> asInt = Arrays.asList(new Class[]{
            byte.class,
            Byte.class,
            short.class,
            Short.class,
            int.class,
            Integer.class,
    });
    private static List<Class> asFloat = Arrays.asList(new Class[]{
            float.class,
            Float.class
    });
    private static List<Class> asDouble = Arrays.asList(new Class[]{
            double.class,
            Double.class
    });

    private static List<Class> asLong = Arrays.asList(new Class[]{
            long.class,
            Long.class
    });

    @Override
    public List<Type> applyTo() {
        List<Type> res = new ArrayList<Type>(asInt);
        res.addAll(asDouble);
        res.addAll(asFloat);
        res.addAll(asLong);
        return res;
    }

    public void setLongUsingString(boolean longUsingString) {
        this.longUsingString = longUsingString;
    }

    public void setUsingString(boolean usingString) {
        this.usingString = usingString;
    }
}
