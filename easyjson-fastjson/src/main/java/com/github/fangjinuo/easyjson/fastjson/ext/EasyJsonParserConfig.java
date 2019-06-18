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

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;
import com.github.fangjinuo.easyjson.core.exclusion.ExclusionConfiguration;
import com.github.fangjinuo.easyjson.fastjson.FastJsonJSONBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EasyJsonParserConfig extends ParserConfig {
    private FastJsonJSONBuilder jsonJSONBuilder;

    public EasyJsonParserConfig(FastJsonJSONBuilder builder) {
        super();
        this.jsonJSONBuilder = builder;
    }

    @Override
    public ObjectDeserializer createJavaBeanDeserializer(Class<?> clazz, Type type) {
        boolean asmEnable = this.isAsmEnable() & !this.fieldBased;
        if (asmEnable) {
            JSONType jsonType = TypeUtils.getAnnotation(clazz, JSONType.class);

            if (jsonType != null) {
                Class<?> deserializerClass = jsonType.deserializer();
                if (deserializerClass != Void.class) {
                    try {
                        Object deseralizer = deserializerClass.newInstance();
                        if (deseralizer instanceof ObjectDeserializer) {
                            return (ObjectDeserializer) deseralizer;
                        }
                    } catch (Throwable e) {
                        // skip
                    }
                }

                asmEnable = jsonType.asm();
            }

            if (asmEnable) {
                Class<?> superClass = JavaBeanInfo.getBuilderClass(clazz, jsonType);
                if (superClass == null) {
                    superClass = clazz;
                }

                for (; ; ) {
                    if (!Modifier.isPublic(superClass.getModifiers())) {
                        asmEnable = false;
                        break;
                    }

                    superClass = superClass.getSuperclass();
                    if (superClass == Object.class || superClass == null) {
                        break;
                    }
                }
            }
        }

        if (clazz.getTypeParameters().length != 0) {
            asmEnable = false;
        }

        if (asmEnable && asmFactory != null && asmFactory.classLoader.isExternalClass(clazz)) {
            asmEnable = false;
        }

        if (asmEnable) {
            asmEnable = ASMUtils.checkName(clazz.getSimpleName());
        }

        if (asmEnable) {
            if (clazz.isInterface()) {
                asmEnable = false;
            }
            JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz
                    , type
                    , propertyNamingStrategy
                    , false
                    , TypeUtils.compatibleWithJavaBean
                    , isJacksonCompatible()
            );

            if (asmEnable && beanInfo.fields.length > 200) {
                asmEnable = false;
            }

            Constructor<?> defaultConstructor = beanInfo.defaultConstructor;
            if (asmEnable && defaultConstructor == null && !clazz.isInterface()) {
                asmEnable = false;
            }
            // ==========EasyJson start=================
            beanInfo = filterFields(beanInfo);
            //===========EasyJson end===================
            for (FieldInfo fieldInfo : beanInfo.fields) {
                if (fieldInfo.getOnly) {
                    asmEnable = false;
                    break;
                }

                Class<?> fieldClass = fieldInfo.fieldClass;
                if (!Modifier.isPublic(fieldClass.getModifiers())) {
                    asmEnable = false;
                    break;
                }

                if (fieldClass.isMemberClass() && !Modifier.isStatic(fieldClass.getModifiers())) {
                    asmEnable = false;
                    break;
                }

                if (fieldInfo.getMember() != null //
                        && !ASMUtils.checkName(fieldInfo.getMember().getName())) {
                    asmEnable = false;
                    break;
                }

                JSONField annotation = fieldInfo.getAnnotation();
                if (annotation != null //
                        && ((!ASMUtils.checkName(annotation.name())) //
                        || annotation.format().length() != 0 //
                        || annotation.deserializeUsing() != Void.class //
                        || annotation.unwrapped())
                        || (fieldInfo.method != null && fieldInfo.method.getParameterTypes().length > 1)) {
                    asmEnable = false;
                    break;
                }

                if (fieldClass.isEnum()) { // EnumDeserializer
                    ObjectDeserializer fieldDeser = this.getDeserializer(fieldClass);
                    if (!(fieldDeser instanceof EnumDeserializer)) {
                        asmEnable = false;
                        break;
                    }
                }
            }
        }

        if (asmEnable) {
            if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
                asmEnable = false;
            }
        }

        if (asmEnable) {
            if (TypeUtils.isXmlField(clazz)) {
                asmEnable = false;
            }
        }

        if (!asmEnable) {
            return new JavaBeanDeserializer(this, clazz, type);
        }

        JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz, type, propertyNamingStrategy);
        // ==========EasyJson start=================
        beanInfo = filterFields(beanInfo);
        //===========EasyJson end===================
        try {
            return asmFactory.createJavaBeanDeserializer(this, beanInfo);
            // } catch (VerifyError e) {
            // e.printStackTrace();
            // return new JavaBeanDeserializer(this, clazz, type);
        } catch (NoSuchMethodException ex) {
            // return new JavaBeanDeserializer(this, clazz, type);
            //=================EasyJson start========================
            return new EasyJsonJavaBeanDeserializer(this, clazz, type);
            //=================EasyJson end==========================
        } catch (JSONException asmError) {
            return new JavaBeanDeserializer(this, beanInfo);
        } catch (Exception e) {
            throw new JSONException("create asm deserializer error, " + clazz.getName(), e);
        }
    }

    public JavaBeanInfo filterFields(JavaBeanInfo beanInfo) {
        // ==========EasyJson start=================
        Class clazz = beanInfo.clazz;
        if (jsonJSONBuilder != null) {

            ExclusionConfiguration exclusionConfiguration = jsonJSONBuilder.getExclusionConfiguration();
            if (!exclusionConfiguration.isExcludedClass(clazz, false)) {
                FieldInfo[] fields = beanInfo.fields;
                if (fields != null) {
                    List<FieldInfo> fieldInfoes = new ArrayList<FieldInfo>();
                    for (FieldInfo fieldInfo : fields) {
                        if (exclusionConfiguration.isExcludedField(fieldInfo.field, false)) {
                            continue;
                        }
                        fieldInfoes.add(fieldInfo);
                    }
                    if (fieldInfoes.size() != fields.length) {
                        beanInfo = new JavaBeanInfo(clazz,
                                beanInfo.builderClass,
                                beanInfo.defaultConstructor,
                                beanInfo.creatorConstructor,
                                beanInfo.factoryMethod,
                                beanInfo.buildMethod,
                                beanInfo.jsonType,
                                fieldInfoes);
                    }
                }
            } else {
                if (beanInfo.fields.length > 0) {
                    beanInfo = new JavaBeanInfo(clazz,
                            beanInfo.builderClass,
                            beanInfo.defaultConstructor,
                            beanInfo.creatorConstructor,
                            beanInfo.factoryMethod,
                            beanInfo.buildMethod,
                            beanInfo.jsonType,
                            new ArrayList<FieldInfo>(0));
                }
            }
        }
        return beanInfo;
        //===========EasyJson end===================
    }
}
