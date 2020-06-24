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

package com.jn.easyjson.core;

import com.jn.easyjson.core.codec.dialect.CodecConfigurationRepositoryService;
import com.jn.easyjson.core.codec.dialect.DialectIdentify;
import com.jn.easyjson.core.exclusion.Exclusion;
import com.jn.easyjson.core.exclusion.ExclusionConfiguration;
import com.jn.easyjson.core.exclusion.IgnoreAnnotationExclusion;
import com.jn.langx.util.function.Consumer2;

import java.text.DateFormat;

/**
 * 代表单次 JSON 转换请求的 JSON 构建器。
 * 也可以作为全局使用。
 */
public abstract class JSONBuilder implements Cloneable {
    /***
     * 这些配置项，都代表了单次请求的配置。但由于这个配置，是针对这一次或者N次操作的数据的通用配置，
     * 所以它的优先级是最低的。
     *
     * 一旦一个类上有特定注解，则注解的优先级会高于这里的配置项的优先级。
     */

    /**
     * 配置排除策略
     */
    private final ExclusionConfiguration exclusionConfiguration;
    // null
    private boolean serializeNulls = false;
    // enum priority: ordinal() > toString() > field > name()
    private boolean serializeEnumUsingToString = false; // default using name()
    private boolean serializeEnumUsingValue = false;
    private String serializeEnumUsingField = null;
    // date priority: dateFormat > pattern > toString() > timestamp []
    private DateFormat dateFormat = null;
    private String serializeDateUsingPattern = null;// default : using timestamp
    private boolean serializeDateUsingToString = false;
    // number priority: longAsString > numberAsString > number
    private boolean serializeLongAsString = false;
    private boolean serializeNumberAsString = false;
    // boolean priority: on_off > 1_0 > true_false
    private boolean serializeBooleanUsingOnOff = false;
    private boolean serializeBooleanUsing1_0 = false;
    private boolean serializeNonFieldGetter = true;
    // print format
    private boolean prettyFormat = false;
    private boolean isLenient;

    /**
     * 是否使用全局配置项
     */
    private boolean enableCustomConfiguration = false;

    /**
     * 代理库
     */
    private DialectIdentify proxyDialectId;
    /**
     * 真正的 JSON库
     */
    private DialectIdentify dialectId;

    /**
     * 全局配置项, 这里面其实是每个类每个字段的默认配置项
     */
    private static final CodecConfigurationRepositoryService codecConfigurationRepository = CodecConfigurationRepositoryService.getInstance();

    public JSONBuilder() {
        exclusionConfiguration = new ExclusionConfiguration();
    }

    public JSONBuilder(ExclusionConfiguration exclusionConfiguration) {
        this.exclusionConfiguration = exclusionConfiguration;
    }

