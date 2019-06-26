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

import com.github.fangjinuo.easyjson.tests.examples.struct.Contact;
import com.github.fangjinuo.easyjson.tests.examples.struct.Gender;
import com.github.fangjinuo.easyjson.tests.examples.struct.Person;

import java.util.*;

public class BaseTests {
    protected static Person person;
    protected static final List<Person> persons = new ArrayList<Person>();
    protected static final Map<String, Person> nameToPersonMap = new HashMap<String, Person>();
    protected static final Map<Integer, Person> idToPersonMap = new HashMap<Integer, Person>();

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
}
