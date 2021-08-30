package com.alibaba.fastjson.easyjson;

import com.alibaba.fastjson.annotation.JSONField;
import com.jn.easyjson.core.codec.dialect.BeanPropertyFinder;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public class JsonFieldFinder implements BeanPropertyFinder {
    @Override
    public String apply(Class declaringClass, final String propertyName) {
        if (declaringClass == Object.class) {
            return null;
        }

        Field[] declaredFields = declaringClass.getDeclaredFields();
        Member member = Pipeline.of(declaredFields).findFirst(JsonFieldFinder.<Field>jsonFieldAnnotatedMemberPredicate(propertyName));
        if (member == null) {
            Method[] declaredMethods = declaredFields.getClass().getMethods();
            member = Pipeline.of(declaredMethods)
                    .filter(new Predicate<Method>() {
                        @Override
                        public boolean test(Method m) {
                            return Reflects.isGetterOrSetter(m);
                        }
                    })
                    .findFirst(JsonFieldFinder.<Method>jsonFieldAnnotatedMemberPredicate(propertyName));
        }
        if (member != null) {
            return Reflects.extractFieldName(member);
        }
        Class parentClass = declaringClass.getSuperclass();
        String memberName = null;
        if (parentClass != Object.class) {
            memberName = apply(parentClass, propertyName);
        }
        return memberName;
    }

    private static <T extends Member> Predicate<T> jsonFieldAnnotatedMemberPredicate(final String propertyName) {
        return new Predicate<T>() {
            @Override
            public boolean test(T m) {
                if (m instanceof AnnotatedElement) {
                    AnnotatedElement annotated = (AnnotatedElement) m;
                    JSONField jsonField = Reflects.getAnnotation(annotated, JSONField.class);
                    if (jsonField == null) {
                        return false;
                    }
                    String alias = jsonField.name();
                    if (Strings.isNotBlank(alias)) {
                        if (alias.equals(propertyName)) {
                            return true;
                        }
                    }
                    String[] alternates = jsonField.alternateNames();
                    return Collects.contains(alternates, propertyName);
                }
                return false;
            }
        };

    }
}
