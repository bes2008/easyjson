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

package com.jn.easyjson.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JacksonMigrates {
    /**
     * 迁移自 ClassUtil ，从高版本迁移
     */
    public static String exceptionMessage(Throwable t) {
        if (t instanceof JsonProcessingException) {
            return ((JsonProcessingException) t).getOriginalMessage();
        }
        return t.getMessage();
    }

    /**
     * 迁移自 ClassUtil ，从高版本迁移
     */
    public static void verifyMustOverride(Class<?> expType, Object instance,
                                          String method)
    {
        if (instance.getClass() != expType) {
            throw new IllegalStateException(String.format(
                    "Sub-class %s (of class %s) must override method '%s'",
                    instance.getClass().getName(), expType.getName(), method));
        }
    }
}
