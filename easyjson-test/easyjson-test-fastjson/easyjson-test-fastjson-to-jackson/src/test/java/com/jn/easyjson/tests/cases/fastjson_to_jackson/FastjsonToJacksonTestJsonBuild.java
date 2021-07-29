package com.jn.easyjson.tests.cases.fastjson_to_jackson;

import com.alibaba.fastjson.JSON;
import com.jn.easyjson.tests.cases.EasyJsonBaseTest;
import com.jn.easyjson.tests.entity.user.UserEntity;
import com.jn.langx.util.Dates;
import org.testng.annotations.Test;

@Test
public class FastjsonToJacksonTestJsonBuild extends EasyJsonBaseTest {

    @Test(priority = 10001)
    public void testJsonbuild(){
        UserEntity userEntity = getUserEntityObject();
        System.out.println(JSON.toJSONStringWithDateFormat(userEntity, Dates.yyyy_MM_dd));
        System.out.println(JSON.toJSONStringWithDateFormat(userEntity, Dates.yyyy_MM_dd));
    }
}
