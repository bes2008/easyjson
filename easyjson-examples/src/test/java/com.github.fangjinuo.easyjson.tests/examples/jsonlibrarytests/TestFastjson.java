/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.fangjinuo.easyjson.tests.examples.jsonlibrarytests;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.github.fangjinuo.easyjson.tests.examples.BaseTests;
import com.github.fangjinuo.easyjson.tests.examples.struct.Person;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestFastjson extends BaseTests {
    @Test
    public void testFastJson() throws Exception {
        System.out.println("=====================FastJson test start =============================");

        // test simple object
        System.out.println("test simple object");
        String str1 = JSON.toJSONString(person, SerializerFeature.WriteMapNullValue);
        System.out.println(str1);
        Person p1 = JSON.parseObject(str1, Person.class, Feature.AutoCloseSource);
        System.out.println(p1.equals(person));
        System.out.println(JSON.toJSONString(p1, SerializerFeature.WriteMapNullValue));

        // test fastjson JSONObject
        System.out.println("test fastjson JSONObject");
        JSONObject jsonObject = JSON.parseObject(str1);
        System.out.println(jsonObject);
        System.out.println(JSON.toJSONString(jsonObject));

        // test list
        System.out.println("test list");
        String str2 = JSON.toJSONString(persons, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(str2);
        List<Person> persons2 = JSON.parseObject(str2, new ParameterizedTypeImpl(new Class[]{Person.class}, null, List.class));
        System.out.println(JSON.toJSONString(persons2, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect));

        // test map
        System.out.println("test map");
        String str3 = JSON.toJSONString(idToPersonMap);
        System.out.println(str3);
        Map<Integer, Person> personMap = JSON.parseObject(str3, new ParameterizedTypeImpl(new Class[]{Integer.class, Person.class}, null, Map.class));
        System.out.println(JSON.toJSONString(personMap, SerializerFeature.WriteMapNullValue));

        JSONArray jsonArray = new JSONArray();
        for (Person person : persons) {
            jsonArray.add(person);
        }

        // test list
        String str20 = JSON.toJSONString(persons, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(str20);
        List<Person> persons20 = JSON.parseObject(str20, new ParameterizedTypeImpl(new Class[]{Person.class}, null, List.class));
        System.out.println(JSON.toJSONString(persons20, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect));

        String str22 = JSON.toJSONString(jsonArray, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(str22);
        List<Person> persons22 = JSON.parseObject(str22, new ParameterizedTypeImpl(new Class[]{Person.class}, null, List.class));
        System.out.println(JSON.toJSONString(persons22, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect));

        List<Person> persons23 = JSON.parseArray(str22, Person.class);
        System.out.println(JSON.toJSONString(persons23, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect));

        JSONArray jsonArray4 = JSON.parseArray(str22);
        String str4 = JSON.toJSONString(jsonArray4, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(str4);


        System.out.println("=====================FastJson test end =============================");
    }
}
