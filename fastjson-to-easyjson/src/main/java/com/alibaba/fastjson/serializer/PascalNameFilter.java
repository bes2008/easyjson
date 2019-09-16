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

package com.alibaba.fastjson.serializer;

public class PascalNameFilter implements NameFilter {

    public String process(Object source, String name, Object value) {
        if (name == null || name.length() == 0) {
            return name;
        }

        char[] chars = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);

        String pascalName = new String(chars);
        return pascalName;
    }
}
