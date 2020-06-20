package com.jn.easyjson.tests.cases;

import java.util.Date;
import org.testng.annotations.Test;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jn.easyjson.tests.utils.Asserts;

/**
 * Jackson注解测试类
 *
 * @author zhaohuihua
 * @version 20200614
 */
@Test
public abstract class JacksonAnnotationTest extends AbstractBaseTest {

    protected String javaBeanToJsonString(Object javaBean) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(javaBean);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> T jsonStringToJavaBean(String jsonString, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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

    protected static class UserEntity1UseMethodIgnore {

        private Date birthday;
        private String id;
        private String name;
        private String password;

        @JsonFormat(pattern = "yyyy-MM-dd")
        public Date getBirthday() {
            return birthday;
        }

        @JsonFormat(pattern = "yyyy-MM-dd")
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

        @JsonIgnore
        public String getPassword() {
            return password;
        }

        @JsonIgnore
        public void setPassword(String password) {
            this.password = password;
        }

    }

    protected static class UserEntity2UseFieldIgnore {

        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date birthday;
        private String id;
        private String name;
        @JsonIgnore
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
