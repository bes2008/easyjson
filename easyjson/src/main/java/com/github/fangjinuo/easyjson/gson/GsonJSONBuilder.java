package com.github.fangjinuo.easyjson.gson;

import com.github.fangjinuo.easyjson.core.JSON;
import com.github.fangjinuo.easyjson.core.JSONBuilder;
import com.github.fangjinuo.easyjson.gson.typeadapter.BooleanTypeAdapter;
import com.github.fangjinuo.easyjson.gson.typeadapter.DateTypeAdapter;
import com.github.fangjinuo.easyjson.gson.typeadapter.EnumTypeAdapter;
import com.github.fangjinuo.easyjson.gson.typeadapter.NumberTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

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
