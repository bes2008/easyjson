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

package com.github.fangjinuo.easyjson.gson.typeadapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private DateFormat df = null;
    private boolean usingToString = false;

    public void setPattern(String pattern) {
        if (df != null && pattern != null && !pattern.trim().isEmpty()) {
            df = new SimpleDateFormat(pattern);
        }
    }

    public void setDateFormat(DateFormat df) {
        this.df = df;
    }

    public void setUsingToString(boolean using) {
        usingToString = using;
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject() || json.isJsonArray() || json.isJsonNull()) {
            return null;
        }
        JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
        if (df != null) {
            try {
                return df.parse(jsonPrimitive.getAsString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (usingToString) {
            return new Date(json.getAsString());
        }
        return new Date(json.getAsLong());
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        if(src==null){
            return JsonNull.INSTANCE;
        }
        if (df != null) {
            return new JsonPrimitive(df.format(src));
        }
        if (usingToString) {
            return new JsonPrimitive(src.toString());
        }
        return new JsonPrimitive(src.getTime());
    }
}

