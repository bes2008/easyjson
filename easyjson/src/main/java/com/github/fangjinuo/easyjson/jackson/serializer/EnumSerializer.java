package com.github.fangjinuo.easyjson.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.lang.reflect.Field;

public class EnumSerializer<T extends Enum> extends JsonSerializer<T> {
    public static final String WRITE_ENUM_USING_FIELD_ATTR_KEY = "WRITE_ENUM_USING_FIELD";

    @Override
    public void serialize(T e, JsonGenerator gen, SerializerProvider sp) throws IOException {
        boolean usingIndex = sp.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        boolean usingToString = sp.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        String usingField = (String) sp.getAttribute(WRITE_ENUM_USING_FIELD_ATTR_KEY);

        if (usingIndex) {
            gen.writeNumber(e.ordinal());
            return;
        }

        if (usingToString) {
            gen.writeString(e.toString());
            return;
        }

        if (usingField != null && !usingField.trim().isEmpty()) {
            usingField = usingField.trim();
            try {
                Field field = e.getClass().getDeclaredField(usingField);
                field.setAccessible(true);
                Class fieldClazz = field.getType();
                if (fieldClazz.isArray() || fieldClazz.isAnonymousClass() || fieldClazz.isAnnotation() || fieldClazz.isSynthetic()) {
                    throw new UnsupportedOperationException();
                }
                if (fieldClazz == String.class) {
                    gen.writeString(field.get(e).toString());
                    return;
                }
                if (fieldClazz == Character.class) {
                    gen.writeRaw((Character) field.get(e));
                    return;
                }
                if (fieldClazz == Boolean.class) {
                    gen.writeBoolean((Boolean) field.get(e));
                    return;
                }
                if (Number.class.isAssignableFrom(fieldClazz)) {
                    gen.writeNumber(((Number) field.get(e)).toString());
                    return;
                }
            } catch (NoSuchFieldException e1) {
                // ignore it
            } catch (IllegalAccessException e1) {
                // ignore it
            }
        }
        gen.writeString(e.name());
    }

}
