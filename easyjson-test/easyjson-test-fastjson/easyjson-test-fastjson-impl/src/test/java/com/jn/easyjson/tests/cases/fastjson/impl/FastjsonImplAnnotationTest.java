package com.jn.easyjson.tests.cases.fastjson.impl;

import com.jn.easyjson.tests.cases.fastjson.FastjsonAnnotationTest;
import org.testng.annotations.Test;

/**
 * FastJson注解测试类
 *
 * @author zhaohuihua
 * @version 20200614
 */
@Test
public class FastjsonImplAnnotationTest extends FastjsonAnnotationTest {

    @Override
    @Test(priority = 10006)
    public void testDeserializeUseIgnores10006() {
        System.err.println("Skip this testcase, @JSONType(ignores=xxx) is not used in Fastjson Deserialize.");
    }

    @Override
    @Test(priority = 10008)
    public void testDeserializeUseIncludes10008() {
        System.err.println("Skip this testcase, @JSONType(includes=xxx) is not used in Fastjson Deserialize.");
    }
}
