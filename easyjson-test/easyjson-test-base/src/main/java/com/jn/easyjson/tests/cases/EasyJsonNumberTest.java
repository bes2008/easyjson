package com.jn.easyjson.tests.cases;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.tests.utils.CompareTools;
import com.jn.langx.util.reflect.type.Types;

public abstract class EasyJsonNumberTest extends AbstractBaseTest {

    private static JSON json = JSONBuilderProvider.simplest();

    protected String getStringNumberMap1String() {
        return readClassResourceText(EasyJsonBaseTest.class, "json/NumberWithStringValue.json");
    }

    protected String getOritinalNumberMap2String() {
        return readClassResourceText(EasyJsonBaseTest.class, "json/NumberWithOriginalValue.json");
    }

    protected Map<String, Integer> getIntegerMap1Object() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number1", 0);
        map.put("number2", 1);
        map.put("number3", 128);
        map.put("number4", 10000);
        map.put("number5", 10000);
        map.put("number6", (int) 1423232322323534L);
        map.put("number7", 2);
        map.put("number8", 1000000000);
        return map;
    }

    protected Map<String, Double> getDoubleMap2Object() {
        Map<String, Double> map = new HashMap<>();
        map.put("number1", 0D);
        map.put("number2", 1.0);
        map.put("number3", 128.0);
        map.put("number4", 10000.0);
        map.put("number5", 10000.0000003);
        map.put("number6", 1423232322323534.0);
        map.put("number7", 1.9999);
        map.put("number8", 999999999.999999999);
        return map;
    }

    @Test(priority = 10001)
    public void testSerializeOritinalNumber10001() {
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        jsonBuilder.serializeNumberAsString(false);
        JSON json = jsonBuilder.build();
        Map<String, Double> map = getDoubleMap2Object();
        String actual = json.toJson(map);
        System.out.println(actual);
        String expected = getOritinalNumberMap2String();
        CompareTools.assertJsonEquals(actual, expected);
    }

    @Test(priority = 10002)
    public void testSerializeOritinalNumberAsString10002() {
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        jsonBuilder.serializeNumberAsString(true);
        JSON json = jsonBuilder.build();
        String actual = json.toJson(getDoubleMap2Object());
        System.out.println(actual);
        String expected = getStringNumberMap1String();
        CompareTools.assertJsonEquals(actual, expected);
    }

    @Test(priority = 10101)
    public void testDeserializeStringNumberToDouble10101() {
        String jsonString = getStringNumberMap1String();
        ParameterizedType type = Types.getMapParameterizedType(String.class, Double.class);
        Map<String, Double> actual = json.fromJson(jsonString, type);
        Map<String, Double> expected = getDoubleMap2Object();
        CompareTools.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10102)
    public void testDeserializeOritinalNumberToDouble10102() {
        String jsonString = getOritinalNumberMap2String();
        ParameterizedType type = Types.getMapParameterizedType(String.class, Double.class);
        Map<String, Double> actual = json.fromJson(jsonString, type);
        Map<String, Double> expected = getDoubleMap2Object();
        CompareTools.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10103)
    public void testDeserializeStringNumberToInteger10103() {
        String jsonString = getStringNumberMap1String();
        ParameterizedType type = Types.getMapParameterizedType(String.class, Integer.class);
        Map<String, Integer> actual = json.fromJson(jsonString, type);
        Map<String, Integer> expected = getIntegerMap1Object();
        CompareTools.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10104)
    public void testDeserializeOritinalNumberToInteger10104() {
        String jsonString = getOritinalNumberMap2String();
        ParameterizedType type = Types.getMapParameterizedType(String.class, Integer.class);
        Map<String, Integer> actual = json.fromJson(jsonString, type);
        Map<String, Integer> expected = getIntegerMap1Object();
        CompareTools.assertDeepEquals(actual, expected);
    }
}
