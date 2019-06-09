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
    private final boolean usingTimestamp = true;

    public void setPattern(String pattern) {
        if (pattern != null && !pattern.trim().isEmpty()) {
            df = new SimpleDateFormat(pattern);
        }
    }

    public void setUsingToString(boolean using) {
        usingToString = using;
    }

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

    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        if (df != null) {
            return new JsonPrimitive(df.format(src));
        }
        if (usingToString) {
            return new JsonPrimitive(src.toString());
        }
        return new JsonPrimitive(src.getTime());
    }
}

