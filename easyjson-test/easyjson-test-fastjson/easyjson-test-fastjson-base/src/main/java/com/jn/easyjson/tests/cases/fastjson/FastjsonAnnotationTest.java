package com.jn.easyjson.tests.cases.fastjson;

import com.jn.easyjson.tests.cases.AbstractBaseTest;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.jn.easyjson.tests.utils.Asserts;

import java.util.Date;

/**
 * FastJson注解测试类
 *
 * @author zhaohuihua
 * @version 20200614
 */
@Test
public abstract class FastjsonAnnotationTest extends AbstractBaseTest {

    protected String javaBeanToJsonString(Object javaBean) {
        return JSONObject.toJSONString(javaBean);
    }

    protected <T> T jsonStringToJavaBean(String jsonString, Class<T> clazz) {
        return JSONObject.parseObject(jsonString, clazz);
    }

    protected UserEntity1UseMethodIgnore getUserEntity1Object() {
        UserEntity1UseMethodIgnore user = new UserEntity1UseMethodIgnore();
        user.setId("1001");
        user.setName("Test1");
        user.setPassword("a@b@c");
        user.setBirthday(new Date(1577845230400L));
        return user;
    }

    protected UserEntity2UseFieldIgnore getUserEntity2Object() {
        UserEntity2UseFieldIgnore user = new UserEntity2UseFieldIgnore();
        user.setId("1001");
        user.setName("Test1");
        user.setPassword("a@b@c");
        user.setBirthday(new Date(1577845230400L));
        return user;
    }

    protected UserEntity3UseIgnores getUserEntity3Object() {
        UserEntity3UseIgnores user = new UserEntity3UseIgnores();
        user.setId("1001");
        user.setName("Test1");
        user.setPassword("a@b@c");
        user.setBirthday(new Date(1577845230400L));
        return user;
    }

    protected UserEntity4UseIncludes getUserEntity4Object() {
        UserEntity4UseIncludes user = new UserEntity4UseIncludes();
        user.setId("1001");
        user.setName("Test1");
        user.setPassword("a@b@c");
        user.setBirthday(new Date(1577845230400L));
        return user;
    }

    protected String getUserEntityString() {
        return "{\"birthday\":\"2020-01-01\",\"id\":\"1001\",\"name\":\"Test1\",\"password\":\"a@b@c\"}";
    }

    protected String getUserEntityIgnorePasswordString() {
        return "{\"birthday\":\"2020-01-01\",\"id\":\"1001\",\"name\":\"Test1\"}";
    }

    @Test(priority = 10001)
    public void testSerializeUseMethodIgnore10001() {
        UserEntity1UseMethodIgnore user = getUserEntity1Object();
        String actual = javaBeanToJsonString(user);
        System.out.println(actual);
        String expected = getUserEntityIgnorePasswordString();
        System.out.println(expected);
        Asserts.assertJsonEquals(actual, expected);
    }

    @Test(priority = 10002)
    public void testDeserializeUseMethodIgnore10002() {
        String jsonString = getUserEntityString();
        UserEntity1UseMethodIgnore actual = jsonStringToJavaBean(jsonString, UserEntity1UseMethodIgnore.class);
        UserEntity1UseMethodIgnore expected = getUserEntity1Object();
        expected.setPassword(null);
        expected.setBirthday(new Date(1577808000000L));
        Asserts.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10003)
    public void testSerializeUseFieldIgnore10003() {
        UserEntity2UseFieldIgnore user = getUserEntity2Object();
        String actual = javaBeanToJsonString(user);
        System.out.println(actual);
        String expected = getUserEntityIgnorePasswordString();
        System.out.println(expected);
        Asserts.assertJsonEquals(actual, expected);
    }

    @Test(priority = 10004)
    public void testDeserializeUseFieldIgnore10004() {
        String jsonString = getUserEntityString();
        UserEntity2UseFieldIgnore actual = jsonStringToJavaBean(jsonString, UserEntity2UseFieldIgnore.class);
        UserEntity2UseFieldIgnore expected = getUserEntity2Object();
        expected.setPassword(null);
        expected.setBirthday(new Date(1577808000000L));
        Asserts.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10005)
    public void testSerializeUseIgnores10005() {
        UserEntity3UseIgnores user = getUserEntity3Object();
        String actual = javaBeanToJsonString(user);
        System.out.println(actual);
        String expected = getUserEntityIgnorePasswordString();
        System.out.println(expected);
        Asserts.assertJsonEquals(actual, expected);
    }

    @Test(priority = 10006)
    public void testDeserializeUseIgnores10006() {
        String jsonString = getUserEntityString();
        UserEntity3UseIgnores actual = jsonStringToJavaBean(jsonString, UserEntity3UseIgnores.class);
        UserEntity3UseIgnores expected = getUserEntity3Object();
        expected.setPassword(null);
        expected.setBirthday(new Date(1577808000000L));
        Asserts.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10007)
    public void testSerializeUseIncludes10007() {
        UserEntity4UseIncludes user = getUserEntity4Object();
        String actual = javaBeanToJsonString(user);
        System.out.println(actual);
        String expected = getUserEntityIgnorePasswordString();
        System.out.println(expected);
        Asserts.assertJsonEquals(actual, expected);
    }

    @Test(priority = 10008)
    public void testDeserializeUseIncludes10008() {
        String jsonString = getUserEntityString();
        UserEntity4UseIncludes actual = jsonStringToJavaBean(jsonString, UserEntity4UseIncludes.class);
        UserEntity4UseIncludes expected = getUserEntity4Object();
        expected.setPassword(null);
        expected.setBirthday(new Date(1577808000000L));
        Asserts.assertDeepEquals(actual, expected);
    }

    protected static class UserEntity1UseMethodIgnore {

        private Date birthday;
        private String id;
        private String name;
        private String password;

        @JSONField(format = "yyyy-MM-dd")
        public Date getBirthday() {
            return birthday;
        }

        @JSONField(format = "yyyy-MM-dd")
        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JSONField(serialize = false)
        public String getPassword() {
            return password;
        }

        @JSONField(deserialize = false)
        public void setPassword(String password) {
            this.password = password;
        }

    }

    protected static class UserEntity2UseFieldIgnore {

        @JSONField(format = "yyyy-MM-dd")
        private Date birthday;
        private String id;
        private String name;
        @JSONField(serialize = false, deserialize = false)
        private String password;

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @JSONType(ignores = "password")
    protected static class UserEntity3UseIgnores {

        @JSONField(format = "yyyy-MM-dd")
        private Date birthday;
        private String id;
        private String name;
        private String password;

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

    @JSONType(includes = { "id", "name", "birthday" })
    protected static class UserEntity4UseIncludes {

        @JSONField(format = "yyyy-MM-dd")
        private Date birthday;
        private String id;
        private String name;
        private String password;

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
