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
import com.github.fangjinuo.easyjson.core.annotation.Ignore;
import com.github.fangjinuo.easyjson.core.util.FieldAttributes;

import java.util.ArrayList;
import java.util.List;

public class EasyFastJsonSerializeConfig extends SerializeConfig {
    public EasyFastJsonSerializeConfig() {
        super();
    }

    @Override
    public ObjectSerializer createJavaBeanSerializer(SerializeBeanInfo beanInfo) {
        SerializeBeanInfoGetter getter = new SerializeBeanInfoGetter(beanInfo);
        FieldInfo[] fields = getter.getFields();
        FieldInfo[] sortedFields = getter.getSortedFields();

        List<FieldInfo> fieldInfoes = new ArrayList<FieldInfo>();
        List<FieldInfo> sortedFieldInfoes = new ArrayList<FieldInfo>();

        for (FieldInfo fieldInfo : fields) {
            Ignore ignore = new FieldAttributes(fieldInfo.field).getAnnotation(Ignore.class);
            if (ignore != null && ignore.write()) {
                continue;
            }
            fieldInfoes.add(fieldInfo);
        }

        for (FieldInfo fieldInfo : sortedFields) {
            Ignore ignore = new FieldAttributes(fieldInfo.field).getAnnotation(Ignore.class);
            if (ignore != null && ignore.write()) {
                continue;
            }
            sortedFieldInfoes.add(fieldInfo);
        }

        if (fieldInfoes.size() != fields.length) {
            // has ignored field
            Class<?> beanType = getter.getBeanType();
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
        return super.createJavaBeanSerializer(beanInfo);
    }


}
