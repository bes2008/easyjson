/*
 * Copyright 2019 the original author or authors.
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

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.FieldInfo;

public class SerializeBeanInfo {

    protected final Class<?> beanType;
    protected final String typeName;
    protected final String typeKey;
    protected final JSONType jsonType;

    protected final FieldInfo[] fields;
    protected final FieldInfo[] sortedFields;

    protected int features;

    public SerializeBeanInfo(Class<?> beanType, //
                             JSONType jsonType, //
                             String typeName, //
                             String typeKey,
                             int features,
                             FieldInfo[] fields, //
                             FieldInfo[] sortedFields
    ) {
        this.beanType = beanType;
        this.jsonType = jsonType;
        this.typeName = typeName;
        this.typeKey = typeKey;
        this.features = features;
        this.fields = fields;
        this.sortedFields = sortedFields;
    }

}
