package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.jackson.JacksonJSONBuilder;

public class FloatTests {
    public float number;

    public static void main(String[] args) {
        final JSON json =  new JacksonJSONBuilder().build();
        System.out.println(json.fromJson("{\"number\":0.01}", FloatTests.class).number);
    }
}
