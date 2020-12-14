/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache, Version 2.0 (the "License");
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
import com.jn.langx.util.Numbers;
import com.jn.langx.util.reflect.type.Types;
import org.junit.Test;

import java.util.Map;

public class NumberParserTests {
    @Test
    public void test() {
        String json = "{\"id\":\"e42a8c\",\"username\":\"wyb1\",\"roleId\":\"4602e671\",\"serverAuthType\":1.0}";
        User u = JSON.parseObject(json, User.class);
        System.out.println(u);

        Map<String, Object> map = JSON.parseObject(json, Map.class);
        System.out.println(map.size());

        String json2 = "{\"id\":\"e42a8c\",\"username\":\"wyb1\",\"roleId\":\"4602e671\",\"serverAuthType\":1.0}";

        String jsonXxx = "{\"id\":\"1.0\",\"id2\":\"128.0\",\"id3\":\"10000.0\",\"id4\":\"10000.03\",\"id5\":\"1423232322323534.0\",\"id6\":\"1.0\",\"username\":2.0,\"roleId\":\"3.0\",\"serverAuthType\":1}";
        Map<String, Double> j = JSON.parseObject(jsonXxx, Types.getMapParameterizedType(String.class, Double.class));
        System.out.println(j.size());

        String jsonYyy = "{\"id\":1.0,\"id2\":128.0,\"id3\":10000.0,\"id4\":10000.03,\"id5\":1423232322323534.0,\"id6\":\"1.0\",\"id7\":1234567.0,\"username\":1231243234235423542345234.0,\"roleId\":\"3.0\",\"serverAuthType\":1}";
        Map<String, Object> k = JSON.parseObject(jsonYyy, Types.getMapParameterizedType(String.class, Object.class));
        System.out.println(k.size());

        Map m = JSON.parseObject(jsonYyy, Map.class);

        int id= Numbers.createNumber(m.get("id").toString()).intValue();
        int id2= Numbers.createNumber(m.get("id2").toString()).intValue();
        int id3= Numbers.createNumber(m.get("id3").toString()).intValue();
        int id7= Numbers.createNumber(m.get("id7").toString()).intValue();
        System.out.println(id);
        System.out.println(id2);
        System.out.println(id3);
        System.out.println(id7);
        System.out.println(m.size());
    }
}
