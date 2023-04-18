package com.jn.easyjson.tests.cases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jn.easyjson.core.util.JSONs;
import org.junit.Test;

import java.util.Date;

public class JacksonFormatTest {
    @Test
    public void test() throws JsonProcessingException {
        Xyz xyz = new Xyz();
        xyz.setCreateTime(new Date());
        xyz.setId("001");

        String json = JSONs.toJson(xyz);
        String json1 = new ObjectMapper().writeValueAsString(xyz);
        System.out.println(json);
        System.out.println(json1);
        Xyz xyz1 = JSONs.parse(json, Xyz.class);
        System.out.println(xyz1);
    }
}
