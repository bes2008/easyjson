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

package com.github.fangjinuo.easyjson.gson;

import com.github.fangjinuo.easyjson.core.JSON;
import com.github.fangjinuo.easyjson.core.JSONBuilder;
import com.github.fangjinuo.easyjson.core.annotation.DependOn;
import com.github.fangjinuo.easyjson.core.annotation.Name;
import com.github.fangjinuo.easyjson.gson.typeadapter.BooleanTypeAdapter;
import com.github.fangjinuo.easyjson.gson.typeadapter.DateTypeAdapter;
import com.github.fangjinuo.easyjson.gson.typeadapter.EnumTypeAdapter;
import com.github.fangjinuo.easyjson.gson.typeadapter.NumberTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

@Name("gson")
@DependOn("com.google.gson.Gson")
public class GsonJSONBuilder extends JSONBuilder {
    public JSON build() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Null
        if (serializeNulls) {
            gsonBuilder.serializeNulls();
        }

        // Boolean
        BooleanTypeAdapter booleanTypeAdapter = new BooleanTypeAdapter();
        booleanTypeAdapter.setUsing1_0(serializeBooleanUsing1_0);
        booleanTypeAdapter.setUsingOnOff(serializeBooleanUsingOnOff);
        gsonBuilder.registerTypeHierarchyAdapter(Boolean.class, booleanTypeAdapter);

        // Number
        NumberTypeAdapter numberTypeAdapter = new NumberTypeAdapter();
        numberTypeAdapter.setLongUsingString(serializeLongAsString);
        numberTypeAdapter.setUsingString(serializeNumberAsString);
        gsonBuilder.registerTypeHierarchyAdapter(Number.class, numberTypeAdapter);

        // Date
        DateTypeAdapter dateTypeAdapter = new DateTypeAdapter();
        dateTypeAdapter.setDateFormat(dateFormat);
        dateTypeAdapter.setPattern(serializeDateUsingPattern);
        dateTypeAdapter.setUsingToString(serializeDateUsingToString);
        gsonBuilder.registerTypeHierarchyAdapter(Date.class, dateTypeAdapter);

        // Enum
        EnumTypeAdapter enumTypeAdapter = new EnumTypeAdapter();
        enumTypeAdapter.setUsingValue(serializeEnumUsingValue);
        enumTypeAdapter.setUsingField(serializeEnumUsingField);
        enumTypeAdapter.setUsingToString(serializeEnumUsingToString);
        gsonBuilder.registerTypeHierarchyAdapter(Enum.class, enumTypeAdapter);

        if (prettyFormat) {
            gsonBuilder.setPrettyPrinting();
        }

        Gson gson = gsonBuilder.create();
        JSON json = new JSON();
        GsonAdapter gsonAdapter = new GsonAdapter();
        gsonAdapter.setGson(gson);
        json.setJsonHandler(gsonAdapter);
        return json;
    }
}
