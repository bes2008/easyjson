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

package com.jn.easyjson.core.factory;

import com.jn.easyjson.core.codec.dialect.DialectIdentify;
import com.jn.easyjson.core.exclusion.Exclusion;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;

import java.text.DateFormat;
import java.util.List;
import java.util.Set;

/**
 * 用于配置 JSON Builder，可以在Spring环境下直接用
 */
public class JsonFactoryProperties {
    private JsonScope jsonScope = null;


    private boolean lenient = false;
    private boolean serializeNulls = true;
    private boolean prettyFormat = false;
    private boolean serializeEnumUsingToString = false;
    private boolean serializeEnumUsingIndex = false;
    private boolean serializeLongAsString = false;
    private String datePattern = null;
    private DateFormat dateFormat;
    private boolean serializeDateUsingToString;
    private final Set<Integer> exclusiveFieldModifiers = Collects.emptyTreeSet();
    private boolean enableCustomConfiguration;
    private DialectIdentify proxyDialectIdentify;
    private final List<Exclusion> exclusions = Collects.emptyArrayList();

    public JsonScope getJsonScope() {
        return jsonScope;
    }

    public void setJsonScope(JsonScope jsonScope) {
        this.jsonScope = jsonScope;
    }

    public boolean isLenient() {
        return lenient;
    }

    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public boolean isSerializeNulls() {
        return serializeNulls;
    }

    public void setSerializeNulls(boolean serializeNulls) {
        this.serializeNulls = serializeNulls;
    }

    public boolean isPrettyFormat() {
        return prettyFormat;
    }

    public void setPrettyFormat(boolean prettyFormat) {
        this.prettyFormat = prettyFormat;
    }

    public boolean isSerializeEnumUsingToString() {
        return serializeEnumUsingToString;
    }

    public void setSerializeEnumUsingToString(boolean serializeEnumUsingToString) {
        this.serializeEnumUsingToString = serializeEnumUsingToString;
    }

    public boolean isSerializeEnumUsingIndex() {
        return serializeEnumUsingIndex;
    }

    public void setSerializeEnumUsingIndex(boolean serializeEnumUsingIndex) {
        this.serializeEnumUsingIndex = serializeEnumUsingIndex;
    }

    public boolean isSerializeLongAsString() {
        return serializeLongAsString;
    }

    public void setSerializeLongAsString(boolean serializeLongAsString) {
        this.serializeLongAsString = serializeLongAsString;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean getSerializeDateUsingToString() {
        return serializeDateUsingToString;
    }

    public void setSerializeDateUsingToString(boolean serializeDateUsingToString) {
        this.serializeDateUsingToString = serializeDateUsingToString;
    }

    public boolean isSerializeDateUsingToString() {
        return serializeDateUsingToString;
    }

    public Set<Integer> getExclusiveFieldModifiers() {
        return exclusiveFieldModifiers;
    }

    public void setExclusiveFieldModifiers(Set<Integer> exclusiveFieldModifiers) {
        this.exclusiveFieldModifiers.addAll(exclusiveFieldModifiers);
    }

    public void addExclusiveFieldModifier(int modifier) {
        this.exclusiveFieldModifiers.add(modifier);
    }

    public boolean isEnableCustomConfiguration() {
        return enableCustomConfiguration;
    }

    public void setEnableCustomConfiguration(boolean enableCustomConfiguration) {
        this.enableCustomConfiguration = enableCustomConfiguration;
    }

    public DialectIdentify getProxyDialectIdentify() {
        return proxyDialectIdentify;
    }

    public void setProxyDialectIdentify(DialectIdentify proxyDialectIdentify) {
        this.proxyDialectIdentify = proxyDialectIdentify;
    }

    public List<Exclusion> getExclusions() {
        return exclusions;
    }

    public void setExclusions(List<Exclusion> exclusions) {
        this.exclusions.addAll(exclusions);
    }

    public void addExclusion(Exclusion exclusion) {
        this.exclusions.add(exclusion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JsonFactoryProperties that = (JsonFactoryProperties) o;

        if (serializeNulls != that.serializeNulls) {
            return false;
        }
        if (prettyFormat != that.prettyFormat) {
            return false;
        }

        if (serializeEnumUsingToString != that.serializeEnumUsingToString) {
            return false;
        }
        if (serializeEnumUsingIndex != that.serializeEnumUsingIndex) {
            return false;
        }
        if (serializeLongAsString != that.serializeLongAsString) {
            return false;
        }
        if (serializeDateUsingToString != that.serializeDateUsingToString) {
            return false;
        }
        if (enableCustomConfiguration != that.enableCustomConfiguration) {
            return false;
        }

        if (proxyDialectIdentify != that.proxyDialectIdentify) {
            return false;
        }
        if (!Objs.equals(dateFormat, that.dateFormat)) {
            return false;
        }
        if (!Objs.equals(datePattern, that.datePattern)) {
            return false;
        }
        if (Collects.diff(this.exclusions, that.exclusions).hasDifference()) {
            return false;
        }
        return !Collects.diff(this.exclusiveFieldModifiers, that.exclusiveFieldModifiers).hasDifference();
    }

    @Override
    public int hashCode() {
        return Objs.hash(
                serializeNulls,
                serializeEnumUsingToString,
                serializeEnumUsingIndex,
                serializeLongAsString,
                serializeDateUsingToString,
                enableCustomConfiguration,
                prettyFormat,
                datePattern,
                proxyDialectIdentify
        );
    }
}
