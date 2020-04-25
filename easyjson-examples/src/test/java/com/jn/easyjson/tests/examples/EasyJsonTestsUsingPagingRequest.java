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

package com.jn.easyjson.tests.examples;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.exclusion.IgnoreAnnotationExclusion;
import com.jn.easyjson.fastjson.FastJsonJSONBuilder;
import com.jn.easyjson.gson.GsonJSONBuilder;
import com.jn.easyjson.jackson.JacksonJSONBuilder;
import com.jn.easyjson.tests.examples.struct.PagingRequest;
import com.jn.easyjson.tests.examples.struct.PagingRequestContext;
import com.jn.easyjson.tests.examples.struct.PagingResult;
import com.jn.easyjson.tests.examples.struct.Person;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EasyJsonTestsUsingPagingRequest extends BaseTests {
    @Test
    public void testEasyJson() {
        Map<String, JSONBuilder> jsonBuilderMap = new HashMap<String, JSONBuilder>();
        jsonBuilderMap.put("jackson", new JacksonJSONBuilder());
        jsonBuilderMap.put("fastjson", new FastJsonJSONBuilder());
        jsonBuilderMap.put("gson", new GsonJSONBuilder());


        PagingRequest pagingRequest = new PagingRequest();
        pagingRequest.setCacheCount(true);
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("a", 1);
        condition.put("b", 2);
        pagingRequest.setCondition(condition);
        pagingRequest.setCountColumn("name_");
        pagingRequest.setPageNo(20);
        pagingRequest.setPageSize(50);
        PagingRequestContext pagingRequestContext = new PagingRequestContext();
        pagingRequest.setCtx(pagingRequestContext);
        PagingResult result = new PagingResult();
        result.setItems(persons);
        result.setTotal(203423);
        result.setPageNo(pagingRequest.getPageNo());
        result.setPageSize(pagingRequest.getPageSize());
        pagingRequest.setResult(result);


        for (Map.Entry<String, JSONBuilder> entry : jsonBuilderMap.entrySet()) {
            String jsonLibraryName = entry.getKey();
            System.out.println("=====================EasyJson test [" + jsonLibraryName + "] start =============================");
            JSONBuilder jsonBuilder = entry.getValue();
            JSON gson = jsonBuilder.serializeNulls(false).serializeNumberAsString(true).serializeEnumUsingValue(true).addDeserializationExclusion(new IgnoreAnnotationExclusion()).build();

            // test simple object
            String str1 = gson.toJson(pagingRequest);
            System.out.println(str1);
            Person p1 = gson.fromJson(str1, Person.class);
            System.out.println(p1.equals(pagingRequest));
            System.out.println(gson.toJson(p1));

            System.out.println("=====================EasyJson test [" + jsonLibraryName + "] end =============================");
        }
    }
}
