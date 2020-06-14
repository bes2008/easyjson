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
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.codec.dialect.JsonLibraryIdentify;
import com.jn.easyjson.core.exclusion.FieldNamesExclusion;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.Primitives;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FastEasyJsons {
    public static final JsonLibraryIdentify FASTJSON;

    static {
        FASTJSON = new JsonLibraryIdentify("fastjson", Reflects.getCodeLocation(FastEasyJsons.class).toString());
    }

    private FastEasyJsons() {
    }

    public static JSONBuilder getJsonBuilder(int features, SerializerFeature... features2) {
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        if (features2 != null) {
            for (SerializerFeature feature : features2) {
                features |= feature.getMask();
            }
        }
        boolean serializeNulls = (SerializerFeature.WriteMapNullValue.getMask() & features) != 0;
        jsonBuilder.serializeNulls(serializeNulls);
        boolean serializeEnumUsingToString = (SerializerFeature.WriteEnumUsingToString.getMask() & features) != 0;
        jsonBuilder.serializeEnumUsingToString(serializeEnumUsingToString);
        boolean serializeEnumUsingName = (SerializerFeature.WriteEnumUsingName.getMask() & features) != 0;
        jsonBuilder.serializeEnumUsingValue(!serializeEnumUsingName);
        boolean skipTransientField = (SerializerFeature.SkipTransientField.getMask() & features) != 0;
        if (skipTransientField) {
            jsonBuilder.excludeFieldsWithAppendModifiers(Modifier.TRANSIENT);
        }
        boolean prettyFormat = (SerializerFeature.PrettyFormat.getMask() & features) != 0;
        jsonBuilder.prettyFormat(prettyFormat);
        jsonBuilder.useGlobalConfiguration(true);
        return jsonBuilder;
    }

    public static JSONBuilder parseTargetClass(@NonNull JSONBuilder jsonBuilder, @NonNull Class targetClass) {
        Preconditions.checkNotNull(targetClass);
        if (Primitives.isPrimitive(targetClass) || Primitives.isWrapperType(targetClass)) {
            return jsonBuilder;
        }

        if (Reflects.isSubClassOrEquals(Map.class, targetClass) || Reflects.isSubClassOrEquals(Collection.class, targetClass)) {
            String packageName = Reflects.getPackageName(targetClass);
            if (Emptys.isNotEmpty(packageName)) {
                if (packageName.startsWith("java.")) {
                    return jsonBuilder;
                }
            }
        }

        parseJSONType(jsonBuilder, targetClass);
        jsonBuilder.addExclusionStrategies(new FastjsonAnnotationExclusion());
        return jsonBuilder;
    }

    private static JSONBuilder parseJSONType(JSONBuilder jsonBuilder, Class targetClass) {
        JSONType jsonType = Reflects.getAnnotation(targetClass, JSONType.class);
        if (jsonType == null) {
            return jsonBuilder;
        }

        // serial features
        List<SerializerFeature> serializerFeatures = Collects.asList(jsonType.serialzeFeatures());
        if (serializerFeatures.contains(SerializerFeature.WriteEnumUsingToString)) {
            jsonBuilder.serializeEnumUsingToString(true);
        }
        if (serializerFeatures.contains(SerializerFeature.WriteEnumUsingName)) {
            jsonBuilder.serializeEnumUsingValue(false);
        }
        if (serializerFeatures.contains(SerializerFeature.PrettyFormat)) {
            jsonBuilder.prettyFormat(true);
        }

        // ignores
        String[] ignores = jsonType.ignores();
        if (Emptys.isNotEmpty(ignores)) {
            jsonBuilder.addExclusionStrategies(new FieldNamesExclusion(Collects.asList(ignores)));
        }

        return jsonBuilder;
    }

    private static JSONBuilder parseJSONField(JSONBuilder jsonBuilder, Class targetClass) {
        JSONField jsonField = Reflects.getAnnotation(targetClass, JSONField.class);
        if (jsonField == null) {
            return jsonBuilder;
        }
        return jsonBuilder;
    }


}
