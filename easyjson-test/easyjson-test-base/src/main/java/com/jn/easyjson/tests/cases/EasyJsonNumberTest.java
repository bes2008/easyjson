package com.jn.easyjson.tests.cases;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;
import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.tests.utils.Asserts;
import com.jn.langx.util.reflect.type.Types;

public abstract class EasyJsonNumberTest extends AbstractBaseTest {

    private static JSON json = JSONBuilderProvider.simplest();

    protected List<Integer> getIntegralNumbers() {
        // @formatter:off
        return Arrays.asList(0, 1, -1, 
            999999999, -999999999, Integer.MAX_VALUE, Integer.MIN_VALUE);
        // @formatter:on
    }

    @Test(priority = 10001)
    public void testSerializeIntegralNumber10001() {
        List<Integer> numbers = getIntegralNumbers();
        List<String> expected = new ArrayList<>();
        List<String> actual = new ArrayList<>();
        for (Integer number : numbers) {
            expected.add(String.valueOf(number));
            // 数字转字符串
            actual.add(json.toJson(number));
        }
        Asserts.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10002)
    public void testDeserializeIntegralNumber10002() {
        List<Integer> numbers = getIntegralNumbers();
        List<Integer> expected = new ArrayList<>();
        List<Integer> actual = new ArrayList<>();
        for (Integer number : numbers) {
            expected.add(number);
            // 字符串转数字
            actual.add(json.fromJson(String.valueOf(number), int.class));
        }
        Asserts.assertDeepEquals(actual, expected);
    }

    protected List<Double> getDoubleNumbers() {
        // @formatter:off
        return Arrays.asList(0.0, 1.0, -1.0, 
            999999999.0, -999999999.0, 0.99999999, 1.99999999, -1.99999999, 
            999999999.99999999, -999999999.99999999, 
            Double.MAX_VALUE, Double.MIN_VALUE);
        // @formatter:on
    }

    @Test(priority = 10003)
    public void testSerializeDoubleNumber10003() {
        List<Double> numbers = getDoubleNumbers();
        List<String> expected = new ArrayList<>();
        List<String> actual = new ArrayList<>();
        for (Double number : numbers) {
            expected.add(String.valueOf(number));
            // 数字转字符串
            actual.add(json.toJson(number));
        }
        Asserts.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10004)
    public void testDeserializeDoubleNumber10004() {
        List<Double> numbers = getDoubleNumbers();
        List<Double> expected = new ArrayList<>();
        List<Double> actual = new ArrayList<>();
        for (Double number : numbers) {
            expected.add(number);
            // 字符串转数字
            actual.add(json.fromJson(String.valueOf(number), double.class));
        }
        Asserts.assertDeepEquals(actual, expected);
    }

    protected String getStringNumberMap1String() {
        return readClassResourceText(EasyJsonBaseTest.class, "json/NumberWithStringValue.json");
    }

    protected String getOritinalNumberMap2String() {
        return readClassResourceText(EasyJsonBaseTest.class, "json/NumberWithOriginalValue.json");
    }

    protected Map<String, Double> getDoubleMap2Object() {
        Map<String, Double> map = new HashMap<>();
        map.put("number1", 0.0);
        map.put("number2", 1.0);
        map.put("number3", 128.0);
        map.put("number4", 10000.0);
        map.put("number5", 10000.0000003);
        map.put("number6", 1.9999);
        map.put("number7", 1423232322323534.0);
        map.put("number8", 999999999.999999999);
        return map;
    }

    @Test(priority = 10101)
    public void testDeserializeStringNumberToDouble10101() {
        String jsonString = getStringNumberMap1String();
        ParameterizedType type = Types.getMapParameterizedType(String.class, Double.class);
        Map<String, Double> actual = json.fromJson(jsonString, type);
        Map<String, Double> expected = getDoubleMap2Object();
        Asserts.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10102)
    public void testDeserializeOritinalNumberToDouble10102() {
        String jsonString = getOritinalNumberMap2String();
        ParameterizedType type = Types.getMapParameterizedType(String.class, Double.class);
        Map<String, Double> actual = json.fromJson(jsonString, type);
        Map<String, Double> expected = getDoubleMap2Object();
        Asserts.assertDeepEquals(actual, expected);
    }

}