    public static JSONBuilder clone(JSONBuilder builder) {
        try {
            Object jsonBuilder = builder.clone();
            return (JSONBuilder) jsonBuilder;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isLenient() {
        return isLenient;
    }

    public void setLenient(boolean lenient) {
        isLenient = lenient;
    }

    public JSONBuilder serializeNulls(boolean serializeNulls) {
        this.serializeNulls = serializeNulls;
        return this;
    }

    public boolean serializeNulls() {
        return serializeNulls;
    }

    public JSONBuilder prettyFormat(boolean prettyFormat) {
        this.prettyFormat = prettyFormat;
        return this;
    }

    public boolean prettyFormat() {
        return prettyFormat;
    }

    public JSONBuilder serializeEnumUsingToString(boolean value) {
        this.serializeEnumUsingToString = value;
        return this;
    }

    public boolean serializeEnumUsingToString() {
        return serializeEnumUsingToString;
    }

    public JSONBuilder serializeEnumUsingIndex(boolean value) {
        this.serializeEnumUsingValue = value;
        return this;
    }

    public boolean serializeEnumUsingIndex() {
        return serializeEnumUsingValue;
    }

    /**
     * 该方法已废弃，原因是方法名不够明确，由 serializeEnumUsingIndex来代替
     * @param value
     * @return
     * @deprecated
     *
     */
    @Deprecated
    public JSONBuilder serializeEnumUsingValue(boolean value) {
        return this.serializeEnumUsingIndex(value);
    }

    /**
     *
     * @return
     * @deprecated
     */
    @Deprecated
    public boolean serializeEnumUsingValue() {
        return serializeEnumUsingIndex();
    }

    public JSONBuilder serializeEnumUsingField(String field) {
        if (field != null && !field.trim().isEmpty()) {
            this.serializeEnumUsingField = field.trim();
        }
        return this;
    }

    public String serializeEnumUsingField() {
        return serializeEnumUsingField;
    }

    public JSONBuilder serializeLongAsString(boolean value) {
        this.serializeLongAsString = value;
        return this;
    }

    public boolean serializeLongAsString() {
        return serializeLongAsString;
    }

    public JSONBuilder serializeNumberAsString(boolean value) {
        this.serializeNumberAsString = value;
        return this;
    }

    public boolean serializeNumberAsString() {
        return serializeNumberAsString;
    }

    public boolean serializeNonFieldGetter() {
        return this.serializeNonFieldGetter;
    }

    public JSONBuilder serializeNonFieldGetter(boolean serializeNonFieldGetter) {
        this.serializeNonFieldGetter = serializeNonFieldGetter;
        return this;
    }

    public JSONBuilder serializeUseDateFormat(DateFormat df) {
        this.dateFormat = df;
        return this;
    }

    public DateFormat serializeUseDateFormat() {
        return dateFormat;
    }

    public JSONBuilder serializeDateUsingPattern(String datePattern) {
        this.serializeDateUsingPattern = datePattern;
        return this;
    }

    public String serializeDateUsingPattern() {
        return serializeDateUsingPattern;
    }

    public JSONBuilder serializeDateUsingToString(boolean value) {
        this.serializeDateUsingToString = value;
        return this;
    }

    public boolean serializeDateUsingToString() {
        return serializeDateUsingToString;
    }

    public JSONBuilder serializeBooleanUsingOnOff(boolean value) {
        this.serializeBooleanUsingOnOff = value;
        return this;
    }

    public boolean serializeBooleanUsingOnOff() {
        return serializeBooleanUsingOnOff;
    }

    public JSONBuilder serializeBooleanUsing1_0(boolean value) {
        this.serializeBooleanUsing1_0 = value;
        return this;
    }

    public boolean serializeBooleanUsing1_0() {
        return serializeBooleanUsing1_0;
    }

    public JSONBuilder enableIgnoreAnnotation() {
        IgnoreAnnotationExclusion ignoreAnnotationExclusion = new IgnoreAnnotationExclusion();
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

    public JSONBuilder enableCustomConfiguration(boolean enable) {
        this.enableCustomConfiguration = enable;
        return this;
    }

    public boolean isEnableCustomConfiguration() {
        return this.enableCustomConfiguration;
    }
    public JSONBuilder proxyDialectIdentify(DialectIdentify dialectId){
        this.proxyDialectId = dialectId;
        return this;
    }

    public DialectIdentify proxyDialectIdentify(){
        return this.proxyDialectId ;
    }

    public JSONBuilder dialectIdentify(DialectIdentify dialectId){
        this.dialectId = dialectId;
        return this;
    }

    public DialectIdentify dialectIdentify(){
        return this.dialectId ;
    }

    public JSONBuilder withTargetClass(Class clazz) {
        return this;
    }

    public JSONBuilder withTargetClass(Class clazz, Consumer2<JSONBuilder, Class> consumer) {
        consumer.accept(this, clazz);
        return this;
    }

    public abstract JSON build();

    protected <E extends JSONBuilder> void copyTo(E builder) {
        builder.setLenient(this.isLenient);
        builder.serializeNulls(this.serializeNulls);
        builder.prettyFormat(this.prettyFormat);
        builder.serializeEnumUsingToString(this.serializeEnumUsingToString);
        builder.serializeEnumUsingIndex(this.serializeEnumUsingValue);
        builder.serializeEnumUsingField(this.serializeEnumUsingField);
        builder.serializeLongAsString(this.serializeLongAsString);
        builder.serializeNumberAsString(this.serializeNumberAsString);
        builder.serializeUseDateFormat(this.dateFormat);
        builder.serializeDateUsingPattern(this.serializeDateUsingPattern);
        builder.serializeDateUsingToString(this.serializeDateUsingToString);
        builder.serializeBooleanUsingOnOff(this.serializeBooleanUsingOnOff);
        builder.serializeBooleanUsing1_0(this.serializeBooleanUsing1_0);
        builder.enableCustomConfiguration(this.enableCustomConfiguration);
        builder.dialectIdentify(this.dialectIdentify());
        builder.proxyDialectIdentify(this.proxyDialectIdentify());
    }
}
