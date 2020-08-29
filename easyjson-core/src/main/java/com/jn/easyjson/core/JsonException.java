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

package com.jn.easyjson.core;

public class JsonException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JsonException(String msg) {
        super(msg);
    }

    public JsonException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Creates exception with the specified cause. Consider using
     * {@link #JsonException(String, Throwable)} instead if you can
     * describe what actually happened.
     *
     * @param cause root exception that caused this exception to be thrown.
     */
    public JsonException(Throwable cause) {
        super(cause);
    }

    public static JsonException wrapAsJsonException(Throwable ex){
        if(ex instanceof JsonException){
            return  (JsonException)ex;
        }
        return new JsonException(ex.getMessage(), ex);
    }

}
