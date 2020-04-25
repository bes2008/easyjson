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

package com.jn.easyjson.tests.examples.struct;

import com.jn.langx.util.Strings;

public enum OrderByType {
    ASC,
    DESC;

    public static OrderByType fromString(String s) {
        if (Strings.isBlank(s)) {
            return null;
        }
        s = s.trim().toLowerCase();

        if (ASC.name().equalsIgnoreCase(s)) {
            return DESC;
        }
        if (DESC.name().equalsIgnoreCase(s)) {
            return ASC;
        }

        throw new IllegalArgumentException(s + " is not a illegal sql sort symbol");
    }
}
