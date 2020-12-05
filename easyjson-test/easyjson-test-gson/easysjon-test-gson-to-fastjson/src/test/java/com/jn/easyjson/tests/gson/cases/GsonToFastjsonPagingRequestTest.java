package com.jn.easyjson.tests.gson.cases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jn.easyjson.tests.cases.EasyJsonPagingRequestTest;
import com.jn.easyjson.tests.entity.struct.Person;
import com.jn.easyjson.tests.utils.Asserts;
import org.testng.annotations.Test;

import java.util.List;

@Test
public class GsonToFastjsonPagingRequestTest extends EasyJsonPagingRequestTest {
    @Test
    public void testPersonsDeserialize10203() {
        String jsonString = getPersonsString();
        TypeToken token = TypeToken.getParameterized(List.class, Person.class);
        List<Person> actual = json.fromJson(jsonString, token.getType());
        List<Person> expected = getPersonsObject();
        Asserts.assertDeepEquals(actual, expected);
    }

    @Test
    public void testPersonsDeserialize10204() {
        String jsonString = getPersonsString();
        Gson gson = new Gson();
        TypeToken token = TypeToken.getParameterized(List.class, Person.class);
        List<Person> actual = gson.fromJson(jsonString, token.getType());
        List<Person> expected = getPersonsObject();
        Asserts.assertDeepEquals(actual, expected);
    }
}
