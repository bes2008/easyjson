package com.jn.easyjson.tests.cases;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.gitee.qdbp.tools.files.PathTools;
import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.tests.entity.struct.Contact;
import com.jn.easyjson.tests.entity.struct.PagingRequest;
import com.jn.easyjson.tests.entity.struct.PagingRequestContext;
import com.jn.easyjson.tests.entity.struct.PagingResult;
import com.jn.easyjson.tests.entity.struct.Person;
import com.jn.easyjson.tests.entity.user.Gender;
import com.jn.easyjson.tests.utils.CompareTools;
import com.jn.langx.util.reflect.type.Types;

/**
 * PagingRequest复杂对象测试
 *
 * @author zhaohuihua
 * @version 20200615
 */
public abstract class EasyJsonPagingRequestTest {

    private JSON json;
    private Person person;
    private List<Person> persons = new ArrayList<Person>();
    private Map<String, Person> nameToPersonMap = new HashMap<String, Person>();
    private Map<Integer, Person> idToPersonMap = new HashMap<Integer, Person>();
    private PagingRequest<Map<String, Object>, Person> pagingRequest;

    @BeforeMethod
    public void testStart(Method method) {
        System.out.println("/************************************************************\\");
        System.out.println("| " + method.getDeclaringClass().getSimpleName() + '.' + method.getName());
        System.out.println("\\************************************************************/");
    }

    public EasyJsonPagingRequestTest() {
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        jsonBuilder.serializeNulls(true).serializeNumberAsString(false);
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

        PagingRequest<Map<String, Object>, Person> pagingRequest = new PagingRequest<>();
        pagingRequest.setCacheCount(true);
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("a", 1);
        condition.put("b", 2);
        pagingRequest.setCondition(condition);
        pagingRequest.setCountColumn("name_");
        pagingRequest.setPageNo(20);
        pagingRequest.setPageSize(50);
        PagingRequestContext<Map<String, Object>, Person> pagingRequestContext = new PagingRequestContext<>();
        pagingRequest.setCtx(pagingRequestContext);
        PagingResult<Person> result = new PagingResult<>();
        result.setItems(persons);
        result.setTotal(203423);
        result.setPageNo(pagingRequest.getPageNo());
        result.setPageSize(pagingRequest.getPageSize());
        pagingRequest.setResult(result);
        this.pagingRequest = pagingRequest;
    }

    protected Person getPersonObject() {
        return person;
    }

    protected String getPersonString() {
        return "{\"authCode\":12312425353464565,\"birthday\":1592179316459,\"contact\":{\"email\":\"name_1@gmail.com\",\"mobilePhone\":\"mphone1\",\"msn\":\"msn1\",\"phone\":\"phone1\",\"qq\":null,\"webchat\":\"webchat1\"},\"gender\":\"FEMALE\",\"id\":1,\"married\":false,\"name\":\"name_1\"}";
    }

    protected List<Person> getPersonsObject() {
        return persons;
    }

    protected String getPersonsString() {
        return "[{\"authCode\":12312425353464565,\"birthday\":1592179316459,\"contact\":{\"email\":\"name_1@gmail.com\",\"mobilePhone\":\"mphone1\",\"msn\":\"msn1\",\"phone\":\"phone1\",\"qq\":null,\"webchat\":\"webchat1\"},\"gender\":\"FEMALE\",\"id\":1,\"married\":false,\"name\":\"name_1\"},{\"authCode\":12312425353464565,\"birthday\":1592179316459,\"contact\":{\"email\":\"name_1@gmail.com\",\"mobilePhone\":\"mphone1\",\"msn\":\"msn1\",\"phone\":\"phone1\",\"qq\":null,\"webchat\":\"webchat1\"},\"gender\":\"FEMALE\",\"id\":1,\"married\":false,\"name\":\"name_1\"},{\"authCode\":12312425353464565,\"birthday\":1592179316459,\"contact\":{\"email\":\"name_1@gmail.com\",\"mobilePhone\":\"mphone1\",\"msn\":\"msn1\",\"phone\":\"phone1\",\"qq\":null,\"webchat\":\"webchat1\"},\"gender\":\"FEMALE\",\"id\":1,\"married\":false,\"name\":\"name_1\"},{\"authCode\":12312425353464565,\"birthday\":1592179316459,\"contact\":{\"email\":\"name_1@gmail.com\",\"mobilePhone\":\"mphone1\",\"msn\":\"msn1\",\"phone\":\"phone1\",\"qq\":null,\"webchat\":\"webchat1\"},\"gender\":\"FEMALE\",\"id\":1,\"married\":false,\"name\":\"name_1\"},{\"authCode\":12312425353464565,\"birthday\":1592179316459,\"contact\":{\"email\":\"name_1@gmail.com\",\"mobilePhone\":\"mphone1\",\"msn\":\"msn1\",\"phone\":\"phone1\",\"qq\":null,\"webchat\":\"webchat1\"},\"gender\":\"FEMALE\",\"id\":1,\"married\":false,\"name\":\"name_1\"},{\"authCode\":12312425353464565,\"birthday\":1592179316459,\"contact\":{\"email\":\"name_1@gmail.com\",\"mobilePhone\":\"mphone1\",\"msn\":\"msn1\",\"phone\":\"phone1\",\"qq\":null,\"webchat\":\"webchat1\"},\"gender\":\"FEMALE\",\"id\":1,\"married\":false,\"name\":\"name_1\"},{\"authCode\":12312425353464565,\"birthday\":1592179316459,\"contact\":{\"email\":\"name_1@gmail.com\",\"mobilePhone\":\"mphone1\",\"msn\":\"msn1\",\"phone\":\"phone1\",\"qq\":null,\"webchat\":\"webchat1\"},\"gender\":\"FEMALE\",\"id\":1,\"married\":false,\"name\":\"name_1\"},{\"authCode\":12312425353464565,\"birthday\":1592179316459,\"contact\":{\"email\":\"name_1@gmail.com\",\"mobilePhone\":\"mphone1\",\"msn\":\"msn1\",\"phone\":\"phone1\",\"qq\":null,\"webchat\":\"webchat1\"},\"gender\":\"FEMALE\",\"id\":1,\"married\":false,\"name\":\"name_1\"},{\"authCode\":12312425353464565,\"birthday\":1592179316459,\"contact\":{\"email\":\"name_1@gmail.com\",\"mobilePhone\":\"mphone1\",\"msn\":\"msn1\",\"phone\":\"phone1\",\"qq\":null,\"webchat\":\"webchat1\"},\"gender\":\"FEMALE\",\"id\":1,\"married\":false,\"name\":\"name_1\"},{\"authCode\":12312425353464565,\"birthday\":1592179316459,\"contact\":{\"email\":\"name_1@gmail.com\",\"mobilePhone\":\"mphone1\",\"msn\":\"msn1\",\"phone\":\"phone1\",\"qq\":null,\"webchat\":\"webchat1\"},\"gender\":\"FEMALE\",\"id\":1,\"married\":false,\"name\":\"name_1\"}]";
    }

