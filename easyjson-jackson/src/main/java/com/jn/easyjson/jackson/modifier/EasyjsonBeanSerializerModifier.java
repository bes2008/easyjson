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

package com.jn.easyjson.jackson.modifier;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.jn.easyjson.core.util.JSONs;
import com.jn.easyjson.jackson.DateSeries;
import com.jn.easyjson.jackson.serializer.*;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.Types;

import java.util.List;
import java.util.Map;

public class EasyjsonBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        return super.changeProperties(config, beanDesc, beanProperties);
    }

    @Override
    public JsonSerializer<?> modifyEnumSerializer(SerializationConfig config, JavaType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        return super.modifyEnumSerializer(config, valueType, beanDesc, serializer);
    }

    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        Class beanClass = beanDesc.getBeanClass();
        if(Boolean.class == beanClass || boolean.class==beanClass){
            return new BooleanSerializer();
        }
        if (Reflects.isSubClassOrEquals(Number.class, beanClass) || Types.isPrimitive(beanClass) ) {
            if (Number.class.isAssignableFrom(Types.getPrimitiveWrapClass(beanClass))) {
                return new NumberSerializer();
            }
        }
        if (DateSeries.isSupported(beanClass)) {
             return new DateSerializer();
        }
        if(beanClass.isEnum()){
            return new EnumSerializer();
        }

        /*
        if(Reflects.isSubClassOrEquals(Map.Entry.class, beanClass)){
            if(JSONs.hasOtherPropertiesForMapEntry(beanClass)){
                return new MapEntrySubClassSerializer();
            }
        }
         */
        return super.modifySerializer(config, beanDesc, serializer);
    }
}
