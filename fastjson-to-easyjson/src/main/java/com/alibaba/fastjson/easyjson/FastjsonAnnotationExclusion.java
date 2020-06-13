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

package com.alibaba.fastjson.easyjson;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.jn.easyjson.core.exclusion.Exclusion;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.FieldAttributes;
import com.jn.langx.util.reflect.MethodAttributes;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.Field;
import java.lang.reflect.Member;

/**
 * 处理 fastjson  JSONType, JSONField 注解
 */
public class FastjsonAnnotationExclusion implements Exclusion {

    @Override
    public boolean shouldSkipMethod(MethodAttributes m, boolean serializePhrase) {
        return skipProperty(m.get(), serializePhrase);
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f, boolean serializePhrase) {
        return skipProperty(f.get(), serializePhrase);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz, boolean serializePhrase) {
        return false;
    }

    private boolean skipProperty(Member member, boolean serializePhrase) {
        Class beanClass = member.getDeclaringClass();
        JSONField jsonField = Reflects.getAnnotation(beanClass, JSONField.class);
        JSONType jsonType = Reflects.getAnnotation(beanClass, JSONType.class);
        String name = null;
        if (member instanceof Field) {
            name = new FieldAttributes((Field) member).getName();
        } else {
            String method = member.getName();

            if (method.startsWith("get") || method.startsWith("set")) {
                name = method.substring(3);
            } else if (method.startsWith("is")) {
                name = method.substring(2);
            }
        }
        return skipProperty(name, member.getDeclaringClass(), jsonType, jsonField, serializePhrase);
    }

    private boolean skipProperty(String propertyName, Class declaringClass, JSONType jsonType, JSONField jsonField, boolean serializePhrase) {
        String[] ignores = jsonType == null ? null : jsonType.ignores();
        if (serializePhrase) {
            if (jsonField != null) {
                return !jsonField.serialize();
            } else {
                return Collects.asList(ignores).contains(propertyName);
            }
        } else {
            // deserialize phrase
            if (jsonField != null) {
                return !jsonField.deserialize();
            } else {
                return Collects.asList(ignores).contains(propertyName);
            }
        }
    }

}
