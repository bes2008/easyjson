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

package com.alibaba.fastjson.easyjson;

import com.jn.easyjson.core.codec.dialect.PropertyCodecConfiguration;
import com.jn.easyjson.core.codec.dialect.PropertyCodecConfigurationMerger;
import com.jn.easyjson.core.codec.dialect.PropertyConfigurationSourceType;
import com.jn.langx.annotation.NonNull;

public class JsonFieldPropertyCodeConfigurationMerger implements PropertyCodecConfigurationMerger {
    @Override
    public void merge(@NonNull PropertyCodecConfiguration configuration, @NonNull PropertyCodecConfiguration newConfiguration) {
        if(newConfiguration.getName()!=null){
            configuration.setName(newConfiguration.getName());
        }
        if(newConfiguration.getAlias()!=null){
            configuration.setAlias(newConfiguration.getAlias());
        }
        if(newConfiguration.getSerialNull()!=null){
            configuration.setSerialNull(newConfiguration.getSerialNull());
        }
        if(newConfiguration.getSourceType()== PropertyConfigurationSourceType.GETTER || newConfiguration.getSourceType()== PropertyConfigurationSourceType.FIELD) {
            if (newConfiguration.getSerialize() != null) {
                configuration.setSerialize(newConfiguration.getSerialize());
            }
        }
        if(newConfiguration.getSourceType()==PropertyConfigurationSourceType.SETTER || newConfiguration.getSourceType()== PropertyConfigurationSourceType.FIELD) {
            if (newConfiguration.getDeserialize() != null) {
                configuration.setDeserialize(newConfiguration.getDeserialize());
            }
        }
        if(newConfiguration.getDateFormat()!=null){
            configuration.setDateFormat(newConfiguration.getDateFormat());
        }
        if(newConfiguration.getDatePattern()!=null){
            configuration.setDatePattern(newConfiguration.getDatePattern());
        }
        if(newConfiguration.getEnumUsingName()!=null){
            configuration.setEnumUsingName(newConfiguration.getEnumUsingName());
        }
        if(newConfiguration.getEnumUsingToString()!=null){
            configuration.setEnumUsingToString(newConfiguration.getEnumUsingToString());
        }
        if(newConfiguration.getEnumUsingIndex()!=null){
            configuration.setEnumUsingIndex(newConfiguration.getEnumUsingIndex());
        }
        if(newConfiguration.getBooleanUsing01()!=null){
            configuration.setBooleanUsing01(newConfiguration.getBooleanUsing01());
        }
        if(newConfiguration.getBooleanUsingONOFF()!=null){
            configuration.setBooleanUsingONOFF(newConfiguration.getBooleanUsingONOFF());
        }
    }


}
