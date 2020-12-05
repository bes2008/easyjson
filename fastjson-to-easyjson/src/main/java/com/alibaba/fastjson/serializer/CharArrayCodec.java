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

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.Collection;


public class CharArrayCodec implements ObjectDeserializer {

    @SuppressWarnings("unchecked")
    public static <T> T deserialze(DefaultJSONParser parser) {
        final JSONLexer lexer = parser.lexer;
        if (lexer.token() == JSONToken.LITERAL_STRING) {
            String val = lexer.stringVal();
            lexer.nextToken(JSONToken.COMMA);
            return (T) val.toCharArray();
        }

        if (lexer.token() == JSONToken.LITERAL_INT) {
            Number val = lexer.integerValue();
            lexer.nextToken(JSONToken.COMMA);
            return (T) val.toString().toCharArray();
        }

        Object value = parser.parse();

        if (value instanceof String) {
            return (T) ((String) value).toCharArray();
        }

        if (value instanceof Collection) {
            Collection<?> collection = (Collection) value;

            boolean accept = true;
            for (Object item : collection) {
                if (item instanceof String) {
                    int itemLength = ((String) item).length();
                    if (itemLength != 1) {
                        accept = false;
                        break;
                    }
                }
            }

            if (!accept) {
                throw new JSONException("can not cast to char[]");
            }

            char[] chars = new char[collection.size()];
            int pos = 0;
            for (Object item : collection) {
                chars[pos++] = ((String) item).charAt(0);
            }
            return (T) chars;
        }

        return value == null //
                ? null //
                : (T) JSON.toJSONString(value).toCharArray();
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        return (T) deserialze(parser);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
