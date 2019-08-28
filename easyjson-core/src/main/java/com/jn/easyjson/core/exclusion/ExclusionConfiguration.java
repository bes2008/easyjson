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

package com.jn.easyjson.core.exclusion;

import com.jn.langx.util.reflect.FieldAttributes;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ExclusionConfiguration {

    /**
     * modifier exlusion:
     * default : transient, static
     * else: you specified using {@link #overrideModifiers(int...)}
     */
    private List<Integer> modifiers = new ArrayList<Integer>(Arrays.asList(new Integer[]{Modifier.TRANSIENT, Modifier.STATIC}));
    private int _modifiers = Modifier.STATIC | Modifier.TRANSIENT;

    private boolean serializeInnerClasses = true;
    private List<Exclusion> serializationStrategies = Collections.emptyList();
    private List<Exclusion> deserializationStrategies = Collections.emptyList();


    public List<Integer> getModifiers() {
        return modifiers;
    }

    public boolean isSerializeInnerClasses() {
        return serializeInnerClasses;
    }

    public List<Exclusion> getSerializationStrategies() {
        return serializationStrategies;
    }

    public List<Exclusion> getDeserializationStrategies() {
        return deserializationStrategies;
    }

    /**
     * append modifier
     *
     * @param modifier
     * @return
     */
    public ExclusionConfiguration appendModifier(int modifier) {
        this.modifiers.add(modifier);
        _modifiers |= modifier;
        return this;
    }

    /**
     * override modifiers
     *
     * @param modifiers
     * @return
     */
    public ExclusionConfiguration overrideModifiers(int... modifiers) {
        this.modifiers.clear();
        for (int modifier : modifiers) {
            this.modifiers.add(modifier);
        }
        _modifiers = 0;
        for (int modifier : modifiers) {
            _modifiers |= modifier;
        }
        return this;
    }

    public ExclusionConfiguration disableInnerClassSerialization() {
        this.serializeInnerClasses = false;
        return this;
    }


    public ExclusionConfiguration appendExclusion(Exclusion Exclusion,
                                                  boolean serialization, boolean deserialization) {
        if (serialization) {
            this.serializationStrategies = new ArrayList<Exclusion>(serializationStrategies);
            this.serializationStrategies.add(Exclusion);
        }
        if (deserialization) {
            this.deserializationStrategies
                    = new ArrayList<Exclusion>(deserializationStrategies);
            this.deserializationStrategies.add(Exclusion);
        }
        return this;
    }


    public boolean isExcludedField(Field field, boolean serialize) {
        if ((_modifiers & field.getModifiers()) != 0) {
            return true;
        }
        if (field.isSynthetic()) {
            return true;
        }
        if (!serializeInnerClasses && Reflects.isInnerClass(field.getType())) {
            return true;
        }

        if (Reflects.isAnonymousOrLocal(field.getType())) {
            return true;
        }

        List<Exclusion> list = serialize ? serializationStrategies : deserializationStrategies;
        if (!list.isEmpty()) {
            FieldAttributes fieldAttributes = new FieldAttributes(field);
            for (Exclusion Exclusion : list) {
                if (Exclusion.shouldSkipField(fieldAttributes, serialize)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean excludedClassChecks(Class<?> clazz) {
        if (!serializeInnerClasses && Reflects.isInnerClass(clazz)) {
            return true;
        }

        if (Reflects.isAnonymousOrLocal(clazz)) {
            return true;
        }

        return false;
    }

    public boolean isExcludedClass(Class<?> clazz, boolean serialize) {
        return excludedClassChecks(clazz) ||
                isExcludedClassInStrategy(clazz, serialize);
    }

    private boolean isExcludedClassInStrategy(Class<?> clazz, boolean serialize) {
        List<Exclusion> list = serialize ? serializationStrategies : deserializationStrategies;
        for (Exclusion exclusion : list) {
            if (exclusion.shouldSkipClass(clazz, serialize)) {
                return true;
            }
        }
        return false;
    }


}
