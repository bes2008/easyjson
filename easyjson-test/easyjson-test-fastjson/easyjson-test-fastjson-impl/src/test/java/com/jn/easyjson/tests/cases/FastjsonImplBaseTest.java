package com.jn.easyjson.tests.cases;

import org.testng.annotations.Test;

/**
 * FastjsonToGsonBaseTest基础测试
 *
 * @author zhaohuihua
 * @version 20200614
 */
@Test
public class FastjsonImplBaseTest extends EasyJsonBaseTest {

    protected String getUserEntityString() {
        return "{\"addresses\":[{\"details\":\"Nanjing China\",\"name\":\"home\"},{\"details\":\"Beijing China\",\"name\":\"office\"}],\"birthday\":1577808000000,\"gender\":\"FEMALE\",\"height\":170,\"id\":\"1001\",\"intro\":\"This's a test user entity. \\\"EasyJson\\\"\",\"name\":\"Test1\",\"password\":\"a@b@c\",\"weight\":60.5}";
    }
}
