package com.github.fangjinuo.easyjson.gson.typeadapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class BooleanTypeAdapter implements JsonSerializer<Boolean>, JsonDeserializer<Boolean> {
    private static final List<String> evalFalses = Arrays.asList(new String[]{"false", "off", "0"});
    private boolean using1_0 = false;
    private boolean usingOnOff = false;

    public boolean isUsing1_0() {
        return using1_0;
    }

    public void setUsing1_0(boolean using1_0) {
        this.using1_0 = using1_0;
    }

    public boolean isUsingOnOff() {
        return usingOnOff;
    }

    public void setUsingOnOff(boolean usingOnOff) {
        this.usingOnOff = usingOnOff;
    }

    @Override
    public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) {
            return false;
        }
        if (json.isJsonArray() || json.isJsonObject()) {
            return true;
        }
        JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
        if (jsonPrimitive.isString()) {
            String vstring = jsonPrimitive.getAsString().toLowerCase();
            if (evalFalses.contains(vstring)) {
                return false;
            }
            return true;
        }
        if(jsonPrimitive.isNumber()){
            return jsonPrimitive.getAsInt()!=0;
        }
        if (jsonPrimitive.isBoolean()) {
            return jsonPrimitive.getAsBoolean();
        }
        return true;
    }

    @Override
    public JsonElement serialize(Boolean src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
