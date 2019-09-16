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

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

public class JSONLibDataFormatSerializer implements ObjectSerializer {

    public JSONLibDataFormatSerializer() {
    }

    @SuppressWarnings("deprecation")
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object == null) {
            serializer.out.writeNull();
            return;
        }

        Date date = (Date) object;

        JSONObject json = new JSONObject();
        json.put("date", date.getDate());
        json.put("day", date.getDay());
        json.put("hours", date.getHours());
        json.put("minutes", date.getMinutes());
        json.put("month", date.getMonth());
        json.put("seconds", date.getSeconds());
        json.put("time", date.getTime());
        json.put("timezoneOffset", date.getTimezoneOffset());
        json.put("year", date.getYear());

        serializer.write(json);
    }
}
