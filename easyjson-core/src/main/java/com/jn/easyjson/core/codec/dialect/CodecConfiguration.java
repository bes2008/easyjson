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

package com.jn.easyjson.core.codec.dialect;

import com.jn.langx.configuration.Configuration;

import java.text.DateFormat;

public class CodecConfiguration implements Configuration {

    private String id;

    /**
     * 当值为null时，是否序列化
     */
    private boolean serialNull;

    /**
     * 是否要序列化
     */
    private boolean serialize = true;

    /**
     * 是否要反序列化
     */
    private boolean deserialize = true;

    /**
     * 日期类型的字段，格式
     */
    private DateFormat dateFormat;
    private String datePattern;

    /**
     * 枚举序列化的方式
     */
    private boolean enumUsingName = true;
    // 编号
    private boolean enumUsingValue=false;
    private boolean enumUsingToString=false;


    private boolean booleanUsing01=false;
    private boolean booleanUsingONOFF=false;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public boolean isSerialNull() {
        return serialNull;
    }

    public void setSerialNull(boolean serialNull) {
        this.serialNull = serialNull;
    }

    public boolean isSerialize() {
        return serialize;
    }

    public void setSerialize(boolean serialize) {
        this.serialize = serialize;
    }

    public boolean isDeserialize() {
        return deserialize;
    }

    public void setDeserialize(boolean deserialize) {
        this.deserialize = deserialize;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public boolean isEnumUsingName() {
        return enumUsingName;
    }

    public void setEnumUsingName(boolean enumUsingName) {
        this.enumUsingName = enumUsingName;
    }

    public boolean isEnumUsingValue() {
        return enumUsingValue;
    }

    public void setEnumUsingValue(boolean enumUsingValue) {
        this.enumUsingValue = enumUsingValue;
    }

    public boolean isEnumUsingToString() {
        return enumUsingToString;
    }

    public void setEnumUsingToString(boolean enumUsingToString) {
        this.enumUsingToString = enumUsingToString;
    }

    public boolean isBooleanUsing01() {
        return booleanUsing01;
    }

    public void setBooleanUsing01(boolean booleanUsing01) {
        this.booleanUsing01 = booleanUsing01;
    }

    public boolean isBooleanUsingONOFF() {
        return booleanUsingONOFF;
    }

    public void setBooleanUsingONOFF(boolean booleanUsingONOFF) {
        this.booleanUsingONOFF = booleanUsingONOFF;
    }
}
