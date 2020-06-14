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

package com.jn.easyjson.core.codec.dialect;

import com.jn.langx.util.reflect.Reflects;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * id 是类全名
 */
public class ClassCodecConfiguration extends CodecConfiguration {
    private WeakReference clazzRef;

    private List<String> excludePropertyNames;

    public Class getClazz() {
        return clazzRef.getClass();
    }

    public void setClazz(Class clazz) {
        this.clazzRef = new WeakReference(clazz);
        this.setId(Reflects.getFQNClassName(clazz));
    }

    public void addExcludePropertyName(String propertyName){
        excludePropertyNames.add(propertyName);
    }

    public List<String> getExcludePropertyNames() {
        return excludePropertyNames;
    }

    public void setExcludePropertyNames(List<String> excludePropertyNames) {
        this.excludePropertyNames = excludePropertyNames;
    }
}
