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

package com.jn.easyjson.supports.feign.param;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.langx.http.HttpQueryStrings;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.net.UrlEncoder;
import feign.Param;

import java.util.Map;

public class ToQueryStringExpander implements Param.Expander{
    private static JSON jsons = JSONBuilderProvider.simplest();
    @Override
    public String expand(Object value) {
        if(value==null){
            return "";
        }
        String jsonString = jsons.toJson(value);
        Map<String, Object> map = jsons.fromJson(jsonString, Map.class);
        String queryString = HttpQueryStrings.toQueryString(map,null);
        queryString = new UrlEncoder().encode(queryString, Charsets.UTF_8);
        return queryString;
    }
}
