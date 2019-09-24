/*
 * Copyright 2019 the original author or authors.
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
import com.jn.langx.util.reflect.type.Types;
import org.junit.Test;

import java.util.Map;

public class NumberParserTests {
    @Test
    public void test() {
        String json = "{\"id\":\"e42a8c\",\"username\":\"wyb1\",\"roleId\":\"4602e671\",\"serverAuthType\":1.0}";
        User u = JSON.parseObject(json, User.class);
        System.out.println(u);

        Map map = JSON.parseObject(json, Map.class);
        System.out.println(map.size());

        String json2 = "{\"id\":\"e42a8c\",\"username\":\"wyb1\",\"roleId\":\"4602e671\",\"serverAuthType\":1.0}";

        String jsonXxx = "{\"id\":\"1.0\",\"username\":2.0,\"roleId\":\"3.0\",\"serverAuthType\":1}";
        Map<String,Integer> j = JSON.parseObject(jsonXxx, Types.getMapParameterizedType(String.class, Integer.class));
        System.out.println(j.size());
    }
}
