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

package com.github.fangjinuo.easyjson.api;

import java.text.DateFormat;

public abstract class JSONBuilder {
    // null
    protected boolean serializeNulls = false;

    // enum priority: ordinal() > toString() > field > name()
    protected boolean serializeEnumUsingToString = false; // default using name()
    protected boolean serializeEnumUsingValue = false;
    protected String serializeEnumUsingField = null;

    // date priority: dateFormat > pattern > toString() > timestamp []
    protected DateFormat dateFormat=null;
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

    public JSONBuilder serializeUseDateFormat(DateFormat df){
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


    public abstract JSON build();
}
