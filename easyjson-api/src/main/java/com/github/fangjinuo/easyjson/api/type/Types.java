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

package com.github.fangjinuo.easyjson.api.type;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author jinuo.fang
 */
public class Types {

    public static boolean isPrimitive(Type type) {
        return Primitives.isPrimitive(type);
    }

    public static boolean isClass(Type type) {
        try {
            Class clazz = (Class) type;
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    public static boolean isParameterizedType(Type type) {
        return (type instanceof ParameterizedType);
    }

    public static Class getPrimitiveWrapClass(Type type) {
        return Primitives.wrap(type);
    }

    public static Class<?> toClass(Type o) {
        if (o instanceof GenericArrayType)
            return Array.newInstance(toClass(((GenericArrayType) o).getGenericComponentType()),
                    0)
                    .getClass();
        if (isPrimitive(o)) {
            return getPrimitiveWrapClass(o);
        }
        if (isClass(o)) {
            return (Class<?>) o;
        }
        if (isParameterizedType(o)) {
            ParameterizedType type = (ParameterizedType) o;
            return getParameterizedTypeWithOwnerType(type.getOwnerType(), type.getRawType(), type.getActualTypeArguments()).getClass();
        }
        throw new IllegalArgumentException();
    }

    public static ParameterizedType getListParameterizedType(Type elementType) {
        return getParameterizedType(List.class, elementType);
    }

    public static ParameterizedType getMapParameterizedType(Type keyType, Type valueType) {
        return getParameterizedType(Map.class, keyType, valueType);
    }

    public static ParameterizedType getParameterizedType(Type rawType) {
        return new ParameterizedTypeImpl(null, rawType);
    }

    public static ParameterizedType getParameterizedType(Type rawType, Type... typeArguments) {
        return getParameterizedTypeWithOwnerType(null, rawType, typeArguments);
    }

    public static ParameterizedType getParameterizedTypeWithOwnerType(Type ownerType, Type rawType, Type... typeArguments) {
        return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
    }

}
