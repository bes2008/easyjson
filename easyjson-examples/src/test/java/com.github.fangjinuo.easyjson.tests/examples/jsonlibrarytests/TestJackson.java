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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fangjinuo.easyjson.tests.examples.BaseTests;
import com.github.fangjinuo.easyjson.tests.examples.struct.Person;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestJackson extends BaseTests {
    @Test
    public void testJackson() throws Exception {
        System.out.println("=====================Jackson test start =============================");
        ObjectMapper objectMapper = new ObjectMapper();

        // test simple object
        String str1 = objectMapper.writeValueAsString(person);
        System.out.println(str1);
        Person p1 = objectMapper.readValue(str1, Person.class);
        System.out.println(p1.equals(person));
        System.out.println(objectMapper.writeValueAsString(p1));

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
