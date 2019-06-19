/*
 * Copyright 2019 the original author or authors.
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

package com.github.fangjinuo.easyjson.fastjson.ext;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.github.fangjinuo.easyjson.core.exclusion.ExclusionConfiguration;
import com.github.fangjinuo.easyjson.fastjson.FastJsonJSONBuilder;

import java.util.ArrayList;
import java.util.List;

public class EasyJsonSerializeConfig extends SerializeConfig {
    private FastJsonJSONBuilder jsonJSONBuilder;
    public EasyJsonSerializeConfig(FastJsonJSONBuilder jsonJSONBuilder) {
        super();
        this.jsonJSONBuilder = jsonJSONBuilder;
    }

    @Override
    public ObjectSerializer createJavaBeanSerializer(SerializeBeanInfo beanInfo) {
        //=====================EasyJson exclusion start==========================
        SerializeBeanInfoGetter getter = new SerializeBeanInfoGetter(beanInfo);
        Class<?> beanType = getter.getBeanType();
        ExclusionConfiguration exclusionConfiguration = jsonJSONBuilder.getExclusionConfiguration();
        if(!exclusionConfiguration.isExcludedClass(beanType, true)){
            FieldInfo[] fields = getter.getFields();
            FieldInfo[] sortedFields = getter.getSortedFields();

            List<FieldInfo> fieldInfoes = new ArrayList<FieldInfo>();
            List<FieldInfo> sortedFieldInfoes = new ArrayList<FieldInfo>();

            for (FieldInfo fieldInfo : fields) {
                if(exclusionConfiguration.isExcludedField(fieldInfo.field, true)) {
                    continue;
                }
                fieldInfoes.add(fieldInfo);
            }

            for (FieldInfo fieldInfo : sortedFields) {
                if(exclusionConfiguration.isExcludedField(fieldInfo.field, true)) {
                    continue;
                }
                sortedFieldInfoes.add(fieldInfo);
            }

            if (fieldInfoes.size() != fields.length) {
                // has ignored field
                String typeName = getter.getTypeName();
                String typeKey = getter.getTypeKey();
                JSONType jsonType = getter.getJsonType();
                int features = getter.getFeatures();
                SerializeBeanInfo newBeanInfo = new SerializeBeanInfo(beanType,
                        jsonType,
                        typeName,
                        typeKey,
                        features,
                        fieldInfoes.toArray(new FieldInfo[fieldInfoes.size()]),
                        sortedFieldInfoes.toArray(new FieldInfo[sortedFieldInfoes.size()]));
                return super.createJavaBeanSerializer(newBeanInfo);
            }
        }else{
            String typeName = getter.getTypeName();
            String typeKey = getter.getTypeKey();
            JSONType jsonType = getter.getJsonType();
            int features = getter.getFeatures();
            SerializeBeanInfo newBeanInfo = new SerializeBeanInfo(beanType,
                    jsonType,
                    typeName,
                    typeKey,
                    features,
                    new FieldInfo[0],
                    new FieldInfo[0]);
            return super.createJavaBeanSerializer(newBeanInfo);
        }

        //=====================EasyJson exclusion end==========================

        return super.createJavaBeanSerializer(beanInfo);
    }


}
