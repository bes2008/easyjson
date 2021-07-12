package com.jn.easyjson.fastjson.codec;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.jn.langx.util.Strings;
import com.jn.langx.util.enums.Enums;
import com.jn.langx.util.function.Supplier0;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.Primitives;
import com.jn.langx.util.reflect.type.Types;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EnumCodec implements ObjectSerializer, ObjectDeserializer, Typed {
    private boolean serialUseIndex;
    private boolean serialUseToString;
    private String serialUseField;

    public boolean isSerialUseIndex() {
        return serialUseIndex;
    }

    public void setSerialUseIndex(boolean serialUseIndex) {
        this.serialUseIndex = serialUseIndex;
    }

    public boolean isSerialUseToString() {
        return serialUseToString;
    }

    public void setSerialUseToString(boolean serialUseToString) {
        this.serialUseToString = serialUseToString;
    }

    public String getSerialUseField() {
        return serialUseField;
    }

    public void setSerialUseField(String serialUseField) {
        this.serialUseField = serialUseField;
    }


    @Override
    public <T> T deserialze(DefaultJSONParser parser, final Type type, Object fieldName) {
        final JSONLexer lexer = parser.lexer;
        final int token = lexer.token();
        if (token == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }
        if ((serialUseToString) && token == JSONToken.LITERAL_STRING) {
            String stringValue = lexer.stringVal();
            return (T) Enums.ofToString((Class<? extends Enum>) type, stringValue);
        } else if (serialUseIndex && token == JSONToken.LITERAL_INT) {
            int intValue = lexer.intValue();
            return (T) Enums.ofValue(intValue, (Class<? extends Enum>) type);
        } else if (Strings.isNotEmpty(serialUseField)) {
            return (T) Enums.ofField((Class<? extends Enum>) type, serialUseField, new Supplier0<Object>() {
                @Override
                public Object get() {
                    Field field = Reflects.getDeclaredField(Types.toClass(type), serialUseField);
                    if (field != null) {
                        field.setAccessible(true);
                        Class fieldClazz  = field.getType();

                        if (fieldClazz == String.class) {
                            return lexer.stringVal();
                        } else if (Primitives.isInteger(fieldClazz)) {
                            return lexer.integerValue();
                        } else if (Primitives.isShort(fieldClazz)) {
                            return (short) lexer.intValue();
                        } else if (Primitives.isLong(fieldClazz)) {
                            return lexer.longValue();
                        } else if (Primitives.isDouble(fieldClazz)) {
                            return lexer.decimalValue();
                        }
                        return null;

                    }
                    return null;
                }
            });
        }
        if (token == JSONToken.LITERAL_INT) {
            int intValue = lexer.intValue();
            return (T) Enums.ofCode((Class<? extends Enum>) type, intValue);
        } else if (token == JSONToken.LITERAL_STRING) {
            String stringValue = lexer.stringVal();
            return (T) Enums.ofName((Class<? extends Enum>) type, stringValue);
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
        } else if (serialUseIndex) {
            out.writeInt(Enums.getIndex((Enum) object));
        } else if (serialUseToString) {
            out.writeString(object.toString());
        } else if (Strings.isNotBlank(serialUseField)) {
            out.write(Reflects.<char[]>getAnyFieldValue(object, serialUseField, true, true));
        } else {
            out.writeString(Enums.getName((Enum) object));
        }
    }

    @Override
    public List<Type> applyTo() {
        List<Type> types = new ArrayList<Type>();
        types.add(Enum.class);
        return types;
    }
}
