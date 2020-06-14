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

import com.jn.langx.configuration.AbstractConfigurationRepository;
import com.jn.langx.configuration.ConfigurationWriter;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.ConcurrentReferenceHashMap;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.reference.ReferenceType;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CodecConfigurationRepository<T extends CodecConfiguration> extends AbstractConfigurationRepository<T, ClassLoaderCodecConfigurationLoader<T>, ConfigurationWriter<T>> {
    private ConcurrentReferenceHashMap<ClassLoader, ClassLoaderCodecConfigurationRepository<T>> repositories = new ConcurrentReferenceHashMap<ClassLoader, ClassLoaderCodecConfigurationRepository<T>>(1000, 0.95f, Runtime.getRuntime().availableProcessors(), ReferenceType.WEAK, ReferenceType.WEAK);
    private BeanClassAnnotatedCodecConfigurationParser defaultBeanClassParser;
    private BeanPropertyAnnotatedCodecConfigurationParser defaultBeanPropertyParser;

    @Override
    public void setReloadIntervalInSeconds(int reloadIntervalInSeconds) {
        super.setReloadIntervalInSeconds(-1);
        Collects.forEach(repositories, new Consumer2<ClassLoader, AbstractConfigurationRepository>() {
            @Override
            public void accept(ClassLoader cl, AbstractConfigurationRepository repository) {
                repository.setReloadIntervalInSeconds(-1);
            }
        });
    }

    public ClassCodecConfiguration getClassCodecConfiguration(Class clazz){
        ClassLoaderCodecConfigurationRepository<T> repository = findRepository(clazz);
        return (ClassCodecConfiguration)repository.getById(Reflects.getFQNClassName(clazz));
    }

    public PropertyCodecConfiguration getPropertyCodeConfiguration(Class clazz, String propertyName) {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkNotNull(propertyName);
        ClassLoaderCodecConfigurationRepository<T> repository = findRepository(clazz);
        return (PropertyCodecConfiguration)repository.getById(new BeanPropertyIdGenerator().withBeanClass(clazz).withPropertyName(propertyName).get());
    }

    private ClassLoaderCodecConfigurationRepository<T> findRepository(AnnotatedElement annotatedElement) {
        Class clazz = null;
        if (annotatedElement instanceof Class) {
            clazz = ((Class) annotatedElement);
        } else if (annotatedElement instanceof Field) {
            clazz = ((Field) annotatedElement).getDeclaringClass();
        } else if (annotatedElement instanceof Method) {
            clazz = ((Method) annotatedElement).getDeclaringClass();
        } else if (annotatedElement instanceof Constructor) {
            clazz = ((Constructor) annotatedElement).getDeclaringClass();
        }
        Preconditions.checkNotNull(clazz);
        ClassLoader classLoader = clazz.getClassLoader();
        if (classLoader == null) {
            classLoader = FakeBootstrapClassLoader.getInstance();
        }
        ClassLoaderCodecConfigurationRepository<T> repository = repositories.get(classLoader);
        if (repository == null) {
            repository = new ClassLoaderCodeConfigurationRepositoryBuilder<T>()
                   .beanClassAnnotatedCodecConfigurationParser(defaultBeanClassParser)
                   .beanPropertyCodecConfigurationParser(defaultBeanPropertyParser)
                   .classLoader(classLoader)
                   .build();
            repositories.putIfAbsent(classLoader, repository);
        }
        return repository;
    }
}
