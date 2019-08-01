/*
 *  Copyright 2019 the original author or authors.
 *
 *  Licensed under the LGPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.github.fangjinuo.easyjson.tests.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.jn.easyjson.tests.examples.BaseTests;
import com.jn.easyjson.tests.examples.struct.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FastjsonToEasyjsonTests extends BaseTests {

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
        System.out.println("=====================FastJson test end =============================");
    }

    @Test
    public void testJSONArray() {
        System.out.println("=====================FastJson JSONArray start=============================");

        JSONArray jsonArray = new JSONArray();
        for (Person person : persons) {
            jsonArray.add(person);
        }

        // test list
        String str2 = JSON.toJSONString(persons, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(str2);
        List<Person> persons2 = JSON.parseObject(str2, new ParameterizedTypeImpl(new Class[]{Person.class}, null, List.class));
        System.out.println(JSON.toJSONString(persons2, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect));

        String str22 = JSON.toJSONString(jsonArray, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(str22);
        List<Person> persons22 = JSON.parseObject(str22, new ParameterizedTypeImpl(new Class[]{Person.class}, null, List.class));
        System.out.println(JSON.toJSONString(persons22, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect));

        List<Person> persons23 = JSON.parseArray(str22, Person.class);
        System.out.println(JSON.toJSONString(persons23, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect));

        JSONArray jsonArray3 = JSON.parseArray(str22);
        String str3 = JSON.toJSONString(jsonArray3, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(str3);

        System.out.println("=====================FastJson JSONArray end=============================");
    }

    @Test
    public void testJSONArrayList() {
        System.out.println("=====================FastJson JSONArray List start=============================");

        List<JSONArray> jsonArrayList = new ArrayList<JSONArray>();
        JSONArray jsonArray = null;
        for (int i = 0; i < persons.size(); i++) {
            if (i % 3 == 0) {
                jsonArray = new JSONArray();
                jsonArrayList.add(jsonArray);
            }
            jsonArray.add(persons.get(i));
        }

        // test list
        String str2 = JSON.toJSONString(jsonArrayList, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(str2);
        List<JSONArray> persons2 = JSON.parseObject(str2, new ParameterizedTypeImpl(new Class[]{JSONArray.class}, null, List.class));
        System.out.println(JSON.toJSONString(persons2, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect));

        List<JSONArray> persons23 = JSON.parseArray(str2, JSONArray.class);
        System.out.println(JSON.toJSONString(persons23, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect));

        JSONArray jsonArray3 = JSON.parseArray(str2);
        String str3 = JSON.toJSONString(jsonArray3, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(str3);

        System.out.println("=====================FastJson JSONArray List end=============================");
    }

}
