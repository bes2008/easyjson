package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.util.JSONs;
import org.testng.annotations.Test;

import java.util.Map;

public class EasyjsonHexUnicodeTest extends AbstractBaseTest {

    @Test(priority = 10103)
    public void decodeHexOrUnicode() {

        String jsonString2 = "{\"hello\":\"wor\\u005Cld\"}";
        String jsonString1 = "{\"hello\":\"wor\\x5Cld\"}";

        Map map2 = JSONs.parse(jsonString2, Map.class);
        Map map1 = JSONs.parse(jsonString1, Map.class);
        System.out.println(map2);
        System.out.println(map1);

        String jsonString3 = "{\\\"hello\\\":\\\"wor\\\\u005Cld\\\"}";
        Map map3 = JSONs.parse(jsonString3, Map.class);
        System.out.println(map3);
    }


}
