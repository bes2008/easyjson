package com.jn.easyjson.tests.cases;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.jn.easyjson.tests.utils.CompareTools;

import java.util.Date;

/**
 * FastJson注解测试类
 *
 * @author zhaohuihua
 * @version 20200614
 */
@Test
public class FastjsonAnnotationTest {

    protected UserEntity1 getUserEntity1Object() {
        UserEntity1 user = new UserEntity1();
        user.setId("1001");
        user.setName("Test1");
        user.setPassword("a@b@c");
        return user;
    }

    protected UserEntity2 getUserEntity2Object() {
        UserEntity2 user = new UserEntity2();
        user.setId("1001");
        user.setName("Test1");
        user.setPassword("a@b@c");
        user.setBirthday(new Date(1577808000000L));
        return user;
    }

    protected UserEntity3 getUserEntity3Object() {
        UserEntity3 user = new UserEntity3();
        user.setId("1001");
        user.setName("Test1");
        user.setPassword("a@b@c");
        return user;
    }

    protected UserEntity4 getUserEntity4Object() {
        UserEntity4 user = new UserEntity4();
        user.setId("1001");
        user.setName("Test1");
        user.setPassword("a@b@c");
        return user;
    }

    protected String getUserEntityString() {
        return "{\"id\":\"1001\",\"name\":\"Test1\",\"password\":\"a@b@c\"}";
    }

    protected String getUserEntity2String() {
        return "{\"id\":\"1001\",\"name\":\"Test1\",\"password\":\"a@b@c\",\"birthday\":1577808000000}";
    }

    protected String getUserEntityIgnorePasswordString() {
        return "{\"id\":\"1001\",\"name\":\"Test1\"}";
    }

    protected String getUserEntity2IgnorePasswordString() {
        return "{\"id\":\"1001\",\"name\":\"Test1\",\"birthday\":1577808000000}";
    }

    @Test(priority = 10001)
    public void testSerialize10001() {
        UserEntity1 user = getUserEntity1Object();
        String jsonString = JSONObject.toJSONString(user);
        System.out.println(jsonString);
        Assert.assertEquals(jsonString, getUserEntityIgnorePasswordString());
    }

    @Test(priority = 10002)
    public void testDeserialize10002() {
        String jsonString = getUserEntityString();
        UserEntity1 actual = JSONObject.parseObject(jsonString, UserEntity1.class);
        UserEntity1 expected = getUserEntity1Object();
        expected.setPassword(null);
        CompareTools.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10003)
    public void testSerialize10003() {
        UserEntity2 user = getUserEntity2Object();
        String jsonString = JSONObject.toJSONString(user);
        System.out.println(jsonString);
        Assert.assertEquals(jsonString, getUserEntity2IgnorePasswordString());
    }

    @Test(priority = 10004)
    public void testDeserialize10004() {
        String jsonString = getUserEntity2String();
        UserEntity2 actual = JSONObject.parseObject(jsonString, UserEntity2.class);
        UserEntity2 expected = getUserEntity2Object();
        expected.setPassword(null);
        CompareTools.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10005)
    public void testSerialize10005() {
        UserEntity3 user = getUserEntity3Object();
        String jsonString = JSONObject.toJSONString(user);
        System.out.println(jsonString);
        Assert.assertEquals(jsonString, getUserEntityIgnorePasswordString());
    }

    @Test(priority = 10006)
    public void testDeserialize10006() {
        String jsonString = getUserEntityString();
        UserEntity3 actual = JSONObject.parseObject(jsonString, UserEntity3.class);
        UserEntity3 expected = getUserEntity3Object();
        expected.setPassword(null);
        CompareTools.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10007)
    public void testSerialize10007() {
        UserEntity4 user = getUserEntity4Object();
        String jsonString = JSONObject.toJSONString(user);
        System.out.println(jsonString);
        Assert.assertEquals(jsonString, getUserEntityIgnorePasswordString());
    }

    @Test(priority = 10008)
    public void testDeserialize10008() {
        String jsonString = getUserEntityString();
        UserEntity4 actual = JSONObject.parseObject(jsonString, UserEntity4.class);
        UserEntity4 expected = getUserEntity4Object();
        expected.setPassword(null);
        CompareTools.assertDeepEquals(actual, expected);
    }

    protected static class UserEntity1 {

        private String id;
        private String name;
        private String password;

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

    protected static class UserEntity2 {

        private String id;
        private String name;
        @JSONField(serialize = false, deserialize = false)
        private String password;
        @JSONField(format = "yyyy-MM-dd")
        private Date birthday;


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

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }
    }

    @JSONType(ignores = "password")
    protected static class UserEntity3 {

        private String id;
        private String name;
        private String password;

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

    @JSONType(includes = { "id", "name" })
    protected static class UserEntity4 {

        private String id;
        private String name;
        private String password;

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
