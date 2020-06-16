package com.jn.easyjson.tests.utils;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 使用Fastjson解析json字符串
 *
 * @author zhaohuihua
 * @version 20200616
 */
public class ParseJsonUseFastjson {

    public static Map<String, Object> parseObjectString(String jsonString) {
        return JSONObject.parseObject(jsonString);
    }

    public static List<Object> parseArrayString(String jsonString) {
        return JSONArray.parseArray(jsonString);
    }
}
