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

import com.jn.langx.util.Objects;
import com.jn.langx.util.hash.HashCodeBuilder;

public class BeanPropertyId {
    private String beanClass;
    private String propertyName;

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public boolean equals(Object o) {
        if(o==this){
            return true;
        }

        if(o==null || o.getClass()!=BeanPropertyId.class){
            return false;
        }
        BeanPropertyId ID = (BeanPropertyId)o;

        if(!Objects.equals(ID.beanClass, beanClass)){
            return false;
        }

        if(!Objects.equals(ID.propertyName, propertyName)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().with(beanClass).with(propertyName).build();
    }
}
