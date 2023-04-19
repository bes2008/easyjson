package com.jn.easyjson.tests.cases.fastjson_to_gson;

import com.alibaba.fastjson.JSON;
import com.jn.easyjson.tests.cases.fastjson.entity.NameValueRatio;
import org.junit.Test;

public class FatjsonToGsonIsFieldTest{
    @Test
    public void test(){
        NameValueRatio n = new NameValueRatio();
        n.setName("hello");
        n.setValue("world");

        String json = JSON.toJSONString(n);
        System.out.println(json);

        NameValueRatio m1 = JSON.parseObject(json, NameValueRatio.class);
        String json1 = JSON.toJSONString(m1);
        System.out.println(json1);
    }
}
