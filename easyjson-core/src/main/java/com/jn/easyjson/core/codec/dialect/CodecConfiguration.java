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
import com.jn.langx.util.collection.MapAccessor;

import java.text.DateFormat;
import java.util.HashMap;

public class CodecConfiguration extends MapAccessor implements Configuration {

    private String id;

    /**
     * 当值为null时，是否序列化
     */
    private Boolean serialNull;

    /**
     * 是否要序列化
     */
    private Boolean serialize;

    /**
     * 是否要反序列化
     */
    private Boolean deserialize;

    /**
     * 日期类型的字段，格式
     */
    private DateFormat dateFormat;
    private String datePattern;

    /**
     * 枚举序列化的方式
     */
    private Boolean enumUsingName;
    // 编号
    private Boolean enumUsingValue;
    private Boolean enumUsingToString;


    private Boolean booleanUsing01;
    private Boolean booleanUsingONOFF;

    public CodecConfiguration(){
        setTarget(new HashMap<String, Object>());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSerialNull() {
        return serialNull;
    }

    public void setSerialNull(Boolean serialNull) {
        this.serialNull = serialNull;
    }

    public Boolean getSerialize() {
        return serialize;
    }

    public void setSerialize(Boolean serialize) {
        this.serialize = serialize;
    }

    public Boolean getDeserialize() {
        return deserialize;
    }

    public void setDeserialize(Boolean deserialize) {
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

    public Boolean getEnumUsingName() {
        return enumUsingName;
    }

    public void setEnumUsingName(Boolean enumUsingName) {
        this.enumUsingName = enumUsingName;
    }

    public Boolean getEnumUsingValue() {
        return enumUsingValue;
    }

    public void setEnumUsingValue(Boolean enumUsingValue) {
        this.enumUsingValue = enumUsingValue;
    }

    public Boolean getEnumUsingToString() {
        return enumUsingToString;
    }

    public void setEnumUsingToString(Boolean enumUsingToString) {
        this.enumUsingToString = enumUsingToString;
    }

    public Boolean getBooleanUsing01() {
        return booleanUsing01;
    }

    public void setBooleanUsing01(Boolean booleanUsing01) {
        this.booleanUsing01 = booleanUsing01;
    }

    public Boolean getBooleanUsingONOFF() {
        return booleanUsingONOFF;
    }

    public void setBooleanUsingONOFF(Boolean booleanUsingONOFF) {
        this.booleanUsingONOFF = booleanUsingONOFF;
    }
}
