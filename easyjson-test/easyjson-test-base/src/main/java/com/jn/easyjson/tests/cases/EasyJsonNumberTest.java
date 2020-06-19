package com.jn.easyjson.tests.cases;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.tests.utils.Asserts;
import com.jn.langx.util.reflect.type.Types;

public abstract class EasyJsonNumberTest extends AbstractBaseTest {

    private static JSON json = JSONBuilderProvider.simplest();

    @Test(priority = 10001)
    public void testSerializeIntegralNumber10001() {
        Asserts.assertEquals(json.toJson(0), "0");
        Asserts.assertEquals(json.toJson(1), "1");
        Asserts.assertEquals(json.toJson(-1), "-1");
        Asserts.assertEquals(json.toJson(999999999), "999999999");
        Asserts.assertEquals(json.toJson(-999999999), "-999999999");
        Asserts.assertEquals(json.toJson(Integer.MAX_VALUE), String.valueOf(Integer.MAX_VALUE));
        Asserts.assertEquals(json.toJson(Integer.MIN_VALUE), String.valueOf(Integer.MIN_VALUE));
    }

    @Test(priority = 10002)
    public void testDeserializeIntegralNumber10002() {
        Asserts.assertEquals(json.fromJson("0", int.class), 0);
        Asserts.assertEquals(json.fromJson("1", int.class), 1);
        Asserts.assertEquals(json.fromJson("-1", int.class), -1);
        Asserts.assertEquals(json.fromJson("999999999", int.class), 999999999);
        Asserts.assertEquals(json.fromJson("-999999999", int.class), -999999999);
        Asserts.assertEquals(json.fromJson(String.valueOf(Integer.MAX_VALUE), int.class), Integer.MAX_VALUE);
        Asserts.assertEquals(json.fromJson(String.valueOf(Integer.MIN_VALUE), int.class), Integer.MIN_VALUE);
    }

    @Test(priority = 10003)
    public void testSerializeDoubleNumber10003() {
        Asserts.assertEquals(json.toJson(0.0), "0.0");
        Asserts.assertEquals(json.toJson(1.0), "1.0");
        Asserts.assertEquals(json.toJson(-1.0), "-1.0");
        Asserts.assertEquals(json.toJson(999999999.0), "999999999.0");
        Asserts.assertEquals(json.toJson(-999999999.0), "-999999999.0");
        Asserts.assertEquals(json.toJson(0.99999999), "0.99999999");
        Asserts.assertEquals(json.toJson(1.99999999), "1.99999999");
        Asserts.assertEquals(json.toJson(-1.99999999), "-1.99999999");
        Asserts.assertEquals(json.toJson(999999999.99999999), "999999999.99999999");
        Asserts.assertEquals(json.toJson(-999999999.99999999), "-999999999.99999999");
        Asserts.assertEquals(json.toJson(Double.MAX_VALUE), String.valueOf(Double.MAX_VALUE));
        Asserts.assertEquals(json.toJson(Double.MIN_VALUE), String.valueOf(Double.MIN_VALUE));
    }

    @Test(priority = 10004)
    public void testDeserializeDoubleNumber10004() {
        Asserts.assertEquals(json.fromJson("0.0", double.class), 0.0);
        Asserts.assertEquals(json.fromJson("1.0", double.class), 1.0);
        Asserts.assertEquals(json.fromJson("-1.0", double.class), -1.0);
        Asserts.assertEquals(json.fromJson("999999999.0", double.class), 999999999.0);
        Asserts.assertEquals(json.fromJson("-999999999.0", double.class), -999999999.0);
        Asserts.assertEquals(json.fromJson("0.99999999", double.class), 0.99999999);
        Asserts.assertEquals(json.fromJson("1.99999999", double.class), 1.99999999);
        Asserts.assertEquals(json.fromJson("-1.99999999", double.class), -1.99999999);
        Asserts.assertEquals(json.fromJson("999999999.99999999", double.class), 999999999.99999999);
        Asserts.assertEquals(json.fromJson("-999999999.99999999", double.class), -999999999.99999999);
        Asserts.assertEquals(json.fromJson(String.valueOf(Double.MAX_VALUE), double.class), Double.MAX_VALUE);
        Asserts.assertEquals(json.fromJson(String.valueOf(Double.MIN_VALUE), double.class), Double.MIN_VALUE);
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
