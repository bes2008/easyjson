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

package com.github.fangjinuo.easyjson.tests.gson;

import com.github.fangjinuo.easyjson.core.JSONBuilder;
import com.github.fangjinuo.easyjson.core.JSONBuilderProvider;
import com.github.fangjinuo.easyjson.core.JsonTreeNode;
import com.github.fangjinuo.easyjson.core.util.type.Types;
import com.github.fangjinuo.easyjson.tests.gson.struct.Contact;
import com.github.fangjinuo.easyjson.tests.gson.struct.Gender;
import com.github.fangjinuo.easyjson.tests.gson.struct.Person;
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
            c.setQq("qq" + i);
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
    public void testGson() {
        System.out.println("=====================Gson test start =============================");
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat(DateFormat.LONG).create();

        // test simple object
        String str1 = gson.toJson(person, person.getClass());
        System.out.println(str1);
        Person p1 = gson.fromJson(str1, Person.class);
        System.out.println(p1.equals(person));
        System.out.println(gson.toJson(person));

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
    public void testEasyJson_gson() {

        System.out.println("=====================EasyJson [Gson] test start =============================");
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        com.github.fangjinuo.easyjson.core.JSON gson = jsonBuilder.serializeNulls().serializeNumberAsString().serializeEnumUsingValue().enableIgnoreAnnotation().build();

        // test simple object
        String str1 = gson.toJson(person, person.getClass());
        System.out.println(str1);
        Person p1 = gson.fromJson(str1, Person.class);
        System.out.println(p1.equals(person));
        System.out.println(gson.toJson(person));

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
        System.out.println("=====================EasyJson [Gson] test end =============================");
    }

    @Test
    public void testEasyJson_gson_tree() {

        System.out.println("=====================EasyJson tree [Gson] test start =============================");
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        com.github.fangjinuo.easyjson.core.JSON gson = jsonBuilder.serializeNulls().serializeNumberAsString().serializeEnumUsingValue().enableIgnoreAnnotation().build();

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
        System.out.println("=====================EasyJson tree [Gson] test end =============================");
    }

}
