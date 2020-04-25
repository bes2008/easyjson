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

package com.jn.easyjson.tests.examples;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.exclusion.IgnoreAnnotationExclusion;
import com.jn.easyjson.fastjson.FastJsonJSONBuilder;
import com.jn.easyjson.gson.GsonJSONBuilder;
import com.jn.easyjson.jackson.JacksonJSONBuilder;
import com.jn.easyjson.tests.examples.struct.Gender;
import com.jn.easyjson.tests.examples.struct.Person;
import com.jn.langx.util.reflect.type.ParameterizedTypeGetter;
import com.jn.langx.util.reflect.type.Types;
import net.arnx.jsonic.TypeReference;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasyJsonTests extends BaseTests {

    @Test
    public void testEasyJson() {
        Map<String, JSONBuilder> jsonBuilderMap = new HashMap<String, JSONBuilder>();
        jsonBuilderMap.put("jackson", new JacksonJSONBuilder());
        jsonBuilderMap.put("fastjson", new FastJsonJSONBuilder());
        jsonBuilderMap.put("gson", new GsonJSONBuilder());

        for (Map.Entry<String, JSONBuilder> entry : jsonBuilderMap.entrySet()) {
            String jsonLibraryName = entry.getKey();
            System.out.println("=====================EasyJson test [" + jsonLibraryName + "] start =============================");
            JSONBuilder jsonBuilder = entry.getValue();
            JSON gson = jsonBuilder.serializeNulls(false).serializeNumberAsString(true).serializeEnumUsingValue(true).addDeserializationExclusion(new IgnoreAnnotationExclusion()).build();

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
            System.out.println("=====================EasyJson test [" + jsonLibraryName + "] end =============================");
        }
    }

    @Test
    public void testEasyJson_enum_list() {
        Map<String, JSONBuilder> jsonBuilderMap = new HashMap<String, JSONBuilder>();
        jsonBuilderMap.put("jackson", new JacksonJSONBuilder());
        jsonBuilderMap.put("fastjson", new FastJsonJSONBuilder());
        jsonBuilderMap.put("gson", new GsonJSONBuilder());

        for (Map.Entry<String, JSONBuilder> entry : jsonBuilderMap.entrySet()) {
            String jsonLibraryName = entry.getKey();

            System.out.println("===============EasyJson enum test [" + jsonLibraryName + "] start=================");
            List<Gender> genders = new ArrayList<Gender>();
            genders.add(Gender.man);
            genders.add(Gender.woman);
            genders.add(Gender.woman);
            genders.add(Gender.man);

            JSON json = new JacksonJSONBuilder().serializeEnumUsingValue(true).build();
            String jsonString = json.toJson(genders);
            System.out.println(jsonString);
            List<Gender> a = json.fromJson(jsonString, Types.getListParameterizedType(Gender.class));
            jsonString = json.toJson(a);
            System.out.println(jsonString);
            System.out.println("===============EasyJson enum test [" + jsonLibraryName + "]  end=================");
        }
    }

    @Test
    public void testEasyJson_tree() {
        Map<String, JSONBuilder> jsonBuilderMap = new HashMap<String, JSONBuilder>();
        jsonBuilderMap.put("jackson", new JacksonJSONBuilder());
        jsonBuilderMap.put("fastjson", new FastJsonJSONBuilder());
        jsonBuilderMap.put("gson", new GsonJSONBuilder());

        for (Map.Entry<String, JSONBuilder> entry : jsonBuilderMap.entrySet()) {
            String jsonLibraryName = entry.getKey();

            System.out.println("=====================EasyJson tree [" + jsonLibraryName + "] test start =============================");
            JSONBuilder jsonBuilder = JSONBuilderProvider.create();
            JSON gson = jsonBuilder.serializeNulls(true).serializeNumberAsString(true).serializeEnumUsingValue(true).build();

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
            System.out.println("=====================EasyJson tree [" + jsonLibraryName + "] test end =============================");
        }
    }

    @Test
    public void genericTest() {
        String str = "[[{\"a\":\"b\"}]]";
        JSON json = new JacksonJSONBuilder().build();
        // Types API
        Type type = Types.getListParameterizedType(Types.getListParameterizedType(Types.getMapParameterizedType(String.class, String.class)));
        Object obj = json.fromJson(str, type);
        System.out.println(obj.toString());

        // langx-java type
        type = new ParameterizedTypeGetter<List<List<Map<String,String>>>>(){}.getRawType();
        obj = json.fromJson(str, type);
        System.out.println(obj.toString());

        // jackson TypeReference API
        type = new TypeReference<List<List<Map<String, String>>>>() {
        }.getType();
        obj = json.fromJson(str, type);
        System.out.println(obj.toString());


        json = new FastJsonJSONBuilder().build();
        obj = json.fromJson(str, type);
        System.out.println(obj.toString());

        json = new GsonJSONBuilder().build();
        obj = json.fromJson(str, type);
        System.out.println(obj.toString());
    }

}
