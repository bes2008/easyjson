package com.jn.easyjson.tests.cases;

import org.testng.annotations.Test;

/**
 * FastjsonToJackson基础测试
 *
 * @author zhaohuihua
 * @version 20200614
 */
@Test
public class FastjsonToJacksonBaseTest extends EasyJsonBaseTest {

    protected String getUserEntityString() {
        // 默认设置下, jackson是按字段声明顺序输出的, fastjson是按字段字母顺序输出的, jackson胜出
        return "{\"id\":\"1001\",\"name\":\"Test1\",\"gender\":\"FEMALE\",\"password\":\"a@b@c\",\"weight\":60.5,\"height\":170,\"birthday\":1577808000000,\"intro\":\"This's a test user entity. \\\"EasyJson\\\"\",\"addresses\":[{\"name\":\"home\",\"details\":\"Nanjing China\"},{\"name\":\"office\",\"details\":\"Beijing China\"}]}";
    }
}
