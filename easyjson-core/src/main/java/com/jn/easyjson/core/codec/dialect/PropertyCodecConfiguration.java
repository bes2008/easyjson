/*
 * Copyright 2020 the original author or authors.
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

package com.jn.easyjson.core.codec.dialect;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.Reflects;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * 序列化，或反序列化字段、方法时的配置
 * <p>
 * gson 是基于字段
 * jackson 是基于方法
 * fastjson 都有
 */
public class PropertyCodecConfiguration extends CodecConfiguration {

    /**
     * 字段名，如果是方法，则为去掉了 get,set,is之后的
     */
    @NotEmpty
    private String name;

    /**
     * 别名，若指定了别名，则序列化时采用 别名，否则用 name
     */
    @Nullable
    private String alias;

    /**
     * 反序列化时，可以使用这些备用名
     */
    @Nullable
    private Set<String> alternateNames;


    private PropertyConfigurationSourceType sourceType;

    /**
     * 当 isClassProperty 时, classRef 才有意义
     */
    private WeakReference<Class> clazzRef;

    public static PropertyCodecConfiguration getPropertyCodecConfiguration(@NonNull DialectIdentify dialectIdentify, @NonNull Object container, @NonNull String propertyName) {
        if (container == null) {
            return null;
        }
        return getPropertyCodecConfiguration(dialectIdentify, container.getClass(), propertyName);
    }

    public static PropertyCodecConfiguration getPropertyCodecConfiguration(@NonNull DialectIdentify dialectIdentify, @NonNull Class containerClass, @NonNull String propertyName) {
        if (dialectIdentify == null) {
            return null;
        }
        if (containerClass == null) {
            return null;
        }
        if (Emptys.isEmpty(propertyName)) {
            return null;
        }

        String packageName = Reflects.getPackageName(containerClass);
        if (packageName.startsWith("java.")) {
            return null;
        }

        if (Reflects.isSubClassOrEquals(Map.class, containerClass) || Reflects.isSubClassOrEquals(Collection.class, containerClass) || containerClass.isArray()) {
            return null;
        }
        CodecConfigurationRepository codecConfigurationRepository = CodecConfigurationRepositoryService.getInstance().getCodecConfigurationRepository(dialectIdentify);
        if (codecConfigurationRepository == null) {
            return null;
        }
        return codecConfigurationRepository.getPropertyCodeConfiguration(containerClass, propertyName);
    }

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

    public Class getClazz() {
        return clazzRef == null ? null : clazzRef.get();
    }

    public void setClazz(Class clazz) {
        this.clazzRef = new WeakReference<Class>(clazz);
    }

    public PropertyConfigurationSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(PropertyConfigurationSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public Set<String> getAlternateNames() {
        return alternateNames;
    }

    public void setAlternateNames(List<String> alternateNames) {
        this.alternateNames = Collects.asSet(alternateNames);
    }

    public void setAlternateNames(String[] alternateNames) {
        this.alternateNames = Collects.asSet(alternateNames);
    }

    public void addAlternateNames(Collection<String> alternateNames) {
        Set<String> ret = new HashSet<String>();
        Collects.addAll(ret, this.alternateNames);
        Collects.addAll(ret, alternateNames);
        this.alternateNames = ret;
    }

    public void addAlternateNames(String[] alternateNames) {
        Set<String> ret = new HashSet<String>();
        Collects.addAll(ret, this.alternateNames);
        Collects.addAll(ret, alternateNames);
        this.alternateNames = ret;
    }

    public boolean hasAlias() {
        return Strings.isNotBlank(alias);
    }

    public boolean hasAlternateName(String alternate) {
        if (Strings.isBlank(alternate)) {
            return false;
        }
        if (this.name.equals(alternate)) {
            return true;
        }
        if (hasAlias()) {
            if (this.alias.equals(alternate)) {
                return true;
            }
        }
        return Collects.contains(this.alternateNames, alternate);
    }
}
