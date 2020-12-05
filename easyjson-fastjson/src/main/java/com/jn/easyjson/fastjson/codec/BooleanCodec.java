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

package com.jn.easyjson.fastjson.codec;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.util.TypeUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BooleanCodec implements ObjectSerializer, ObjectDeserializer, Typed {
    private boolean using1_0 = false;
    private boolean usingOnOff = false;

    @Override
    public List<Type> applyTo() {
        return Arrays.asList(new Type[]{boolean.class, Boolean.class});
    }

    public void setUsing1_0(boolean using1_0) {
        this.using1_0 = using1_0;
    }

    public void setUsingOnOff(boolean usingOnOff) {
        this.usingOnOff = usingOnOff;
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (usingOnOff) {
            if (evalAsBoolean(object)) {
                out.write("true");
            } else {
                out.write("false");
            }
            return;
        }
        if (using1_0) {
            if (evalAsBoolean(object)) {
                out.write(1);
            } else {
                out.write(0);
            }
            return;
        }

        if (evalAsBoolean(object)) {
            out.write("true");
        } else {
            out.write("false");
        }
    }

    private boolean evalAsBoolean(Object value) {
        if (value == null) {
            return false;
        }
        Boolean v = (Boolean) value;
        return v.booleanValue();
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        final JSONLexer lexer = parser.lexer;

        Boolean boolObj = Boolean.FALSE;

        try {
            if (lexer.token() == JSONToken.TRUE) {
                lexer.nextToken(JSONToken.COMMA);
                boolObj = Boolean.TRUE;
            } else if (lexer.token() == JSONToken.FALSE) {
                lexer.nextToken(JSONToken.COMMA);
                boolObj = Boolean.FALSE;
            } else if (lexer.token() == JSONToken.LITERAL_INT) {
                int intValue = lexer.intValue();
                lexer.nextToken(JSONToken.COMMA);

                if (intValue == 1) {
                    boolObj = Boolean.TRUE;
                } else {
                    boolObj = Boolean.FALSE;
                }
            } else {
                Object value = parser.parse();

                if (value == null) {
                    return null;
                }
                boolean goon = true;
                if (usingOnOff) {
                    if (value instanceof String) {
                        if (value.equals("on")) {
                            boolObj = Boolean.TRUE;
                            goon = false;
                        } else if (value.equals("off")) {
                            boolObj = Boolean.FALSE;
                            goon = false;
                        }
                    }
                }
                if (goon) {
                    boolObj = TypeUtils.castToBoolean(value);
                }
            }
        } catch (Exception ex) {
            throw new JSONException("parseBoolean error, field : " + fieldName, ex);
        }

        if (clazz == AtomicBoolean.class) {
            return (T) new AtomicBoolean(boolObj.booleanValue());
        }

        return (T) boolObj;
    }


    public int getFastMatchToken() {
        return JSONToken.TRUE;
    }
}
