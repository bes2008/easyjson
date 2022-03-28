/*
 * Copyright 2019 the original author or authors.
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

package com.jn.easyjson.jackson.deserializer;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.jn.easyjson.core.codec.dialect.PropertyCodecConfiguration;
import com.jn.easyjson.jackson.Jacksons;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.enums.Enums;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Supplier0;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static com.jn.easyjson.jackson.JacksonConstants.*;

public class EnumDeserializer<T extends Enum> extends JsonDeserializer<T> implements ContextualDeserializer {
    private static final Logger logger = LoggerFactory.getLogger(EnumDeserializer.class);
    private Class<T> clazz;

    @Override
    public Class<T> handledType() {
        return clazz;
    }

    public EnumDeserializer() {
    }

    public EnumDeserializer(Class clazz) {
        this.clazz = clazz;
    }


    @Override
    public T deserialize(final JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {

        Boolean usingIndex = null;
        Boolean usingToString = null;
        String usingField = null;
        if (Jacksons.getBooleanAttr(ctx, ENABLE_CUSTOM_CONFIGURATION)) {
            PropertyCodecConfiguration propertyCodecConfiguration = Jacksons.getPropertyCodecConfiguration(p);
            if (propertyCodecConfiguration != null) {
                usingIndex = propertyCodecConfiguration.getEnumUsingIndex();
                usingToString = propertyCodecConfiguration.getEnumUsingToString();
            }
        }
        if (usingIndex == null) {
            usingIndex = Jacksons.getBooleanAttr(ctx, SERIALIZE_ENUM_USING_INDEX_ATTR_KEY);
        }
        if (usingToString == null) {
            usingToString = ctx.isEnabled(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        }
        if (Strings.isEmpty(usingField)) {
            usingField = (String) ctx.getAttribute(SERIALIZE_ENUM_USING_FIELD_ATTR_KEY);
        }
        Class<T> enumClass = clazz;
        if (enumClass == null) {
            Object currentOwner = p.getCurrentValue();
            String currentName = p.getCurrentName();

            // enum is bean's field
            if (currentName != null && currentOwner != null) {
                try {
                    Field field = currentOwner.getClass().getDeclaredField(currentName);
                    enumClass = (Class<T>) field.getType();
                } catch (NoSuchFieldException e) {
                    throw Throwables.wrapAsRuntimeException(e);
                }
            }
        }

        final JsonToken jtoken = p.getCurrentToken();

        if (usingIndex && jtoken == JsonToken.VALUE_NUMBER_INT) {
            int index = p.getIntValue();
            return (T) Enums.ofCode(enumClass, index);
        }

        if (jtoken == JsonToken.VALUE_STRING) {
            String string = p.getValueAsString();
            if (usingToString) {
                return (T) Enums.ofToString(enumClass, string);
            }
            // name
            T t = Enums.ofName(enumClass, string);
            if (t == null) {
                t = Enums.ofField(enumClass, "name", string);
            }
            if (t == null) {
                t = Enums.ofToString(enumClass, string);
            }
            if (t == null) {
                Collection<Field> fields = Reflects.getAllDeclaredFields(enumClass, false);
                Field field = Pipeline.of(fields)
                        .findFirst(new Predicate<Field>() {
                            @Override
                            public boolean test(Field field) {
                                return Reflects.hasAnnotation(field, JsonValue.class);
                            }
                        });
                if (field != null) {
                    usingField = field.getName();
                }
            }
        }


        if (Strings.isNotEmpty(usingField)) {
            try {
                Field field = enumClass.getDeclaredField(usingField);
                final Class fieldType = field.getType();
                return (T) Enums.ofField(enumClass, usingField, new Supplier0<Object>() {
                    @Override
                    public Object get() {
                        try {
                            if (String.class == fieldType && jtoken == JsonToken.VALUE_STRING) {
                                return p.getValueAsString();
                            }
                            if (Character.class == fieldType) {
                                return p.getTextCharacters()[0];

                            }
                            if (Boolean.class == fieldType) {
                                return p.getBooleanValue();
                            }
                            if (Number.class == fieldType) {
                                return p.getNumberValue();
                            }
                        } catch (Throwable ex) {
                            logger.error(ex.getMessage(), ex);
                        }
                        return null;
                    }
                });
            } catch (Throwable ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        if (jtoken == JsonToken.VALUE_STRING) {
            String enumName = p.getValueAsString();
            return (T) Enums.ofName(enumClass, enumName);
        }
        return null;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty beanProperty, Class<?> type) throws JsonMappingException {
        if (handledType() == null || (type != null && handledType() != type)) {
            EnumDeserializer enumDeserializer = new EnumDeserializer();
            enumDeserializer.clazz = type;
            return enumDeserializer;
        }
        return this;
    }
}
