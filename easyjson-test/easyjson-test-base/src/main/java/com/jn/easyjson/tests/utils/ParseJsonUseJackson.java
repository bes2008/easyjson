package com.jn.easyjson.tests.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

/**
 * 使用Jackson解析json字符串
 *
 * @author zhaohuihua
 * @version 20200616
 */
public class ParseJsonUseJackson {

    public static Map<String, Object> parseObjectString(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        try {
            JsonNode node = new ObjectMapper().readTree(jsonString);
            if (node == null || node.isEmpty()) {
                return null;
            }
            if (node instanceof NullNode) {
                return null;
            }
            if (!(node instanceof ObjectNode)) {
                throw new IllegalArgumentException("Json object string format error: " + jsonString);
            }
            return unwrapJsonObject((ObjectNode) node);

        } catch (Exception e) {
            throw new IllegalArgumentException("Json string format error: " + jsonString);
        }
    }

    /** 将JsonArray字符串解析为List对象 **/
    public static List<Object> parseArrayString(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        try {
            JsonNode node = new ObjectMapper().readTree(jsonString);
            if (node == null || node.isEmpty()) {
                return null;
            }
            if (node instanceof NullNode) {
                return null;
            }
            if (!(node instanceof ArrayNode)) {
                throw new IllegalArgumentException("Json array string format error: " + jsonString);
            }
            return unwrapJsonArray((ArrayNode) node);
        } catch (Exception e) {
            throw new IllegalArgumentException("Json string format error: " + jsonString);
        }
    }

    private static Object unwrapJsonNode(JsonNode node) {
        if (node == null || node.isEmpty()) {
            return null;
        } else if (node instanceof ValueNode) {
            return unwrapJsonPrimitive((ValueNode) node);
        } else if (node instanceof ObjectNode) {
            return unwrapJsonObject((ObjectNode) node);
        } else if (node instanceof ArrayNode) {
            return unwrapJsonArray((ArrayNode) node);
        } else {
            // 目前JsonNode只有以上几种情况
            return node;
        }
    }

    /** 去掉ValueNode的包装, 直接返回内容 **/
    private static Object unwrapJsonPrimitive(ValueNode node) {
        if (node instanceof MissingNode) {
            return null;
        } else if (node instanceof NullNode) {
            return null;
        } else if (node instanceof BinaryNode) {
            return ((BinaryNode) node).binaryValue();
        } else if (node instanceof BooleanNode) {
            return ((BooleanNode) node).booleanValue();
        } else if (node instanceof NumericNode) {
            return ((NumericNode) node).numberValue();
        } else if (node instanceof POJONode) {
            return ((POJONode) node).getPojo();
        } else if (node instanceof TextNode) {
            return ((TextNode) node).textValue();
        } else {
            // 目前只有上面这几种情况
            return node;
        }
    }

    /** 去掉JsonObject的包装, 直接返回Map内容 **/
    private static Map<String, Object> unwrapJsonObject(ObjectNode json) {
        Map<String, Object> map = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> iterator = json.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            JsonNode value = entry.getValue();
            map.put(entry.getKey(), unwrapJsonNode(value));
        }
        return map;
    }

    /** 去掉JsonArray的包装, 直接返回List内容 **/
    private static List<Object> unwrapJsonArray(ArrayNode array) {
        List<Object> list = new ArrayList<>();
        Iterator<JsonNode> iterator = array.elements();
        while (iterator.hasNext()) {
            list.add(unwrapJsonNode(iterator.next()));
        }
        return list;
    }
}