    protected Person getPersonById(int id) {
        return idToPersonMap.get(id);
    }

    protected Person getPersonByName(String name) {
        return nameToPersonMap.get(name);
    }

    protected PagingRequest<Map<String, Object>, Person> getPagingRequestObject() {
        return pagingRequest;
    }

    protected String readClassResourceText(String path) throws IOException {
        URL url = PathTools.findClassResource(EasyJsonPagingRequestTest.class, path);
        return PathTools.downloadString(url);
    }

    protected String getPagingRequestStringIncludeNulls() throws IOException {
        return readClassResourceText("json/PagingRequestIncludeNulls.json");
    }

    protected String getPagingRequestStringExcludeNulls() throws IOException {
        return readClassResourceText("json/PagingRequestExcludeNulls.json");
    }

    protected String getPagingRequestStringNumberAsString() throws IOException {
        return readClassResourceText("json/PagingRequestNumberAsString.json");
    }

    @Test(priority = 10001)
    public void testPagingRequestSerializeIncludeNulls10001() throws Exception {
        PagingRequest<Map<String, Object>, Person> object = getPagingRequestObject();
        String jsonString = json.toJson(object);
        System.out.println(jsonString);
        CompareTools.assertJsonEquals(jsonString, getPagingRequestStringIncludeNulls());
    }

    @Test(priority = 10002)
    public void testPagingRequestSerializeExcludeNulls10002() throws Exception {
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        jsonBuilder.serializeNulls(false).serializeNumberAsString(false);
        JSON json = jsonBuilder.build();
        PagingRequest<Map<String, Object>, Person> object = getPagingRequestObject();
        String jsonString = json.toJson(object);
        System.out.println(jsonString);
        CompareTools.assertJsonEquals(jsonString, getPagingRequestStringExcludeNulls());
    }

    @Test(priority = 10003)
    public void testPagingRequestSerializeNumberAsString10003() throws Exception {
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        jsonBuilder.serializeNulls(true).serializeNumberAsString(true);
        JSON json = jsonBuilder.build();
        PagingRequest<Map<String, Object>, Person> object = getPagingRequestObject();
        String jsonString = json.toJson(object);
        System.out.println(jsonString);
        CompareTools.assertJsonEquals(jsonString, getPagingRequestStringNumberAsString());
    }

    @Test(priority = 10101)
    public void testPersonSerialize10101() {
        Person object = getPersonObject();
        String jsonString = json.toJson(object);
        System.out.println(jsonString);
        CompareTools.assertJsonEquals(jsonString, getPersonString());
    }

    @Test(priority = 10102)
    public void testPersonDeserialize10102() {
        String jsonString = getPersonString();
        Person actual = json.fromJson(jsonString, Person.class);
        Person expected = getPersonObject();
        CompareTools.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10201)
    public void testPersonsSerialize10201() {
        List<Person> object = getPersonsObject();
        String jsonString = json.toJson(object);
        System.out.println(jsonString);
        CompareTools.assertJsonEquals(jsonString, getPersonsString());
    }

    @Test(priority = 10202)
    public void testPersonsDeserialize10202() {
        String jsonString = getPersonsString();
        List<Person> actual = json.fromJson(jsonString, Types.getListParameterizedType(Person.class));
        List<Person> expected = getPersonsObject();
        CompareTools.assertDeepEquals(actual, expected);
    }
}
