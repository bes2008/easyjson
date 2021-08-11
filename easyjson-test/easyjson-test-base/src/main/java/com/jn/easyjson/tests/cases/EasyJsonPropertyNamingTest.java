package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.tests.entity.struct.Contact;
import com.jn.easyjson.tests.entity.struct.Person;
import com.jn.easyjson.tests.entity.user.Gender;
import com.jn.easyjson.tests.utils.Asserts;
import com.jn.langx.util.reflect.type.Types;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class EasyJsonPropertyNamingTest extends AbstractBaseTest {
    protected JSON json;
    private Person person;
    private List<Person> persons = new ArrayList<Person>();

    public EasyJsonPropertyNamingTest() {
        JSONBuilder jsonBuilder = JSONBuilderProvider.create()
                .serializeNulls(true)
                .beanPropertyNamingPolicy("UpperCamelCase");

        // IgnoreAnnotationExclusion不在这里测试, 单独写用例
        // jsonBuilder.addDeserializationExclusion(new IgnoreAnnotationExclusion());
        this.json = jsonBuilder.build();

        for (int i = 1; i <= 10; i++) {
            Person p = new Person();
            p.setId(i);
            p.setName("name_" + i);
            p.setBirthday(new Date(1592179316459L));
            p.setGender(i % 2 == 0 ? Gender.MALE : Gender.FEMALE);
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
        }
    }

    protected List<Person> getPersonsObject() {
        return persons;
    }

    protected Person getPersonObject() {
        return person;
    }

    protected String getPersonString() {
        return readClassResourceText(EasyJsonPagingRequestTest.class, "json/PersonObjectString_UpperCamelCase.json");
    }

    protected String getPersonMixedString() {
        return readClassResourceText(EasyJsonPagingRequestTest.class, "json/PersonObjectString_UpperCamelCase_mixed.json");
    }

    protected String getPersonsString() {
        return readClassResourceText(EasyJsonPagingRequestTest.class, "json/PersonArrayString.json");
    }

    @Test(priority = 10101)
    public void testPersonSerialize10101() {
        Person object = getPersonObject();
        String jsonString = json.toJson(object);
        System.out.println(jsonString);
        Asserts.assertJsonEquals(jsonString, getPersonString());
    }

    @Test(priority = 10102)
    public void testPersonDeserialize10102() {
        String jsonString = getPersonString();
        Person actual = json.fromJson(jsonString, Person.class);
        Person expected = getPersonObject();
        Asserts.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10103)
    public void testPersonDeserialize10103() {
        String jsonString = getPersonMixedString();
        Person actual = json.fromJson(jsonString, Person.class);
        Person expected = getPersonObject();
        Asserts.assertDeepEquals(actual, expected);
    }


    @Test(priority = 10202)
    public void testPersonsDeserialize10202() {
        String jsonString = getPersonsString();
        List<Person> actual = json.fromJson(jsonString, Types.getListParameterizedType(Person.class));
        List<Person> expected = getPersonsObject();
        Asserts.assertDeepEquals(actual, expected);
    }
}
