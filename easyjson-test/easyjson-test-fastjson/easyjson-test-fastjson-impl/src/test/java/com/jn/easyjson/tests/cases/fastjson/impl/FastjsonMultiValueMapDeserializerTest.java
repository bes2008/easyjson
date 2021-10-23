package com.jn.easyjson.tests.cases.fastjson.impl;

import com.alibaba.fastjson.TypeReference;
import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.multivalue.CommonMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import org.junit.Test;

public class FastjsonMultiValueMapDeserializerTest {

    @Test
    public void test(){
        JSON json = JSONBuilderProvider.simplest();
        MultiValueMap<String, Integer> multiValueMap = new CommonMultiValueMap<String, Integer>();
        multiValueMap.put("key1", Collects.<Integer>newArrayList(1,2,3,4,5));
        multiValueMap.put("key2", Collects.<Integer>newArrayList(21,22,23,24,25));

        String jsonstring = json.toJson(multiValueMap);

        MultiValueMap<String, Integer> result = json.fromJson(jsonstring, new TypeReference<MultiValueMap<String, Integer>>(){}.getType());
        System.out.println(result.size());

    }



}
