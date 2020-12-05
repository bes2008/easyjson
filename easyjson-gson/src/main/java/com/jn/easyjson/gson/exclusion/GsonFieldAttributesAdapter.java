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

package com.jn.easyjson.gson.exclusion;

import com.jn.langx.util.reflect.FieldAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;

public class GsonFieldAttributesAdapter extends FieldAttributes {
    private com.google.gson.FieldAttributes delegate;

    public GsonFieldAttributesAdapter(Field f) {
        this(new com.google.gson.FieldAttributes(f), f);
    }

    public GsonFieldAttributesAdapter(com.google.gson.FieldAttributes f, Field field) {
        super(field);
        this.delegate = f;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return delegate.getDeclaringClass();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public Type getDeclaredType() {
        return delegate.getDeclaredType();
    }

    @Override
    public Class<?> getDeclaredClass() {
        return delegate.getDeclaredClass();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotation) {
        return delegate.getAnnotation(annotation);
    }

    @Override
    public Collection<Annotation> getAnnotations() {
        return delegate.getAnnotations();
    }

    @Override
    public boolean hasModifier(int modifier) {
        return delegate.hasModifier(modifier);
    }
}
