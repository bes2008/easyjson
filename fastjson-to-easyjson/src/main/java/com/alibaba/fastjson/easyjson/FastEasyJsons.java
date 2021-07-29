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

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.codec.dialect.DialectIdentify;
import com.jn.easyjson.core.factory.JsonFactoryProperties;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.Modifier;

public class FastEasyJsons {
    public static final String JSON_IDENTIFY_STRING = "fastjson";
    public static final DialectIdentify FASTJSON;

    static {
        FASTJSON = new DialectIdentify(JSON_IDENTIFY_STRING, Reflects.getCodeLocation(FastEasyJsons.class).toString());
    }

    private FastEasyJsons() {
    }

    public static JSONBuilder getJsonBuilder(int features, SerializerFeature... features2) {
        JSONBuilder jsonBuilder = JSONBuilderProvider.adapter(JSON_IDENTIFY_STRING);
        if (features2 != null) {
            for (SerializerFeature feature : features2) {
                features |= feature.getMask();
            }
        }
        boolean serializeNulls = (SerializerFeature.WriteMapNullValue.getMask() & features) != 0;
        boolean serializeEnumUsingToString = (SerializerFeature.WriteEnumUsingToString.getMask() & features) != 0;
        boolean serializeEnumUsingName = (SerializerFeature.WriteEnumUsingName.getMask() & features) != 0;
        boolean skipTransientField = (SerializerFeature.SkipTransientField.getMask() & features) != 0;
        boolean prettyFormat = (SerializerFeature.PrettyFormat.getMask() & features) != 0;


        JsonFactoryProperties properties = new JsonFactoryProperties();
        properties.setSerializeNulls(serializeNulls);
        properties.setPrettyFormat(prettyFormat);
        properties.setSerializeEnumUsingToString(serializeEnumUsingToString);
        properties.setSerializeEnumUsingIndex(!serializeEnumUsingName);
        if (skipTransientField) {
            properties.addExclusiveFieldModifier(Modifier.TRANSIENT);
        }
        properties.setEnableCustomConfiguration(true);
        properties.setProxyDialectIdentify(FASTJSON);
        properties.addExclusion(FastjsonAnnotationExclusion.INSTANCE);

        return jsonBuilder;
    }

}
