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

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.annotation.DependOn;
import com.jn.easyjson.core.codec.dialect.DialectIdentify;
import com.jn.easyjson.core.exclusion.Exclusion;
import com.jn.easyjson.core.exclusion.ExclusionConfiguration;
import com.jn.easyjson.gson.exclusion.DelegateExclusionStrategy;
import com.jn.easyjson.gson.typeadapter.*;
import com.jn.langx.annotation.Name;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.iter.ReverseListIterator;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.struct.counter.SimpleIntegerCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Name("gson")
@DependOn("com.google.gson.Gson")
public class GsonJSONBuilder extends JSONBuilder {
    private static final Logger logger = LoggerFactory.getLogger(GsonJSONBuilder.class);

    public static final DialectIdentify GSON = new DialectIdentify();

    static {
        GSON.setId("gson");
        GSON.setLibUrl(Reflects.getCodeLocation(Gson.class).toString());
    }

    public GsonJSONBuilder() {
        super();
        dialectIdentify(GSON);
    }

    public GsonJSONBuilder(ExclusionConfiguration exclusionConfiguration) {
        super(exclusionConfiguration);
        dialectIdentify(GSON);
    }

    @Override
    public JSON build() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Null
        if (serializeNulls()) {
            gsonBuilder.serializeNulls();
        }

        // Boolean
        gsonBuilder.registerTypeAdapterFactory(new BooleanTypeAdapterFactory(this));
        // Number
        gsonBuilder.registerTypeAdapterFactory(new NumberTypeAdapterFactory(this));
        // Date
        gsonBuilder.registerTypeAdapterFactory(new DateTypeAdapterFactory(this));
        // Enum
        gsonBuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory(this));

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
    private static Field jsonAdapterFactoryField = null;
    private static Field constructorConstructorField = null;
    private static Field fieldNamingStrategyField = null;

    private void replaceFactories(Gson gson) {
        if (findFactoriesField()) {
            try {
                factoriesField.setAccessible(true);
                List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>((List<TypeAdapterFactory>) factoriesField.get(gson));
                // 替换 ObjectTypeAdapter
                int index = factories.indexOf(com.google.gson.internal.bind.ObjectTypeAdapter.FACTORY);
                if (index >= 0) {
                    // replace
                    factories.remove(index);
                    factories.add(index, ObjectTypeAdapter.FACTORY);
                }

                //
                if (jsonAdapterFactoryField != null && constructorConstructorField != null) {
                    ConstructorConstructor constructorConstructor = Reflects.getFieldValue(constructorConstructorField, gson, true, false);

                    // 替换 jsonAnnotationAdapterFactory
                    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory = Reflects.getFieldValue(jsonAdapterFactoryField, gson, true, false);
                    EasyjsonAdapterAnnotationTypeAdapterFactory easyjsonAnnotationTypeAdapterFactory = new EasyjsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
                    if (jsonAdapterFactory != null) {
                        index = factories.indexOf(jsonAdapterFactory);
                    }
                    if (index >= 0) {
                        factories.remove(index);
                        factories.add(index, easyjsonAnnotationTypeAdapterFactory);
                        // it will fail, because a final field will not be modified
                        Reflects.setFieldValue(jsonAdapterFactoryField, gson, easyjsonAnnotationTypeAdapterFactory, true, false);
                    }

                    // 替换 reflectiveTypeAdapterFactory
                    Iterator<TypeAdapterFactory> iterator = new ReverseListIterator(factories);
                    SimpleIntegerCounter reflectiveTypeAdapterFactoryIndex = new SimpleIntegerCounter(factories.size());
                    while (iterator.hasNext()){
                        reflectiveTypeAdapterFactoryIndex.decrement();
                        TypeAdapterFactory typeAdapterFactory  = iterator.next();
                        if(typeAdapterFactory instanceof ReflectiveTypeAdapterFactory){
                            break;
                        }
                    }
                    index = reflectiveTypeAdapterFactoryIndex.get();
                    if(index>=0 && index<factories.size()){
                        FieldNamingStrategy fieldNamingStrategy = Reflects.getFieldValue(fieldNamingStrategyField, gson, true, false);
                        EasyjsonReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new EasyjsonReflectiveTypeAdapterFactory(
                                constructorConstructor,
                                fieldNamingStrategy,
                                gson.excluder(),
                                easyjsonAnnotationTypeAdapterFactory
                        );
                        factories.remove(index);
                        factories.add(reflectiveTypeAdapterFactory);
                    }
                }

                factoriesField.set(gson, factories);
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
        jsonAdapterFactoryField = Reflects.getDeclaredField(Gson.class, "jsonAdapterFactory");
        constructorConstructorField = Reflects.getDeclaredField(Gson.class, "constructorConstructor");
        fieldNamingStrategyField = Reflects.getDeclaredField(Gson.class, "fieldNamingStrategy");

        return factoriesField != null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        GsonJSONBuilder result = new GsonJSONBuilder(this.getExclusionConfiguration());
        this.copyTo(result);
        return result;
    }
}
