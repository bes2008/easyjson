package com.jn.easyjson.tests.cases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jn.easyjson.core.util.JSONs;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class JacksonFormatTest {
    @Test
    public void test() throws IOException {
        Xyz xyz = new Xyz();
        xyz.setCreateTime(new Date());
        xyz.setId("001");
        System.out.println(xyz.getCreateTime().getTime());

        String json1 = JSONs.toJson(xyz);
        String json2 = new ObjectMapper().writeValueAsString(xyz);
        System.out.println(json1);
        System.out.println(json2);
        Xyz xyz1 = JSONs.parse(json1, Xyz.class);
        Xyz xyz2 = new ObjectMapper().readValue(json2, Xyz.class);

        System.out.println(xyz2.getCreateTime().getTime());
        System.out.println(xyz1.getCreateTime().getTime());

        System.out.println(xyz2.getCreateTime().getTime()-xyz1.getCreateTime().getTime());
    }
}
