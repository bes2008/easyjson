package com.jn.easyjson.tests.cases;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import org.testng.annotations.Test;
import com.gitee.qdbp.tools.files.PathTools;
import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.tests.entity.user.Address;
import com.jn.easyjson.tests.entity.user.Gender;
import com.jn.easyjson.tests.entity.user.UserEntity;
import com.jn.easyjson.tests.utils.Asserts;

/**
 * 基础测试类
 *
 * @author zhaohuihua
 * @version 20200614
 */
public abstract class EasyJsonBaseTest extends AbstractBaseTest {

    private static JSON json = JSONBuilderProvider.simplest();
    private static Charset UTF8 = Charset.forName("UTF-8");

    protected UserEntity getUserEntityObject() {
        UserEntity user = new UserEntity();
        user.setId("1001");
        user.setName("Test1");
        user.setGender(Gender.FEMALE);
        user.setPassword("a@b@c");
        user.setWeight(60.5);
        user.setHeight(170);
        user.setBirthday(new Date(1577808000000L));
        user.addAddress(new Address("home", "Nanjing China"));
        user.addAddress(new Address("office", "Beijing China"));
        user.setIntro("This's a test user entity. \"EasyJson\"");
        return user;
    }

    protected String getUserEntityString() {
        return readClassResourceText(EasyJsonBaseTest.class, "json/BaseTestUserEntityString.json");
    }

    @Test(priority = 10001)
    public void testSerialize10001() {
        UserEntity user = getUserEntityObject();
        String jsonString = json.toJson(user);
        System.out.println(jsonString);
        Asserts.assertJsonEquals(jsonString, getUserEntityString());
    }

    @Test(priority = 10002)
    public void testDeserialize10002() {
        String jsonString = getUserEntityString();
        UserEntity actual = json.fromJson(jsonString, UserEntity.class);
        UserEntity expected = getUserEntityObject();
        Asserts.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10003)
    public void testReader10003() throws IOException {
        URL url = PathTools.findClassResource(EasyJsonBaseTest.class, "json/BaseTestUserEntityString.json");
        try (InputStream input = url.openStream();
                Reader reader = new InputStreamReader(input, UTF8)) {
            UserEntity actual = json.fromJson(reader, UserEntity.class);
            UserEntity expected = getUserEntityObject();
            Asserts.assertDeepEquals(actual, expected);
        }
    }
}
