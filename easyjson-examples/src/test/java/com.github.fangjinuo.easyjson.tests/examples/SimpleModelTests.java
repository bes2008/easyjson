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
import com.github.fangjinuo.easyjson.core.JSONBuilder;
import com.github.fangjinuo.easyjson.core.JSONBuilderProvider;
import com.github.fangjinuo.easyjson.core.JsonTreeNode;
import com.github.fangjinuo.easyjson.core.exclusion.IgnoreAnnotationExclusion;
import com.github.fangjinuo.easyjson.core.util.type.Types;
import com.github.fangjinuo.easyjson.fastjson.FastJsonJSONBuilder;
import com.github.fangjinuo.easyjson.gson.GsonJSONBuilder;
import com.github.fangjinuo.easyjson.jackson.JacksonJSONBuilder;
import com.github.fangjinuo.easyjson.tests.examples.struct.Contact;
import com.github.fangjinuo.easyjson.tests.examples.struct.Gender;
import com.github.fangjinuo.easyjson.tests.examples.struct.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.text.DateFormat;
import java.util.*;

public class SimpleModelTests {
    private static Person person;
    private static final List<Person> persons = new ArrayList<Person>();
    private static final Map<String, Person> nameToPersonMap = new HashMap<String, Person>();
    private static final Map<Integer, Person> idToPersonMap = new HashMap<Integer, Person>();

    static {
        for (int i = 1; i <= 10; i++) {
            Person p = new Person();
            p.setId(i);
            p.setName("name_" + i);
            p.setBirthday(new Date());
            p.setGender(i % 2 == 0 ? Gender.man : Gender.woman);
            p.setAuthCode(12312425353464564L + i);

            Contact c = new Contact();
            p.setContact(c);
            c.setEmail(p.getName() + "@gmail.com");
            c.setMobilePhone("mphone" + i);
            c.setPhone("phone" + i);
            c.setMsn("msn" + i);
            c.setWebchat("webchat" + i);


            if (i == 1) {
                person = p;
            }

            persons.add(person);
            nameToPersonMap.put(p.getName(), p);
            idToPersonMap.put(p.getId(), p);
        }
    }

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

    @Test
    public void testEasyJson() {
        Map<String, JSONBuilder> jsonBuilderMap = new HashMap<String, JSONBuilder>();
        jsonBuilderMap.put("jackson", new JacksonJSONBuilder());
        jsonBuilderMap.put("fastjson", new FastJsonJSONBuilder());
        jsonBuilderMap.put("gson", new GsonJSONBuilder());

        for(Map.Entry<String, JSONBuilder> entry : jsonBuilderMap.entrySet()) {
            String jsonLibraryName = entry.getKey();
            System.out.println("=====================EasyJson test ["+jsonLibraryName+"] start =============================");
            JSONBuilder jsonBuilder = entry.getValue();
            com.github.fangjinuo.easyjson.core.JSON gson = jsonBuilder.serializeNulls(false).serializeNumberAsString(true).serializeEnumUsingValue(true).addDeserializationExclusion(new IgnoreAnnotationExclusion()).build();

            // test simple object
            String str1 = gson.toJson(person, person.getClass());
            System.out.println(str1);
            Person p1 = gson.fromJson(str1, Person.class);
            System.out.println(p1.equals(person));
            System.out.println(gson.toJson(p1));

            // test list
            String str2 = gson.toJson(persons);
            System.out.println(str2);
            List<Person> persons2 = gson.fromJson(str2, Types.getListParameterizedType(Person.class));
            System.out.println(gson.toJson(persons2));
            // test map
            String str3 = gson.toJson(idToPersonMap);
            System.out.println(str3);
            Map<Integer, Person> personMap = gson.fromJson(str3, Types.getMapParameterizedType(Integer.class, Person.class));
            System.out.println(gson.toJson(personMap, Types.getMapParameterizedType(Integer.class, Person.class)));
            System.out.println("=====================EasyJson test ["+jsonLibraryName+"] end =============================");
        }
    }

    @Test
    public void testEasyJson_enum_list() {
        Map<String, JSONBuilder> jsonBuilderMap = new HashMap<String, JSONBuilder>();
        jsonBuilderMap.put("jackson", new JacksonJSONBuilder());
        jsonBuilderMap.put("fastjson", new FastJsonJSONBuilder());
        jsonBuilderMap.put("gson", new GsonJSONBuilder());

        for(Map.Entry<String, JSONBuilder> entry : jsonBuilderMap.entrySet()) {
            String jsonLibraryName = entry.getKey();

            System.out.println("===============EasyJson enum test ["+jsonLibraryName+"] start=================");
            List<Gender> genders = new ArrayList<Gender>();
            genders.add(Gender.man);
            genders.add(Gender.woman);
            genders.add(Gender.woman);
            genders.add(Gender.man);

            com.github.fangjinuo.easyjson.core.JSON json = new JacksonJSONBuilder().serializeEnumUsingValue(true).build();
            String jsonString = json.toJson(genders);
            System.out.println(jsonString);
            List<Gender> a = json.fromJson(jsonString, Types.getListParameterizedType(Gender.class));
            jsonString = json.toJson(a);
            System.out.println(jsonString);
            System.out.println("===============EasyJson enum test ["+jsonLibraryName+"]  end=================");
        }
    }

    @Test
    public void testEasyJson_tree() {
        Map<String, JSONBuilder> jsonBuilderMap = new HashMap<String, JSONBuilder>();
        jsonBuilderMap.put("jackson", new JacksonJSONBuilder());
        jsonBuilderMap.put("fastjson", new FastJsonJSONBuilder());
        jsonBuilderMap.put("gson", new GsonJSONBuilder());

        for(Map.Entry<String, JSONBuilder> entry : jsonBuilderMap.entrySet()) {
            String jsonLibraryName = entry.getKey();

            System.out.println("=====================EasyJson tree ["+jsonLibraryName+"] test start =============================");
            JSONBuilder jsonBuilder = JSONBuilderProvider.create();
            com.github.fangjinuo.easyjson.core.JSON gson = jsonBuilder.serializeNulls(true).serializeNumberAsString(true).serializeEnumUsingValue(true).build();

            // test simple object
            String str1 = gson.toJson(person, person.getClass());
            System.out.println(str1);
            Person p1 = gson.fromJson(str1, Person.class);
            System.out.println(gson.toJson(p1));
            JsonTreeNode t1 = gson.fromJson(str1);
            System.out.println(gson.toJson(t1));

            // test list
            String str2 = gson.toJson(persons);
            System.out.println(str2);
            List<Person> persons2 = gson.fromJson(str2, Types.getListParameterizedType(Person.class));
            System.out.println(gson.toJson(persons2));
            JsonTreeNode t2 = gson.fromJson(str2);
            System.out.println(gson.toJson(t2));

            // test map
            String str3 = gson.toJson(idToPersonMap);
            System.out.println(str3);
            Map<Integer, Person> personMap = gson.fromJson(str3, Types.getMapParameterizedType(Integer.class, Person.class));
            System.out.println(gson.toJson(personMap, Types.getMapParameterizedType(Integer.class, Person.class)));
            JsonTreeNode t3 = gson.fromJson(str3);
            System.out.println(gson.toJson(t3));
            System.out.println("=====================EasyJson tree ["+jsonLibraryName+"] test end =============================");
        }
    }

}
