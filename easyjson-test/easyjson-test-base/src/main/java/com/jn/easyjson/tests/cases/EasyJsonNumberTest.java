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

    protected String getNumberMap1String() {
        return readClassResourceText(EasyJsonBaseTest.class, "json/Number1.json");
    }

    protected String getNumberMap2String() {
        return readClassResourceText(EasyJsonBaseTest.class, "json/Number2.json");
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

    protected Map<String, Number> getNumberMap2Object() {
        Map<String, Number> map = new HashMap<>();
        map.put("number1", 0);
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
    public void testSerialize10001() {
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        jsonBuilder.serializeNumberAsString(false);
        JSON json = jsonBuilder.build();
        String actual = json.toJson(getNumberMap2Object());
        System.out.println(actual);
        String expected = getNumberMap2String();
        CompareTools.assertJsonEquals(actual, expected);
    }

    @Test(priority = 10002)
    public void testSerialize10002() {
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        jsonBuilder.serializeNumberAsString(true);
        JSON json = jsonBuilder.build();
        String actual = json.toJson(getNumberMap2Object());
        System.out.println(actual);
        String expected = getNumberMap1String();
        CompareTools.assertJsonEquals(actual, expected);
    }

    @Test(priority = 10101)
    public void testDeserialize10101() {
        String jsonString = getNumberMap1String();
        ParameterizedType type = Types.getMapParameterizedType(String.class, Number.class);
        Map<String, Number> actual = json.fromJson(jsonString, type);
        Map<String, Number> expected = getNumberMap2Object();
        CompareTools.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10102)
    public void testDeserialize10102() {
        String jsonString = getNumberMap2String();
        ParameterizedType type = Types.getMapParameterizedType(String.class, Number.class);
        Map<String, Number> actual = json.fromJson(jsonString, type);
        Map<String, Number> expected = getNumberMap2Object();
        CompareTools.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10103)
    public void testDeserialize10103() {
        String jsonString = getNumberMap1String();
        ParameterizedType type = Types.getMapParameterizedType(String.class, Integer.class);
        Map<String, Integer> actual = json.fromJson(jsonString, type);
        Map<String, Integer> expected = getIntegerMap1Object();
        CompareTools.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10104)
    public void testDeserialize10104() {
        String jsonString = getNumberMap2String();
        ParameterizedType type = Types.getMapParameterizedType(String.class, Integer.class);
        Map<String, Integer> actual = json.fromJson(jsonString, type);
        Map<String, Integer> expected = getIntegerMap1Object();
        CompareTools.assertDeepEquals(actual, expected);
    }
}
