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
import com.jn.langx.configuration.ConfigurationLoader;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class ClassLoaderCodecConfigurationLoader<T extends CodecConfiguration> implements ConfigurationLoader<T> {
    private static final Logger logger = LoggerFactory.getLogger(ClassLoaderCodecConfigurationLoader.class);

    private ConcurrentHashMap<String, Boolean> loadFlagCache = new ConcurrentHashMap<String, Boolean>();
    /**
     * 类加载器，用于根据 id 去加载类
     */
    @NonNull
    private WeakReference<ClassLoader> classLoaderRef;

    /**
     * Id 解析器，用于根据ID解析出类名
     */
    @NonNull
    private BeanPropertyIdParser beanPropertyIdParser = new BeanPropertyIdParser();


    /**
     * 类字段、方法解析器
     */
    @NonNull
    private BeanPropertyAnnotatedCodecConfigurationParser beanPropertyCodecConfigurationParser;

    /**
     * 用于解析类
     */
    @NonNull
    private BeanClassAnnotatedCodecConfigurationParser beanClassAnnotatedCodecConfigurationParser;

    private PropertyCodecConfigurationMerger propertyCodecConfigurationMerger;


    public ClassLoaderCodecConfigurationLoader() {
    }

    public ClassLoaderCodecConfigurationLoader(BeanClassAnnotatedCodecConfigurationParser beanClassAnnotatedCodecConfigurationParser, BeanPropertyAnnotatedCodecConfigurationParser parser) {
        this.beanClassAnnotatedCodecConfigurationParser = beanClassAnnotatedCodecConfigurationParser;
        this.beanPropertyCodecConfigurationParser = parser;
    }

    public ClassLoader getClassLoader() {
        return classLoaderRef.get();
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoaderRef = new WeakReference<ClassLoader>(classLoader);
    }

    public void setBeanPropertyCodecConfigurationParser(BeanPropertyAnnotatedCodecConfigurationParser beanPropertyCodecConfigurationParser) {
        this.beanPropertyCodecConfigurationParser = beanPropertyCodecConfigurationParser;
    }

    public void setBeanClassAnnotatedCodecConfigurationParser(BeanClassAnnotatedCodecConfigurationParser beanClassAnnotatedCodecConfigurationParser) {
        this.beanClassAnnotatedCodecConfigurationParser = beanClassAnnotatedCodecConfigurationParser;
    }

    public PropertyCodecConfigurationMerger getPropertyCodecConfigurationMerger() {
        return propertyCodecConfigurationMerger;
    }

    public void setPropertyCodecConfigurationMerger(PropertyCodecConfigurationMerger propertyCodecConfigurationMerger) {
        this.propertyCodecConfigurationMerger = propertyCodecConfigurationMerger;
    }

    private ClassLoader getUsableClassLoader() {
        ClassLoader cl = null;
        if (classLoaderRef == null || classLoaderRef.get() == null || classLoaderRef.get() == FakeBootstrapClassLoader.getInstance()) {
            cl = Thread.currentThread().getContextClassLoader();
        } else {
            cl = classLoaderRef.get();
        }
        if (cl == null) {
            cl = getClass().getClassLoader();
        }
        return cl;
    }

    @Override
    public T load(String qualifiedId) {
        Boolean loaded = loadFlagCache.get(qualifiedId);
        if (loaded != null) {
            // 已经解析过了
            return null;
        }
        BeanPropertyId id = beanPropertyIdParser.parse(qualifiedId);
        try {
            Class clazz = null;
            try {
                clazz = getUsableClassLoader().loadClass(id.getBeanClass());
                // 如果没有属性名，就是解析类
                if (Emptys.isEmpty(id.getPropertyName())) {
                    ClassCodecConfiguration classCodecConfiguration = null;
                    classCodecConfiguration = beanClassAnnotatedCodecConfigurationParser.parse(clazz);
                    return (T) classCodecConfiguration;
                }

            } catch (ClassNotFoundException ex) {
                logger.error("Can't find class: {}", id.getBeanClass());
                return null;
            }
            if (clazz == null) {
                logger.error("Can't find class: {}", id.getBeanClass());
                return null;
            }

            boolean useMerged = false;
            PropertyCodecConfiguration codecConfiguration = new PropertyCodecConfiguration();
            codecConfiguration.setClazz(clazz);
            codecConfiguration.setId(qualifiedId);

            PropertyCodecConfiguration fieldConfiguration = null;
            Field field = Reflects.getAnyField(clazz, id.getPropertyName());
            if (field != null) {
                fieldConfiguration = beanPropertyCodecConfigurationParser.parse(field);
                if(fieldConfiguration!=null){
                    propertyCodecConfigurationMerger.merge(codecConfiguration, fieldConfiguration);
                    useMerged = true;
                }
            }
            // getXxx
            PropertyCodecConfiguration getterConfiguration = null;
            if (getterConfiguration == null) {
                String getter = Reflects.getGetter(id.getPropertyName());
                Method getterMethod = Reflects.getAnyMethod(clazz, getter);
                if (getterMethod != null) {
                    getterConfiguration = beanPropertyCodecConfigurationParser.parse(getterMethod);
                    if(getterConfiguration!=null){
                        propertyCodecConfigurationMerger.merge(codecConfiguration, getterConfiguration);
                        useMerged = true;
                    }
                }
            }
            // isXxx
            if (getterConfiguration == null) {
                String getter = Reflects.getIsGetter(id.getPropertyName());
                Method getterMethod = Reflects.getAnyMethod(clazz, getter);
                if (getterMethod != null) {
                    getterConfiguration = beanPropertyCodecConfigurationParser.parse(getterMethod);
                    if(getterConfiguration!=null){
                        propertyCodecConfigurationMerger.merge(codecConfiguration, getterConfiguration);
                        useMerged = true;
                    }
                }
            }
            // setXxx
            PropertyCodecConfiguration setterConfiguration = null;
            if (setterConfiguration == null) {
                String setter = Reflects.getSetter(id.getPropertyName());
                Class propertyType = null;
                if (field != null) {
                    propertyType = field.getType();
                }
                Method setterMethod = propertyType == null ? Reflects.getAnyMethod(clazz, setter) : Reflects.getAnyMethod(clazz, setter, propertyType);
                if (setterMethod != null) {
                    setterConfiguration = beanPropertyCodecConfigurationParser.parse(setterMethod);
                    if(setterConfiguration!=null){
                        propertyCodecConfigurationMerger.merge(codecConfiguration, setterConfiguration);
                        useMerged = true;
                    }
                }
            }
            return useMerged ? (T)codecConfiguration : null;
        } catch (Throwable ex) {
            // ignore

        } finally {
            loadFlagCache.putIfAbsent(qualifiedId, true);
        }
        return null;
    }

}
