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

package com.alibaba.fastjson.easyjson;

import com.jn.easyjson.core.codec.dialect.BeanClassAnnotatedCodecConfigurationParser;
import com.jn.easyjson.core.codec.dialect.ClassCodecConfiguration;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.AnnotatedElement;

public class JsonTypeParser implements BeanClassAnnotatedCodecConfigurationParser {
    @Override
    public ClassCodecConfiguration parse(AnnotatedElement annotatedElement) {
        if(annotatedElement instanceof Class){
            Class beanClass = (Class)annotatedElement;
            ClassCodecConfiguration configuration = new ClassCodecConfiguration();
            configuration.setClazz(beanClass);
            configuration.setId(Reflects.getFQNClassName(beanClass));

            return configuration;
        }
        return null;
    }
}
