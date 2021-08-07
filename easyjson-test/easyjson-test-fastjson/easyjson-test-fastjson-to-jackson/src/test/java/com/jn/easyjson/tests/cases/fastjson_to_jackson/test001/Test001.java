package com.jn.easyjson.tests.cases.fastjson_to_jackson.test001;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jn.easyjson.tests.cases.AbstractBaseTest;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.io.IOs;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import java.io.InputStream;

@Test
public class Test001 extends AbstractBaseTest {

    private String jsonstring;

    @BeforeGroups({"g1"})
    public void init() throws Throwable{
        Resource resource = Resources.loadClassPathResource("fastjson_to_jackson_001.json", Test001.class);
        InputStream inputStream = resource.getInputStream();
        byte[] bytes = IOs.toByteArray(inputStream);
        jsonstring = new String(bytes, "UTF-8");
        IOs.close(inputStream);
    }


    @Test(priority = 10001, groups = {"g1"})
    public void test01() throws Throwable{
        Model model1 = JSON.parseObject(jsonstring, Model.class);
        Model model2 = new ObjectMapper().readValue(jsonstring, Model.class);
        System.out.println(1);
    }


    @Test(priority = 10002, groups = {"g1"})
    public void test02() throws Throwable{
        JSONObject model1 = JSON.parseObject(jsonstring);
    //    Model model2 = new ObjectMapper().readValue(jsonstring, Model.class);
        System.out.println(1);
    }
}
