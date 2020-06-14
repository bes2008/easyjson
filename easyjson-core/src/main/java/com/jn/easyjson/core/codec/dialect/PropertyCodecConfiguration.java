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

import java.lang.ref.WeakReference;

/**
 * 序列化，或反序列化字段、方法时的配置
 *
 * gson 是基于字段
 * jackson 是基于方法
 * fastjson 都有
 */
public class PropertyCodecConfiguration extends CodecConfiguration{

    /**
     * 字段名，如果是方法，则为去掉了 get,set,is之后的
     */
    private String name;

    /**
     * 别名
     */
    private String alias;

    /**
     * 是否是类的属性（字段、getter，setter 都视为属性）
     */
    private boolean isClassProperty;

    /**
     * 当 isClassProperty 时, classRef 才有意义
     */
    private WeakReference<Class> clazzRef;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isClassProperty() {
        return isClassProperty;
    }

    public void setClassProperty(boolean classProperty) {
        isClassProperty = classProperty;
    }

    public Class getClazz() {
        return clazzRef==null? null: clazzRef.get();
    }

    public void setClazz(Class clazz) {
        this.clazzRef = new WeakReference<Class>(clazz);
    }
}
