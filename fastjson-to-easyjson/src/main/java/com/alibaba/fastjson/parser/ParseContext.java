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

package com.alibaba.fastjson.parser;

import java.lang.reflect.Type;

public class ParseContext {

    public Object object;
    public final ParseContext parent;
    public final Object fieldName;
    public Type type;
    private transient String path;

    public ParseContext(ParseContext parent, Object object, Object fieldName) {
        this.parent = parent;
        this.object = object;
        this.fieldName = fieldName;
    }

    public String toString() {
        if (path == null) {
            if (parent == null) {
                path = "$";
            } else {
                if (fieldName instanceof Integer) {
                    path = parent.toString() + "[" + fieldName + "]";
                } else {
                    path = parent.toString() + "." + fieldName;
                }
            }
        }

        return path;
    }
}
