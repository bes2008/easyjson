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

import com.jn.langx.Builder;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.cache.Cache;
import com.jn.langx.cache.CacheBuilder;
import com.jn.langx.configuration.ConfigurationCacheLoaderAdapter;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.concurrent.CommonThreadFactory;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.WheelTimers;
import org.slf4j.Logger;

import java.lang.ref.WeakReference;

public class ClassLoaderCodecConfigurationRepositoryBuilder<T extends CodecConfiguration> implements Builder<ClassLoaderCodecConfigurationRepository<T>> {
    public static final Timer timer = WheelTimers.newHashedWheelTimer(new CommonThreadFactory("EasyJSON", true));
    private static final Logger logger = Loggers.getLogger(ClassLoaderCodecConfigurationRepositoryBuilder.class);
    /**
     * 类字段、方法解析器
     */
    @NonNull
    private BeanPropertyAnnotatedCodecConfigurationParser beanPropertyCodecConfigurationParser;
    @Nullable
    private BeanPropertyFinder beanPropertyFinder;
    /**
     * 用于解析类
     */
    @NonNull
    private BeanClassAnnotatedCodecConfigurationParser beanClassAnnotatedCodecConfigurationParser;
    private WeakReference<ClassLoader> classLoaderRef;
    private PropertyCodecConfigurationMerger propertyCodecConfigurationMerger;


    public ClassLoaderCodecConfigurationRepositoryBuilder<T> beanPropertyCodecConfigurationParser(BeanPropertyAnnotatedCodecConfigurationParser beanPropertyCodecConfigurationParser) {
        this.beanPropertyCodecConfigurationParser = beanPropertyCodecConfigurationParser;
        return this;
    }

    public BeanPropertyAnnotatedCodecConfigurationParser beanPropertyCodecConfigurationParser() {
        return this.beanPropertyCodecConfigurationParser;
    }

    public ClassLoaderCodecConfigurationRepositoryBuilder<T> beanClassAnnotatedCodecConfigurationParser(BeanClassAnnotatedCodecConfigurationParser beanClassAnnotatedCodecConfigurationParser) {
        this.beanClassAnnotatedCodecConfigurationParser = beanClassAnnotatedCodecConfigurationParser;
        return this;
    }

    public BeanClassAnnotatedCodecConfigurationParser beanClassAnnotatedCodecConfigurationParser() {
        return this.beanClassAnnotatedCodecConfigurationParser;
    }
    public PropertyCodecConfigurationMerger propertyCodecConfigurationMerger() {
        return this.propertyCodecConfigurationMerger;
    }

    public ClassLoaderCodecConfigurationRepositoryBuilder<T> propertyCodecConfigurationMerger(PropertyCodecConfigurationMerger merger) {
        this.propertyCodecConfigurationMerger = merger;
        return this;
    }

    public ClassLoaderCodecConfigurationRepositoryBuilder<T> classLoader(ClassLoader classLoader) {
        this.classLoaderRef = new WeakReference<ClassLoader>(classLoader);
        return this;
    }

    public BeanPropertyFinder getBeanPropertyFinder() {
        return beanPropertyFinder;
    }

    public ClassLoaderCodecConfigurationRepositoryBuilder<T> beanPropertyFinder(BeanPropertyFinder beanPropertyFinder) {
        this.beanPropertyFinder = beanPropertyFinder;
        return this;
    }

    public ClassLoader classLoader() {
        if (this.classLoaderRef != null) {
            return this.classLoaderRef.get();
        }
        return null;
    }


    @Override
    public ClassLoaderCodecConfigurationRepository<T> build() {
        Preconditions.checkNotNull(propertyCodecConfigurationMerger);
        logger.info("build a ClassLoaderCodecConfigurationRepository instance for ");
        // loader
        ClassLoaderCodecConfigurationLoader<T> loaderCodecConfigurationLoader = new ClassLoaderCodecConfigurationLoader<T>();
        loaderCodecConfigurationLoader.setBeanClassAnnotatedCodecConfigurationParser(beanClassAnnotatedCodecConfigurationParser);
        loaderCodecConfigurationLoader.setBeanPropertyCodecConfigurationParser(beanPropertyCodecConfigurationParser);
        loaderCodecConfigurationLoader.setPropertyCodecConfigurationMerger(propertyCodecConfigurationMerger);
        loaderCodecConfigurationLoader.setPropertyFinder(beanPropertyFinder);
        loaderCodecConfigurationLoader.setClassLoader(classLoader());

        // cache
        Cache<String, T> cache = CacheBuilder.<String, T>newBuilder()
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .loader(new ConfigurationCacheLoaderAdapter<T>(loaderCodecConfigurationLoader))
                .initialCapacity(1000)
                .maxCapacity(Integer.MAX_VALUE)
                .timer(timer)
                .build();

        // repository
        ClassLoaderCodecConfigurationRepository<T> repository = new ClassLoaderCodecConfigurationRepository<T>();
        repository.setConfigurationLoader(loaderCodecConfigurationLoader);
        repository.setName(classLoader().toString());
        repository.setCache(cache);
        repository.setTimer(timer);
        return repository;
    }
}
