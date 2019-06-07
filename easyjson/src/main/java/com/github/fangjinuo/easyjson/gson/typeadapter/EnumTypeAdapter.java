package com.github.fangjinuo.easyjson.gson.typeadapter;

import com.google.gson.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.EnumSet;

/**
 * priority : ordinal() > toString() > field > name()
 */
public class EnumTypeAdapter implements JsonSerializer<Enum>, JsonDeserializer<Enum> {
    private boolean usingValue = false; // using ordinal()
    private boolean usingToString = false;
    private String usingField = null;
    private final boolean usingName = true;

    public boolean isUsingValue() {
        return usingValue;
    }

    public void setUsingValue(boolean usingValue) {
        this.usingValue = usingValue;
    }

    public boolean isUsingToString() {
        return usingToString;
    }

    public void setUsingToString(boolean usingToString) {
        this.usingToString = usingToString;
    }

    public String getUsingField() {
        return usingField;
    }

    public void setUsingField(String usingField) {
        this.usingField = usingField;
    }

    public Enum deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull() || json.isJsonArray() || json.isJsonObject()) {
            return null;
        }
        JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
        EnumSet es = EnumSet.allOf((Class<Enum>) typeOfT);
        if (usingValue && jsonPrimitive.isNumber()) {
            for (Object obj : es) {
                Enum e = (Enum) obj;
                if (e.ordinal() == jsonPrimitive.getAsInt()) {
                    return e;
                }
            }
        }
        if (usingToString && jsonPrimitive.isString()) {
            for (Object obj : es) {
                Enum e = (Enum) obj;
                if (e.toString().equals(jsonPrimitive.getAsString())) {
                    return e;
                }
            }
        }
        if (usingField != null) {
            try {


                Field field = ((Class<Enum>) typeOfT).getDeclaredField(usingField);
                Class fieldType = field.getType();
                if (String.class == fieldType) {
                    for (Object obj : es) {
                        Enum e = (Enum) obj;
                        String v = (String) field.get(e);
                        if (v.equals(jsonPrimitive.getAsString())) {
                            return e;
                        }
                    }
                }
                if (Character.class == fieldType) {
                    for (Object obj : es) {
                        Enum e = (Enum) obj;
                        Character v = (Character) field.get(e);
                        if (v.equals(jsonPrimitive.getAsCharacter())) {
                            return e;
                        }
                    }
                }
                if (Boolean.class == fieldType) {
                    for (Object obj : es) {
                        Enum e = (Enum) obj;
                        Boolean v = (Boolean) field.get(e);
                        if (v.equals(jsonPrimitive.getAsBoolean())) {
                            return e;
                        }
                    }
                }
                if (Number.class == fieldType) {
                    for (Object obj : es) {
                        Enum e = (Enum) obj;
                        Number v = (Number) field.get(e);
                        if (v.equals(jsonPrimitive.getAsNumber())) {
                            return e;
                        }
                    }
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        if (usingName) {
            for (Object obj : es) {
                Enum e = (Enum) obj;
                if (e.name().equals(jsonPrimitive.getAsString())) {
                    return e;
                }
            }
        }
        return null;
    }

    public JsonElement serialize(Enum e, Type typeOfSrc, JsonSerializationContext context) {
        if (usingValue) {
            return new JsonPrimitive(e.ordinal());
        }
        if (usingToString) {
            return new JsonPrimitive(e.toString());
        }
        if (usingField != null) {
            try {
                Field field = e.getClass().getDeclaredField(usingField);
                field.setAccessible(true);
                Class fieldClazz = field.getType();
                if (fieldClazz.isArray() || fieldClazz.isAnonymousClass() || fieldClazz.isAnnotation() || fieldClazz.isSynthetic()) {
                    throw new UnsupportedOperationException();
                }
                if (fieldClazz == String.class) {
                    return new JsonPrimitive(field.get(e).toString());
                }
                if (fieldClazz == Character.class) {
                    return new JsonPrimitive((Character) field.get(e));
                }
                if (fieldClazz == Boolean.class) {
                    return new JsonPrimitive((Boolean) field.get(e));
                }
                if (Number.class.isAssignableFrom(fieldClazz)) {
                    return new JsonPrimitive((Number) field.get(e));
                }
            } catch (NoSuchFieldException e1) {
                // ignore it
            } catch (IllegalAccessException e1) {
                // ignore it
            }
        }
        return new JsonPrimitive(e.name());
    }

}
