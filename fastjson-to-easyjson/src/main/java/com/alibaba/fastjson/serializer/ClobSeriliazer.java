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

import com.alibaba.fastjson.JSONException;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.sql.SQLException;

public class ClobSeriliazer implements ObjectSerializer {

    public final static ClobSeriliazer instance = new ClobSeriliazer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        try {
            if (object == null) {
                serializer.writeNull();
                return;
            }

            Clob clob = (Clob) object;
            Reader reader = clob.getCharacterStream();

            StringBuilder buf = new StringBuilder();

            try {
                char[] chars = new char[2048];
                for (; ; ) {
                    int len = reader.read(chars, 0, chars.length);
                    if (len < 0) {
                        break;
                    }
                    buf.append(chars, 0, len);
                }
            } catch (Exception ex) {
                throw new JSONException("read string from reader error", ex);
            }

            String text = buf.toString();
            reader.close();
            serializer.write(text);
        } catch (SQLException e) {
            throw new IOException("write clob error", e);
        }
    }

}
