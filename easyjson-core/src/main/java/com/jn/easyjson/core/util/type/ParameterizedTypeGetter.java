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

package com.jn.easyjson.core.util.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Usage:
 * <p>
 * <pre>
 *  List:
 *  ParameterizedType type0 = new ParameterizedTypeGetter<List<Person>>(){}.getType();
 *  ParameterizedType type1 = Types.getParameterizedType(List.class, Person);
 *  ParameterizedType type2 = new ParameterizedType(null, List.class, Person);
 *
 *  type0 equivalent to type1
 *  type0 equivalent to type2
 *
 * </pre>
 *
 * @param <T>
 */
public class ParameterizedTypeGetter<T> {
    public Type getType() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        return genericSuperclass.getActualTypeArguments()[0];
    }
}
