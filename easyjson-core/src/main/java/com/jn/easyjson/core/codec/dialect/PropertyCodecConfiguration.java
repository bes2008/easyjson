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

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.reflect.Reflects;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Map;

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


    private PropertyConfigurationSourceType sourceType;

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

    public Class getClazz() {
        return clazzRef==null? null: clazzRef.get();
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


    public static PropertyCodecConfiguration getPropertyCodecConfiguration(@NonNull DialectIdentify dialectIdentify, @NonNull Object container, @NonNull String propertyName){
        if(dialectIdentify==null){
            return null;
        }
        if(container==null){
            return null;
        }
        if(Emptys.isEmpty(propertyName)){
            return null;
        }

        Class containerClass = container.getClass();
        String packageName=  Reflects.getPackageName(containerClass);
        if(packageName.startsWith("java.")){
            return null;
        }
        if(container instanceof Map || container instanceof Collection || containerClass.isArray()){
            return null;
        }
        CodecConfigurationRepository codecConfigurationRepository = CodecConfigurationRepositoryService.getInstance().getCodecConfigurationRepository(dialectIdentify);
        if(codecConfigurationRepository==null){
            return null;
        }
        return codecConfigurationRepository.getPropertyCodeConfiguration(containerClass, propertyName);
    }
}
