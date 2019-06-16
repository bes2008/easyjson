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

package com.github.fangjinuo.easyjson.jackson.ext;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.github.fangjinuo.easyjson.api.util.type.Types;
import com.github.fangjinuo.easyjson.jackson.serializer.DateSerializer;
import com.github.fangjinuo.easyjson.jackson.serializer.NumberSerializer;

import java.util.Date;

public class EasyJsonBeanSerializerFactory extends BeanSerializerFactory {
    public static EasyJsonBeanSerializerFactory instance = new EasyJsonBeanSerializerFactory(null);

    public EasyJsonBeanSerializerFactory(SerializerFactoryConfig config) {
        super(config);
    }

    public SerializerFactory withConfig(SerializerFactoryConfig config) {
        if (_factoryConfig == config) {
            return this;
        }
        if (getClass() != EasyJsonBeanSerializerFactory.class) {
            throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName()
                    + ") has not properly overridden method 'withAdditionalSerializers': cannot instantiate subtype with "
                    + "additional serializer definitions");
        }
        return new EasyJsonBeanSerializerFactory(config);
    }

    protected JsonSerializer<?> _createSerializer2(SerializerProvider prov,
                                                   JavaType type, BeanDescription beanDesc, boolean staticTyping)
            throws JsonMappingException {
        Class<?> rawType = type.getRawClass();
        String clsName = rawType.getName();
        JsonSerializer<?> ser = null;
        if (Types.isPrimitive(rawType) || clsName.startsWith("java.")) {
            if (Number.class.isAssignableFrom(Types.getPrimitiveWrapClass(rawType))) {
                ser = new NumberSerializer().createContextual(prov, null, type);
                if (ser != null) {
                    return ser;
                }
            }
        }
        if (Date.class.isAssignableFrom(rawType)) {
            ser = new DateSerializer().createContextual(prov, null, type);
            if (ser != null) {
                return ser;
            }
        }
        return super._createSerializer2(prov, type, beanDesc, staticTyping);
    }
}
