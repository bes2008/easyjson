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

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jn.easyjson.tests.examples.BaseTests;
import org.junit.Test;

public class JSONSerializerTests extends BaseTests {
    @Test
    public void test(){
        SerializeWriter out = new SerializeWriter();
        SerializeConfig config =  new SerializeConfig();
        JSONSerializer serializer = new JSONSerializer(out,config);
        serializer.config(SerializerFeature.QuoteFieldNames, true);
        serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
        serializer.write(person);
        System.out.println(out.toString());
    }
}
