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

package com.alibaba.fastjson.easyjson;

import com.jn.easyjson.core.codec.dialect.ClassCodecConfiguration;
import com.jn.easyjson.core.codec.dialect.CodecConfigurationRepository;
import com.jn.easyjson.core.codec.dialect.CodecConfigurationRepositoryService;
import com.jn.easyjson.core.codec.dialect.PropertyCodecConfiguration;
import com.jn.easyjson.core.exclusion.Exclusion;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.reflect.FieldAttributes;
import com.jn.langx.util.reflect.MethodAttributes;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * 处理 fastjson  JSONType, JSONField 注解
 */
public final class FastjsonAnnotationExclusion implements Exclusion {
    public static final FastjsonAnnotationExclusion INSTANCE = new FastjsonAnnotationExclusion();
    @Override
    public boolean shouldSkipMethod(MethodAttributes m, boolean serializePhrase) {
        return skipProperty(m.get(), serializePhrase);
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f, boolean serializePhrase) {
        return skipProperty(f.get(), serializePhrase);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz, boolean serializePhrase) {
        return false;
    }

    private boolean skipProperty(Member member, boolean serializePhrase) {
        if (member instanceof Field || member instanceof Method) {
            Class beanClass = member.getDeclaringClass();
            String name = Reflects.extractFieldName(member);
            return skipProperty(beanClass, name, serializePhrase);
        }
        return true;
    }

    private boolean skipProperty(Class beanClass, String propertyName, boolean serializePhrase) {
        CodecConfigurationRepository configurationRepository = CodecConfigurationRepositoryService.getInstance().getCodecConfigurationRepository(FastEasyJsons.FASTJSON);
        if(configurationRepository==null){
            return false;
        }
        PropertyCodecConfiguration propertyCodeConfiguration = configurationRepository.getPropertyCodeConfiguration(beanClass, propertyName);
        if (propertyCodeConfiguration == null) {
            ClassCodecConfiguration classCodecConfiguration = configurationRepository.getClassCodecConfiguration(beanClass);
            if(classCodecConfiguration!=null){
                if(Emptys.isNotEmpty(classCodecConfiguration.getIncludedPropertyNames())){
                    return !classCodecConfiguration.getIncludedPropertyNames().contains(propertyName);
                }
                if(Emptys.isNotEmpty(classCodecConfiguration.getExcludedPropertyNames())){
                    return classCodecConfiguration.getExcludedPropertyNames().contains(propertyName);
                }
            }
            return false;
        }

        if (serializePhrase) {
            return !(propertyCodeConfiguration.getSerialize() != null && propertyCodeConfiguration.getSerialize());
        } else {
            return !(propertyCodeConfiguration.getDeserialize() != null && propertyCodeConfiguration.getDeserialize());
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FastjsonAnnotationExclusion;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
