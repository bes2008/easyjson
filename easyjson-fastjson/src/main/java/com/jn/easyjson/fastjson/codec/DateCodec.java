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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;

public class DateCodec implements ObjectSerializer, ObjectDeserializer, Typed {

    private DateFormat dateFormat;
    private boolean usingToString;


    public void setDateFormat(DateFormat dateFormat) {
        if (dateFormat != null) {
            this.dateFormat = dateFormat;
        }
    }

    public void setUsingToString(boolean usingToString) {
        this.usingToString = usingToString;
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        Date date = parseDate(parser, fieldName);
        if(date==null){
            return null;
        }
        if(clazz == java.sql.Date.class){
            return (T)new java.sql.Date(date.getTime());
        }
        if(clazz == Timestamp.class){
            return (T)new Timestamp(date.getTime());
        }
        return (T)date;
    }

    private Date parseDate(DefaultJSONParser parser, Object fieldName){
        JSONLexer lexer = parser.lexer;

        Date val;
        if (lexer.token() == JSONToken.LITERAL_STRING) {
            String strVal = lexer.stringVal();
            if (dateFormat != null) {
                try {
                    val = dateFormat.parse(strVal);
                    return val;
                } catch (Throwable ex) {
                    val = null;
                }
            }
            if (usingToString) {
                lexer.nextToken(JSONToken.COMMA);
                return new Date(strVal);
            }
        }
        Date r =  new Date(lexer.longValue());
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
            String str = dateFormat.format(date);
            out.writeString(str);
            return;
        }
        if (usingToString) {
            out.writeString(date.toString());
            return;
        }

        long time = date.getTime();
        out.writeLong(time);
    }

    @Override
    public List<Type> applyTo() {
        return Arrays.asList(new Type[]{Date.class, java.sql.Date.class, Timestamp.class});
    }
}
