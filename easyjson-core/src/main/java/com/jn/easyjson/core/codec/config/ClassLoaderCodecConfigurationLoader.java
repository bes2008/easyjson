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

import com.jn.langx.configuration.ConfigurationLoader;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class ClassLoaderCodecConfigurationLoader<T extends CodecConfiguration> implements ConfigurationLoader<T> {
    private static final Logger logger = LoggerFactory.getLogger(ClassLoaderCodecConfigurationLoader.class);

    private BeanPropertyIdParser beanPropertyIdParser = new BeanPropertyIdParser();
    private ClassLoader classLoader;
    private AnnotatedElementCodecConfigurationParser parser;

    public ClassLoaderCodecConfigurationLoader(AnnotatedElementCodecConfigurationParser parser) {
        this.parser = parser;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public T load(String qualifiedId) {
        BeanPropertyId id = beanPropertyIdParser.parse(qualifiedId);
        Class clazz = null;
        try {
            clazz = classLoader.loadClass(id.getBeanClass());
        } catch (ClassNotFoundException ex) {
            logger.error("Can't find class: {}", id.getBeanClass());
            return null;
        }
        if(clazz==null){
            logger.error("Can't find class: {}", id.getBeanClass());
            return null;
        }
        Field field = Reflects.getAnyField(clazz, id.getPropertyName());
        if(field!=null){
            parser.parse(field);
        }
        return null;
    }

}
