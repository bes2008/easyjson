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

package com.jn.easyjson.tests.examples.jsonlibrarytests;

import com.jn.easyjson.tests.examples.BaseTests;
import com.jn.easyjson.tests.examples.struct.Gender;
import com.jn.easyjson.tests.examples.struct.Person;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.moshi.adapters.EnumJsonAdapter;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestMoshi extends BaseTests {
    @Test
    public void test() throws Exception {
        System.out.println("=====================Moshi test start =============================");
        Moshi moshi = new Moshi.Builder()
                .add(Date.class, new Rfc3339DateJsonAdapter())
                .add(Enum.class, EnumJsonAdapter.create(Gender.class)).build();
        JsonAdapter<Person> personJsonAdapter = moshi.adapter(Person.class);


        // test simple object
        String str1 = personJsonAdapter.toJson(person);
        System.out.println(str1);
        Person p1 = personJsonAdapter.fromJson(str1);
        System.out.println(p1.equals(person));
        System.out.println(personJsonAdapter.toJson(p1));


        Type listType = Types.newParameterizedType(List.class, Person.class);
        JsonAdapter<List<Person>> listJsonAdapter = moshi.adapter(listType);
        // test list
        String str2 = listJsonAdapter.toJson(persons);
        System.out.println(str2);
        List<Person> persons2 = listJsonAdapter.fromJson(str2);
        System.out.println(listJsonAdapter.toJson(persons2));

        Type mapType = Types.newParameterizedType(Map.class, Integer.class, Person.class);
        JsonAdapter<Map<Integer, Person>> mapJsonAdapter = moshi.adapter(mapType);
        // test map
        String str3 = mapJsonAdapter.toJson(idToPersonMap);
        System.out.println(str3);
        Map<Integer, Person> personMap = mapJsonAdapter.fromJson(str3);
        System.out.println(mapJsonAdapter.toJson(personMap));


        System.out.println("=====================Moshi test end =============================");
    }
}
