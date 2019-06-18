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

package com.github.fangjinuo.easyjson.core;

import com.github.fangjinuo.easyjson.core.exclusion.Exclusion;
import com.github.fangjinuo.easyjson.core.exclusion.ExclusionConfiguration;
import com.github.fangjinuo.easyjson.core.exclusion.IgnoreAnnotationExclusion;

import java.text.DateFormat;

public abstract class JSONBuilder {
    // null
    protected boolean serializeNulls = false;

    // enum priority: ordinal() > toString() > field > name()
    protected boolean serializeEnumUsingToString = false; // default using name()
    protected boolean serializeEnumUsingValue = false;
    protected String serializeEnumUsingField = null;

    // date priority: dateFormat > pattern > toString() > timestamp []
    protected DateFormat dateFormat = null;
    protected String serializeDateUsingPattern = null;// default : using timestamp
    protected boolean serializeDateUsingToString = false;

    // number priority: longAsString > numberAsString > number
    protected boolean serializeLongAsString = false;
    protected boolean serializeNumberAsString = false;

    // boolean priority: on_off > 1_0 > true_false
    protected boolean serializeBooleanUsingOnOff = false;
    protected boolean serializeBooleanUsing1_0 = false;

    // print format
    protected boolean prettyFormat = false;

    protected final ExclusionConfiguration exclusionConfiguration = new ExclusionConfiguration();

    public JSONBuilder() {
    }

    public JSONBuilder serializeNulls() {
        this.serializeNulls = true;
        return this;
    }

    public JSONBuilder prettyFormat() {
        this.prettyFormat = true;
        return this;
    }

    public JSONBuilder serializeEnumUsingToString() {
        this.serializeEnumUsingToString = true;
        return this;
    }

    public JSONBuilder serializeEnumUsingValue() {
        this.serializeEnumUsingValue = true;
        return this;
    }

    public JSONBuilder serializeEnumUsingField(String field) {
        if (field != null && !field.trim().isEmpty()) {
            this.serializeEnumUsingField = field.trim();
        }
        return this;
    }

    public JSONBuilder serializeLongAsString() {
        this.serializeLongAsString = true;
        return this;
    }

    public JSONBuilder serializeNumberAsString() {
        this.serializeNumberAsString = true;
        return this;
    }

    public JSONBuilder serializeUseDateFormat(DateFormat df) {
        this.dateFormat = df;
        return this;
    }

    public JSONBuilder serializeDateUsingPattern(String datePattern) {
        this.serializeDateUsingPattern = datePattern;
        return this;
    }

    public JSONBuilder serializeDateUsingToString() {
        this.serializeDateUsingToString = true;
        return this;
    }

    public JSONBuilder serializeBooleanUsingOnOff() {
        this.serializeBooleanUsingOnOff = true;
        return this;
    }

    public JSONBuilder serializeBooleanUsing1_0() {
        this.serializeBooleanUsing1_0 = true;
        return this;
    }

    protected boolean ignoreAnnotationForSerialize = false;

    public boolean isIgnoreAnnotationForSerialize() {
        return ignoreAnnotationForSerialize;
    }

    public JSONBuilder enableIgnoreAnnotation(){
        IgnoreAnnotationExclusion ignoreAnnotationExclusion = new IgnoreAnnotationExclusion();
        ignoreAnnotationForSerialize = true;
        exclusionConfiguration.appendExclusion(ignoreAnnotationExclusion, true, true);
        return this;
    }

    /**
     * Configures JSON to excludes all class fields that have the specified modifiers. By default,
     * JSON will exclude all fields marked transient or static. This method will override that
     * behavior.
     *
     * @param modifiers the field modifiers. You must use the modifiers specified in the
     *                  {@link java.lang.reflect.Modifier} class. For example,
     *                  {@link java.lang.reflect.Modifier#TRANSIENT},
     *                  {@link java.lang.reflect.Modifier#STATIC}.
     * @return a reference to this {@code JSONBuilder} object to fulfill the "Builder" pattern
     */
    public JSONBuilder excludeFieldsWithModifiers(int... modifiers) {
        exclusionConfiguration.overrideModifiers(modifiers);
        return this;
    }

    public JSONBuilder excludeFieldsWithAppendModifiers(int... modifiers) {
        for (int modifier : modifiers) {
            exclusionConfiguration.appendModifier(modifier);
        }
        return this;
    }

    public JSONBuilder disableInnerClassSerialization() {
        exclusionConfiguration.disableInnerClassSerialization();
        return this;
    }

    public JSONBuilder addExclusionStrategies(Exclusion... strategies) {
        for (Exclusion strategy : strategies) {
            exclusionConfiguration.appendExclusion(strategy, true, true);
        }
        return this;
    }

    public JSONBuilder addSerializationExclusion(Exclusion strategy) {
        exclusionConfiguration.appendExclusion(strategy, true, false);
        return this;
    }

    public JSONBuilder addDeserializationExclusion(Exclusion strategy) {
        exclusionConfiguration.appendExclusion(strategy, false, true);
        return this;
    }

    public ExclusionConfiguration getExclusionConfiguration() {
        return exclusionConfiguration;
    }

    public abstract JSON build();
}
