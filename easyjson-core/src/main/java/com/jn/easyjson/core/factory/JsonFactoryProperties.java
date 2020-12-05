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

/**
 * 用于配置 JSON Builder，可以在Spring环境下直接用
 */
public class JsonFactoryProperties {
    private JsonScope jsonScope;


    private boolean lenient = false;
    private boolean serializeNulls = true;
    private boolean prettyFormat = false;
    private boolean serializeEnumUsingToString = false;
    private boolean serializeEnumUsingIndex = false;
    private boolean serializeLongAsString = false;
    private String datePattern = null;
    private boolean serializeDateUsingToString;

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

    public boolean getSerializeDateUsingToString() {
        return serializeDateUsingToString;
    }

    public void setSerializeDateUsingToString(boolean serializeDateUsingToString) {
        this.serializeDateUsingToString = serializeDateUsingToString;
    }
}
