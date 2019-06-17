/*
 *  Copyright 2019 the original author or authors.
 *
 *  Licensed under the LGPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.github.fangjinuo.easyjson.core.exclusion;

import com.github.fangjinuo.easyjson.core.util.FieldAttributes;
import com.github.fangjinuo.easyjson.core.util.Reflects;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Excluder implements Cloneable {
    public static final Excluder DEFAULT = new Excluder();

    private int modifiers = Modifier.TRANSIENT | Modifier.STATIC;
    private boolean serializeInnerClasses = true;
    private List<Exclusion> serializationStrategies = Collections.emptyList();
    private List<Exclusion> deserializationStrategies = Collections.emptyList();

    @Override
    protected Excluder clone() {
        try {
            return (Excluder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public Excluder withModifiers(int... modifiers) {
        Excluder result = clone();
        result.modifiers = 0;
        for (int modifier : modifiers) {
            result.modifiers |= modifier;
        }
        return result;
    }

    public Excluder disableInnerClassSerialization() {
        Excluder result = clone();
        result.serializeInnerClasses = false;
        return result;
    }


    public Excluder withExclusion(Exclusion Exclusion,
                                  boolean serialization, boolean deserialization) {
        Excluder result = clone();
        if (serialization) {
            result.serializationStrategies = new ArrayList<Exclusion>(serializationStrategies);
            result.serializationStrategies.add(Exclusion);
        }
        if (deserialization) {
            result.deserializationStrategies
                    = new ArrayList<Exclusion>(deserializationStrategies);
            result.deserializationStrategies.add(Exclusion);
        }
        return result;
    }


    public boolean excludeField(Field field, boolean serialize) {
        if ((modifiers & field.getModifiers()) != 0) {
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
                if (Exclusion.shouldSkipField(fieldAttributes)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean excludeClassChecks(Class<?> clazz) {
        if (!serializeInnerClasses && Reflects.isInnerClass(clazz)) {
            return true;
        }

        if (Reflects.isAnonymousOrLocal(clazz)) {
            return true;
        }

        return false;
    }

    public boolean excludeClass(Class<?> clazz, boolean serialize) {
        return excludeClassChecks(clazz) ||
                excludeClassInStrategy(clazz, serialize);
    }

    private boolean excludeClassInStrategy(Class<?> clazz, boolean serialize) {
        List<Exclusion> list = serialize ? serializationStrategies : deserializationStrategies;
        for (Exclusion Exclusion : list) {
            if (Exclusion.shouldSkipClass(clazz)) {
                return true;
            }
        }
        return false;
    }


}
