package com.jn.easyjson.tests.cases;

import java.util.Date;
import org.testng.annotations.Test;
import com.jn.easyjson.tests.annotation.JsonIgnore;
import com.jn.easyjson.tests.utils.Asserts;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;
import net.sf.json.processors.JsDateJsonValueProcessor;

/**
 * Jsonlib注解测试类
 *
 * @author zhaohuihua
 * @version 20200614
 */
@Test
public abstract class JsonlibAnnotationTest extends AbstractBaseTest {

    private static JsonConfig config = new JsonConfig();
    static {
        config.addIgnoreFieldAnnotation(JsonIgnore.class);
        config.registerJsonValueProcessor(Date.class, new JsDateJsonValueProcessor());
        config.registerJsonBeanProcessor(Date.class, new JsDateJsonBeanProcessor());
    }

    protected String javaBeanToJsonString(Object javaBean) {
        return JSONObject.fromObject(javaBean, config).toString();
    }

    @SuppressWarnings("unchecked")
    protected <T> T jsonStringToJavaBean(String jsonString, Class<T> clazz) {
        JSONObject json = JSONObject.fromObject(jsonString, config);
        return (T) JSONObject.toBean(json, clazz);
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
        return "{\"birthday\":\"1577845230400\",\"id\":\"1001\",\"name\":\"Test1\"}";
    }

    @Test(priority = 10001)
    public void testSerializeUseMethodIgnore10001() {
        UserEntity1UseMethodIgnore user = getUserEntity1Object();
        String actual = javaBeanToJsonString(user);
        System.out.println(actual);
        String expected = getUserEntityIgnorePasswordString();
        System.out.println(expected);
    }

    @Test(priority = 10002)
    public void testDeserializeUseMethodIgnore10002() {
        String jsonString = getUserEntityString();
        UserEntity2UseFieldIgnore actual = jsonStringToJavaBean(jsonString, UserEntity2UseFieldIgnore.class);
        UserEntity1UseMethodIgnore expected = getUserEntity1Object();
        expected.setPassword(null);
        expected.setBirthday(new Date(1577808000000L));
    }

    @Test(priority = 10003)
    public void testSerializeUseFieldIgnore10003() {
        UserEntity2UseFieldIgnore user = getUserEntity2Object();
        String actual = javaBeanToJsonString(user);
        System.out.println(actual);
        String expected = getUserEntityIgnorePasswordString();
        System.out.println(expected);
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
