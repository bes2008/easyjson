/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.easyjson.tests.fastjson;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Date;

public class FastjsonAnnotationTests {
    @Test
    public void test(){
        User user = new User();
        user.setBirthday(new Date());
        user.setId("zhangsan_id");
        user.setEmail("zhangsan@163.com");
        user.setDescription("hello, zhangsan");
        user.setPhoneNumber("123123123");
        user.setServerAuthType(1);
        user.setUsername("zhangsan");

        System.out.println(JSON.toJSONString(user));
    }

}
