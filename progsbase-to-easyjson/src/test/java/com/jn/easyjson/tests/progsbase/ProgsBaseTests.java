/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.easyjson.tests.progsbase;

import com.jn.easyjson.tests.examples.BaseTests;
import com.jn.easyjson.tests.examples.struct.Person;
import com.progsbase.libraries.JSON.GenericTypeGetter;
import com.progsbase.libraries.JSON.JSONReflectiveReader;
import com.progsbase.libraries.JSON.JSONReflectiveWriter;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ProgsBaseTests extends BaseTests {

    @Test
    public void testPrimiveList() {
        try {
            List<List<Double>> e = (List<List<Double>>) JSONReflectiveReader.readJSON(
                    "[[1.1, 2, 3], [1, 2, 3], [1, 2, 3]]",
                    List.class,
                    new GenericTypeGetter<List<List<Double>>>() {
                    }.getType()
            );
            System.out.println(e);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testProgsbase() throws Exception {
        System.out.println("=====================Progsbase test start =============================");

        // test simple object
        String str1 = JSONReflectiveWriter.writeJSON(person);
        System.out.println(str1);
        Person p1 = JSONReflectiveReader.readJSON(str1, Person.class);
        System.out.println(p1.equals(person));
        System.out.println(JSONReflectiveWriter.writeJSON(p1));

        // test list
        String str2 = JSONReflectiveWriter.writeJSON(persons);
        System.out.println(str2);
        List<Person> persons2 = JSONReflectiveReader.readJSON(str2, List.class, new GenericTypeGetter<List<Person>>() {
        }.getType());
        System.out.println(JSONReflectiveWriter.writeJSON(persons2));
        // test map
        String str3 = JSONReflectiveWriter.writeJSON(idToPersonMap);
        System.out.println(str3);
        Map<Integer, Person> personMap = JSONReflectiveReader.readJSON(str3, Map.class, new GenericTypeGetter<Map<String, Person>>() {
        }.getType());
        System.out.println(JSONReflectiveWriter.writeJSON(personMap));

        System.out.println("=====================Progsbase test end =============================");
    }
}
