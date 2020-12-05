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

package com.alibaba.fastjson.serializer;

/**
 * @since 1.1.35
 */
public abstract class AfterFilter implements SerializeFilter {

    private static final ThreadLocal<JSONSerializer> serializerLocal = new ThreadLocal<JSONSerializer>();
    private static final ThreadLocal<Character> seperatorLocal = new ThreadLocal<Character>();

    private final static Character COMMA = Character.valueOf(',');

    final char writeAfter(JSONSerializer serializer, Object object, char seperator) {
        serializerLocal.set(serializer);
        seperatorLocal.set(seperator);
        writeAfter(object);
        serializerLocal.set(null);
        return seperatorLocal.get();
    }

    protected final void writeKeyValue(String key, Object value) {
        JSONSerializer serializer = serializerLocal.get();
        char seperator = seperatorLocal.get();
        serializer.writeKeyValue(seperator, key, value);
        if (seperator != ',') {
            seperatorLocal.set(COMMA);
        }
    }

    public abstract void writeAfter(Object object);
}
