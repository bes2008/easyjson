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

package com.jn.easyjson.supports.feign;

import com.jn.easyjson.core.JSONFactory;
import com.jn.langx.http.rest.RestRespBody;
import com.jn.langx.util.reflect.type.Types;
import feign.Response;

import java.io.IOException;

public class Feigns {
    public static <T>RestRespBody<T> toRestRespBody(Response response, JSONFactory jsonFactory, Class<T> dataClass) throws IOException {
        return jsonFactory.get().fromJson(response.body().asReader(), Types.getParameterizedType(RestRespBody.class, dataClass));
    }
}
