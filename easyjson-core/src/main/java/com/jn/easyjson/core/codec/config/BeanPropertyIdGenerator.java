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

package com.jn.easyjson.core.codec.config;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.reflect.Reflects;

public class BeanPropertyIdGenerator extends PropertyIdGenerator {
    private Class beanClazz;
    private String propertyName;

    public BeanPropertyIdGenerator withBeanClass(Class beanClass) {
        this.beanClazz = beanClass;
        return this;
    }

    public BeanPropertyIdGenerator withPropertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    @Override
    public String get() {
        return get(this.propertyName);
    }

    @Override
    public String get(String propertyName) {
        Preconditions.checkNotNull(beanClazz);
        Preconditions.checkNotNull(propertyName);
        return Reflects.getFQNClassName(beanClazz) + "#" + propertyName;
    }
}
