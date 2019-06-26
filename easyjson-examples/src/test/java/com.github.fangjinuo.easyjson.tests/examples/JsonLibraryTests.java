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

package com.github.fangjinuo.easyjson.tests.examples;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fangjinuo.easyjson.tests.examples.struct.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.text.DateFormat;
import java.util.List;
import java.util.Map;

public class JsonLibraryTests extends BaseTests {

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
    public void testGson() {
        System.out.println("=====================Gson test start =============================");
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat(DateFormat.LONG).create();

        // test simple object
        String str1 = gson.toJson(person, person.getClass());
        System.out.println(str1);
        Person p1 = gson.fromJson(str1, Person.class);
        System.out.println(p1.equals(person));
        System.out.println(gson.toJson(p1));

        // test list
        String str2 = gson.toJson(persons);
        System.out.println(str2);
        List<Person> persons2 = gson.fromJson(str2, TypeToken.getParameterized(List.class, Person.class).getType());
        System.out.println(gson.toJson(persons2));
        // test map
        String str3 = gson.toJson(idToPersonMap);
        System.out.println(str3);
        Map<Integer, Person> personMap = gson.fromJson(str3, TypeToken.getParameterized(Map.class, Integer.class, Person.class).getType());
        System.out.println(gson.toJson(personMap, TypeToken.getParameterized(Map.class, Integer.class, Person.class).getType()));
        System.out.println("=====================Gson test end =============================");
    }

    @Test
    public void testJackson() throws Exception {
        System.out.println("=====================Jackson test start =============================");
        ObjectMapper objectMapper = new ObjectMapper();

        // test simple object
        String str1 = objectMapper.writeValueAsString(person);
        System.out.println(str1);
        Person p1 = objectMapper.readValue(str1, Person.class);
        System.out.println(p1.equals(person));
        System.out.println(objectMapper.writeValueAsString(person));

        // test list
        String str2 = objectMapper.writeValueAsString(persons);
        System.out.println(str2);
        List<Person> persons2 = objectMapper.readValue(str2, objectMapper.getTypeFactory().constructParametricType(List.class, Person.class));
        System.out.println(objectMapper.writeValueAsString(persons2));
        // test map
        String str3 = objectMapper.writeValueAsString(idToPersonMap);
        System.out.println(str3);
        Map<Integer, Person> personMap = objectMapper.readValue(str3, objectMapper.getTypeFactory().constructMapLikeType(Map.class, Integer.class, Person.class));
        System.out.println(objectMapper.writeValueAsString(personMap));
        System.out.println("=====================Jackson test end =============================");
    }
}
