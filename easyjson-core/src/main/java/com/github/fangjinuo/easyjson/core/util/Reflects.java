/*
 *  Copyright 2019 the original author or authors.
 *
 *  Licensed under the LGPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.github.fangjinuo.easyjson.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Reflects {
    public static Annotation getDeclaredAnnotation(Class clazz, Class<? extends Annotation> annotationClazz){
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for(Annotation anno : annotations){
            if(annotationClazz.isInstance(anno)){
                return anno;
            }
        }
        return null;
    }

    public static boolean isInnerClass(Class<?> clazz) {
        return clazz.isMemberClass() && !isStatic(clazz);
    }

    public static boolean isStatic(Class<?> clazz) {
        return (clazz.getModifiers() & Modifier.STATIC) != 0;
    }

    public static boolean isAnonymousOrLocal(Class<?> clazz) {
        return !Enum.class.isAssignableFrom(clazz)
                && (clazz.isAnonymousClass() || clazz.isLocalClass());
    }

    public static Field getField(Class clazz, String fieldName){
        Field field = null;
        try {
            field = clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            // ignore it
        }
        return field;
    }

    public static Field getDeclaredField(Class clazz, String fieldName){
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // ignore it
        }
        return field;
    }

    public static <T>T getDeclaredFieldValue(Object object, String fieldName) {
        Field field = Reflects.getDeclaredField(object.getClass(), fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                return (T) field.get(object);
            } catch (IllegalAccessException e) {
                // ignore it
            }
        }
        return null;
    }
}
