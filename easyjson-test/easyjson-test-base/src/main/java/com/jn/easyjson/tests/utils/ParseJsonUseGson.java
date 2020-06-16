package com.jn.easyjson.tests.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jn.langx.util.reflect.Reflects;

/**
 * 使用Gson解析json字符串
 *
 * @author zhaohuihua
 * @version 20200616
 */
public class ParseJsonUseGson {

    /** 将JsonObject字符串解析为Map对象 **/
    public static Map<String, Object> parseObjectString(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        JsonElement element = new JsonParser().parse(jsonString);
        if (element instanceof JsonNull) {
            return null;
        }
        if (!(element instanceof JsonObject)) {
            throw new IllegalArgumentException("Json object string format error: " + jsonString);
        }
        return unwrapJsonObject((JsonObject) element);
    }

    /** 将JsonArray字符串解析为List对象 **/
    public static List<Object> parseArrayString(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        JsonElement element = new JsonParser().parse(jsonString);
        if (element instanceof JsonNull) {
            return null;
        }
        if (!(element instanceof JsonArray)) {
            throw new IllegalArgumentException("Json array string format error: " + jsonString);
        }
        return unwrapJsonArray((JsonArray) element);
    }

    private static Object unwrapJsonElement(JsonElement element) {
        if (element instanceof JsonNull) {
            return null;
        } else if (element instanceof JsonPrimitive) {
            return unwrapJsonPrimitive((JsonPrimitive) element);
        } else if (element instanceof JsonObject) {
            return unwrapJsonObject((JsonObject) element);
        } else if (element instanceof JsonArray) {
            return unwrapJsonArray((JsonArray) element);
        } else {
            // 目前JsonElement只有以上4个子类
            return element;
        }
    }

    /** 去掉ValueNode的包装, 直接返回内容 **/
    private static Object unwrapJsonPrimitive(JsonPrimitive element) {
        // 略坑, 居然没有直接获取value值的public方法
        JsonPrimitive primitive = (JsonPrimitive) element;
        return Reflects.getAnyFieldValue(primitive, "value", true, true);
    }

    /** 去掉JsonObject的包装, 直接返回Map内容 **/
    private static Map<String, Object> unwrapJsonObject(JsonObject json) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            JsonElement value = entry.getValue();
            map.put(entry.getKey(), unwrapJsonElement(value));
        }
        return map;
    }

    /** 去掉JsonArray的包装, 直接返回List内容 **/
    private static List<Object> unwrapJsonArray(JsonArray array) {
        List<Object> list = new ArrayList<>();
        Iterator<JsonElement> iterator = array.iterator();
        while (iterator.hasNext()) {
            list.add(unwrapJsonElement(iterator.next()));
        }
        return list;
    }
}
