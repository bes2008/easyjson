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

package com.github.fangjinuo.easyjson.fastjson.ext;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.github.fangjinuo.easyjson.core.util.Reflects;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class DeserializeBeanInfoGetter {
    private JavaBeanInfo javaBeanInfo;
    public DeserializeBeanInfoGetter(JavaBeanInfo javaBeanInfo){
        this.javaBeanInfo = javaBeanInfo;
    }

    Class<?> getClazz(){
        return Reflects.getDeclaredFieldValue(javaBeanInfo, "clazz");
    }
    Class<?> getBuilderClass(){
        return Reflects.getDeclaredFieldValue(javaBeanInfo, "builderClass");
    }
    Constructor<?> getDefaultConstructor(){
        return Reflects.getDeclaredFieldValue(javaBeanInfo, "defaultConstructor");
    }
    Constructor<?> getCreatorConstructor(){
        return Reflects.getDeclaredFieldValue(javaBeanInfo, "creatorConstructor");
    }
    Method getFactoryMethod(){
        return Reflects.getDeclaredFieldValue(javaBeanInfo, "factoryMethod");
    }
    Method getBuildMethod(){
        return Reflects.getDeclaredFieldValue(javaBeanInfo, "buildMethod");
    }
    JSONType getJsonType(){
        return Reflects.getDeclaredFieldValue(javaBeanInfo, "jsonType");
    }
    FieldInfo[] getFields(){
        return Reflects.getDeclaredFieldValue(javaBeanInfo, "fields");
    }
}
