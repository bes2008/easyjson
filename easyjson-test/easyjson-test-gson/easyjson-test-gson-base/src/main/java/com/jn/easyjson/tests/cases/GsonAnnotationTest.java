package com.jn.easyjson.tests.cases;

import java.util.Date;
import org.testng.annotations.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.jn.easyjson.tests.utils.Asserts;

/**
 * Gson注解测试类
 *
 * @author zhaohuihua
 * @version 20200614
 */
@Test
public abstract class GsonAnnotationTest extends AbstractBaseTest {

    private static Gson gson;
    static {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        builder.setDateFormat("yyyy-MM-dd");
        gson = builder.create();
    }

    protected String javaBeanToJsonString(Object javaBean) {
        return gson.toJson(javaBean);
    }

    protected <T> T jsonStringToJavaBean(String jsonString, Class<T> clazz) {
        return gson.fromJson(jsonString, clazz);
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

    protected static class UserEntity2UseFieldIgnore {
        @Expose
        private Date birthday;
        @Expose
        private String id;
        @Expose
        private String name;
        @Expose(serialize = false, deserialize = false)
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
