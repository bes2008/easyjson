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

package com.jn.easyjson.boonjson.test;

import com.jn.easyjson.tests.examples.BaseTests;
import com.jn.easyjson.tests.examples.struct.Person;
import io.advantageous.boon.json.JsonFactory;
import io.advantageous.boon.json.ObjectMapper;
import org.junit.Test;

public class BoonTests extends BaseTests {
    @Test
    public void testBoonJson() throws Exception {
        System.out.println("=====================FastJson test start =============================");
        ObjectMapper objectMapper = JsonFactory.create();
        // test simple object
        System.out.println("test simple object");
        String str1 = objectMapper.toJson(person);
        System.out.println(str1);
        Person p1 = objectMapper.fromJson(str1, Person.class);
        System.out.println(p1.equals(person));
        System.out.println(objectMapper.toJson(p1));

        System.out.println("=====================FastJson test end =============================");
    }


}
