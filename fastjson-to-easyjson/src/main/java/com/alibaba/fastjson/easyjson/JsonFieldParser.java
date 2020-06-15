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

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jn.easyjson.core.codec.dialect.BeanPropertyAnnotatedCodecConfigurationParser;
import com.jn.easyjson.core.codec.dialect.BeanPropertyIdGenerator;
import com.jn.easyjson.core.codec.dialect.PropertyCodecConfiguration;
import com.jn.easyjson.core.codec.dialect.PropertyConfigurationSourceType;
import com.jn.easyjson.core.util.Members;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;

public class JsonFieldParser implements BeanPropertyAnnotatedCodecConfigurationParser {
    private BeanPropertyIdGenerator idGenerator = new BeanPropertyIdGenerator();
    @Override
    public PropertyCodecConfiguration parse(AnnotatedElement annotatedElement) {
        if (annotatedElement instanceof Field || annotatedElement instanceof Method) {
            Member member = (Member) annotatedElement;
            Class beanClass = member.getDeclaringClass();

            JSONField jsonField = Reflects.getAnnotation(annotatedElement, JSONField.class);
            if (jsonField == null) {
                return null;
            }

            if(member instanceof Method){
                if(!Members.isGetterOrSetter((Method)member)){
                    return null;
                }
            }

            PropertyCodecConfiguration configuration = new PropertyCodecConfiguration();
            configuration.setClazz(beanClass);
            configuration.setName(Members.extractFieldName(member));

            if(annotatedElement instanceof Field) {
                configuration.setSourceType(PropertyConfigurationSourceType.FIELD);
            }
            else{
                if(member.getName().startsWith("set")){
                    configuration.setSourceType(PropertyConfigurationSourceType.SETTER);
                }else {
                    configuration.setSourceType(PropertyConfigurationSourceType.GETTER);
                }
            }

            String propertyName = annotatedElement instanceof Field ? ((Field)annotatedElement).getName() : Members.extractFieldName((Method) member);

            configuration.setId(idGenerator.withBeanClass(beanClass).withPropertyName(propertyName).get());

            configuration.setAlias(jsonField.name());
            configuration.setSerialize(jsonField.serialize());
            configuration.setDeserialize(jsonField.deserialize());
            configuration.setDatePattern(jsonField.format());

            // serializeFeatures
            List<SerializerFeature> serializeFeatures = Collects.asList(jsonField.serialzeFeatures());
            configuration.set("serializeFeatures", serializeFeatures);
            if (serializeFeatures.contains(SerializerFeature.WriteEnumUsingToString)) {
                configuration.setEnumUsingToString(true);
            }
            if (serializeFeatures.contains(SerializerFeature.WriteEnumUsingName)) {
                configuration.setEnumUsingValue(false);
            }

            // deserializeFeatures
            List<Feature> parseFeatures = Collects.asList(jsonField.parseFeatures());
            configuration.set("deserializeFeatures", parseFeatures);

            return configuration;
        }
        return null;
    }
}
