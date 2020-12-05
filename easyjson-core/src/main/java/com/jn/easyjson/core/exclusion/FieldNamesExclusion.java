/*
 * Copyright 2020 the original author or authors.
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

package com.jn.easyjson.core.exclusion;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.FieldAttributes;
import com.jn.langx.util.reflect.MethodAttributes;

import java.util.List;

public class FieldNamesExclusion implements Exclusion {
    /**
     * 排除的字段名
     */
    private List<String> fieldNames = Collects.emptyArrayList();

    public FieldNamesExclusion(List<String> fieldNames) {
        Collects.addAll(this.fieldNames, fieldNames);
    }

    public void addField(String name) {
        this.fieldNames.add(name);
    }

    @Override
    public boolean shouldSkipMethod(MethodAttributes m, boolean serializePhrase) {
        final String method = m.getName();
        return Collects.anyMatch(fieldNames, new Predicate<String>() {
            @Override
            public boolean test(String fieldName) {
                return ("set" + fieldName).equalsIgnoreCase(method)
                        || ("get" + fieldName).equalsIgnoreCase(method)
                        || ("is" + fieldName).equalsIgnoreCase(method);
            }
        });
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f, boolean serializePhrase) {
        return fieldNames.contains(f.getName());
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz, boolean serializePhrase) {
        return false;
    }
}
