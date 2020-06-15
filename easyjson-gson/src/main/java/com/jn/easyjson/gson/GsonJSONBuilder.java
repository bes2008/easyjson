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

package com.jn.easyjson.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.annotation.DependOn;
import com.jn.easyjson.core.exclusion.Exclusion;
import com.jn.easyjson.core.exclusion.ExclusionConfiguration;
import com.jn.easyjson.gson.exclusion.DelegateExclusionStrategy;
import com.jn.easyjson.gson.typeadapter.*;
import com.jn.langx.annotation.Name;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Name("gson")
@DependOn("com.google.gson.Gson")
public class GsonJSONBuilder extends JSONBuilder {
    private static final Logger logger = LoggerFactory.getLogger(GsonJSONBuilder.class);

    public GsonJSONBuilder() {
        super();
    }

    public GsonJSONBuilder(ExclusionConfiguration exclusionConfiguration) {
        super(exclusionConfiguration);
    }

    @Override
    public JSON build() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Null
        if (serializeNulls()) {
            gsonBuilder.serializeNulls();
        }

        // Boolean
        BooleanTypeAdapter booleanTypeAdapter = new BooleanTypeAdapter();
        booleanTypeAdapter.setUsing1_0(serializeBooleanUsing1_0());
        booleanTypeAdapter.setUsingOnOff(serializeBooleanUsingOnOff());
        gsonBuilder.registerTypeHierarchyAdapter(Boolean.class, booleanTypeAdapter);

        // Number
        NumberTypeAdapter numberTypeAdapter = new NumberTypeAdapter();
        numberTypeAdapter.setLongUsingString(serializeLongAsString());
        numberTypeAdapter.setUsingString(serializeNumberAsString());
        gsonBuilder.registerTypeHierarchyAdapter(Number.class, numberTypeAdapter);

        // Date
        DateTypeAdapter dateTypeAdapter = new DateTypeAdapter();
        dateTypeAdapter.setDateFormat(serializeUseDateFormat());
        dateTypeAdapter.setPattern(serializeDateUsingPattern());
        dateTypeAdapter.setUsingToString(serializeDateUsingToString());
        gsonBuilder.registerTypeHierarchyAdapter(Date.class, dateTypeAdapter);

        // Enum
        EnumTypeAdapter enumTypeAdapter = new EnumTypeAdapter();
        enumTypeAdapter.setUsingValue(serializeEnumUsingIndex());
        enumTypeAdapter.setUsingField(serializeEnumUsingField());
        enumTypeAdapter.setUsingToString(serializeEnumUsingToString());
        gsonBuilder.registerTypeHierarchyAdapter(Enum.class, enumTypeAdapter);

        // pretty printing
        if (prettyFormat()) {
            gsonBuilder.setPrettyPrinting();
        }

        // exclusion
        List<Integer> modifiers = getExclusionConfiguration().getModifiers();
        int[] modifiers0 = new int[modifiers.size()];
        for (int i = 0; i < modifiers.size(); i++) {
            modifiers0[i] = modifiers.get(i);
        }
        gsonBuilder.excludeFieldsWithModifiers(modifiers0);
        for (Exclusion exclusion : getExclusionConfiguration().getDeserializationStrategies()) {
            gsonBuilder.addDeserializationExclusionStrategy(new DelegateExclusionStrategy(exclusion, false));
        }
        for (Exclusion exclusion : getExclusionConfiguration().getSerializationStrategies()) {
            gsonBuilder.addSerializationExclusionStrategy(new DelegateExclusionStrategy(exclusion, true));
        }
        if (getExclusionConfiguration().isSerializeInnerClasses()) {
            gsonBuilder.disableInnerClassSerialization();
        }

        Gson gson = gsonBuilder.create();
        // replace ObjectTypeAdapter
        replaceFactories(gson);

        JSON json = new JSON();
        GsonAdapter gsonAdapter = new GsonAdapter();
        gsonAdapter.setGson(gson);
        json.setJsonHandler(gsonAdapter);
        return json;
    }

    private static Field factoriesField = null;

    private void replaceFactories(Gson gson) {
        if (findFactoriesField()) {
            try {
                factoriesField.setAccessible(true);
                List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gson);
                int index = factories.indexOf(com.google.gson.internal.bind.ObjectTypeAdapter.FACTORY);
                if (index >= 0) {
                    // replace
                    factories = new ArrayList<TypeAdapterFactory>(factories);
                    factories.add(index, ObjectTypeAdapter.FACTORY);
                    factoriesField.set(gson, factories);
                }
            } catch (Throwable ex) {
                logger.warn(ex.getMessage(), ex);
            }
        }
    }

    private static boolean findFactoriesField() {
        if (factoriesField != null) {
            return true;
        }
        factoriesField = Reflects.getDeclaredField(Gson.class, "factories");
        return factoriesField != null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        GsonJSONBuilder result = new GsonJSONBuilder(this.getExclusionConfiguration());
        this.copyTo(result);
        return result;
    }
}
