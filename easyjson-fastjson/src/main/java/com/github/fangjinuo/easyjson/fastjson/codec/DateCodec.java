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

import com.alibaba.fastjson.JSON;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateCodec implements ObjectSerializer, ObjectDeserializer, Typed {

    private DateFormat dateFormat;
    private boolean usingToString;

    public void setDatePattern(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            return;
        }
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(pattern);
        }
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setUsingToString(boolean usingToString) {
        this.usingToString = usingToString;
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        JSONLexer lexer = parser.lexer;

        Object val;
        if (lexer.token() == JSONToken.LITERAL_STRING) {
            String strVal = lexer.stringVal();
            if (dateFormat != null) {
                try {
                    val = dateFormat.parse(strVal);
                    return (T) val;
                } catch (Throwable ex) {
                    val = null;
                }
            }
            if (usingToString) {
                lexer.nextToken(JSONToken.COMMA);
                return (T) new Date(strVal);
            }
        }
        T r = (T) new Date(lexer.longValue());
        lexer.nextToken(JSONToken.COMMA);
        return r;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        Class<?> clazz = object.getClass();
        if (clazz == java.sql.Date.class) {
            long millis = ((java.sql.Date) object).getTime();
            TimeZone timeZone = JSON.defaultTimeZone;
            ;
            int offset = timeZone.getOffset(millis);
            if (millis % offset == 0) {
                out.writeString(object.toString());
                return;
            }
        }

        if (clazz == java.sql.Time.class) {
            long millis = ((java.sql.Time) object).getTime();
            if (millis < 24L * 60L * 60L * 1000L) {
                out.writeString(object.toString());
                return;
            }
        }

        Date date = (object instanceof Date) ? (Date) object : TypeUtils.castToDate(object);
        if (dateFormat != null) {
            out.write(dateFormat.format(date));
            return;
        }
        if (usingToString) {
            out.write(date.toString());
            return;
        }

        long time = date.getTime();
        out.writeLong(time);
    }

    @Override
    public List<Type> applyTo() {
        return Arrays.asList(new Type[]{Date.class, Calendar.class});
    }
}
