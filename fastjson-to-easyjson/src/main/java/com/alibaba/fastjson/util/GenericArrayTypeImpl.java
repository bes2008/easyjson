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

package com.alibaba.fastjson.util;


import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

public class GenericArrayTypeImpl implements GenericArrayType {
    private final Type genericComponentType;

    public GenericArrayTypeImpl(Type genericComponentType) {
        assert genericComponentType != null;
        this.genericComponentType = genericComponentType;
    }

    @Override
    public Type getGenericComponentType() {
        return this.genericComponentType;
    }

    @Override
    public String toString() {
        Type genericComponentType = this.getGenericComponentType();
        StringBuilder builder = new StringBuilder();
        if (genericComponentType instanceof Class) {
            builder.append(((Class) genericComponentType).getName());
        } else {
            builder.append(genericComponentType.toString());
        }
        builder.append("[]");
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GenericArrayType) {
            GenericArrayType that = (GenericArrayType) obj;
            return this.genericComponentType.equals(that.getGenericComponentType());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.genericComponentType.hashCode();
    }
}
