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

package com.jn.easyjson.supports.feign.codec;

import com.jn.langx.util.io.IOs;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class EasyjsonErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        FeignRestRespBodyException exception = new FeignRestRespBodyException();
        try {
            String body = IOs.readAsString(response.body().asReader());
            exception.setMethodKey(methodKey);
            exception.setStatusCode(response.status());
            exception.setResponseBody(body);
        } catch (IOException ex) {
            return new Default().decode(methodKey, response);
        }
        return exception;
    }

}
