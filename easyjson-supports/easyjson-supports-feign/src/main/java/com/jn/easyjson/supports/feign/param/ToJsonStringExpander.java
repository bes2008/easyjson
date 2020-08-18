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
import com.jn.langx.util.reflect.Reflects;
import feign.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToJsonStringExpander implements Param.Expander {
    private static final Logger logger = LoggerFactory.getLogger(ToJsonStringExpander.class);
    private final JSON jsons = JSONBuilderProvider.simplest();
    @Override
    public String expand(Object value) {
        if (value == null) {
            return "";
        }
        try {
            return jsons.toJson(value);
        } catch (Throwable ex) {
            logger.error("error occur when convert a {} object to a json string", Reflects.getFQNClassName(value.getClass()));
        }
        return "";
    }
}
