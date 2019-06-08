package com.github.fangjinuo.easyjson.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.util.CompactStringObjectMap;
import com.fasterxml.jackson.databind.util.EnumResolver;

import java.io.IOException;

public class EnumDeserializer extends com.fasterxml.jackson.databind.deser.std.EnumDeserializer{

    public static final String READ_ENUM_USING_INDEX_ATTR_KEY="READ_ENUM_USING_INDEX";

    private Enum<?> _enumDefaultValue;

    public EnumDeserializer(EnumResolver byNameResolver, Boolean caseInsensitive) {
        super(byNameResolver, caseInsensitive);
        _enumDefaultValue = byNameResolver.getDefaultValue();
    }

    public EnumDeserializer(com.fasterxml.jackson.databind.deser.std.EnumDeserializer base, Boolean caseInsensitive) {
        super(base, caseInsensitive);
        _enumDefaultValue = ((EnumDeserializer)base)._enumDefaultValue;
    }

    public EnumDeserializer(EnumResolver byNameResolver) {
        super(byNameResolver);
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt, Object intoValue) throws IOException {
        JsonToken curr = p.getCurrentToken();

        // Usually should just get string value:
        if (curr == JsonToken.VALUE_STRING || curr == JsonToken.FIELD_NAME) {
            CompactStringObjectMap lookup = ctxt.isEnabled(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
                    ? _getToStringLookup(ctxt) : _lookupByName;
            final String name = p.getText();
            Object result = lookup.find(name);
            if (result == null) {
                return _deserializeAltString(p, ctxt, lookup, name);
            }
            return result;
        }
        // But let's consider int acceptable as well (if within ordinal range)
        if (curr == JsonToken.VALUE_NUMBER_INT) {
            // ... unless told not to do that
            int index = p.getIntValue();
            if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)) {
                return ctxt.handleWeirdNumberValue(_enumClass(), index,
                        "not allowed to deserialize Enum value out of number: disable DeserializationConfig.DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS to allow"
                );
            }
            if (index >= 0 && index < _enumsByIndex.length) {
                return _enumsByIndex[index];
            }
            if ((_enumDefaultValue != null)
                    && ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)) {
                return _enumDefaultValue;
            }
            if (!ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
                return ctxt.handleWeirdNumberValue(_enumClass(), index,
                        "index value outside legal index range [0..%s]",
                        _enumsByIndex.length-1);
            }
            return null;
        }
        return _deserializeOther(p, ctxt);
    }

    protected Object _deserializeAltString(JsonParser p, DeserializationContext ctxt,
                                               CompactStringObjectMap lookup, String name) throws IOException
    {
        name = name.trim();
        if (name.length() == 0) {
            if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
                return getEmptyValue(ctxt);
            }
        } else {
            // [databind#1313]: Case insensitive enum deserialization
            if (Boolean.TRUE.equals(_caseInsensitive)) {
                Object match = lookup.findCaseInsensitive(name);
                if (match != null) {
                    return match;
                }
            } else if (!ctxt.isEnabled(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)) {
                // [databind#149]: Allow use of 'String' indexes as well -- unless prohibited (as per above)
                char c = name.charAt(0);
                if (c >= '0' && c <= '9') {
                    try {
                        int index = Integer.parseInt(name);
                        if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
                            return ctxt.handleWeirdStringValue(_enumClass(), name,
                                    "value looks like quoted Enum index, but `MapperFeature.ALLOW_COERCION_OF_SCALARS` prevents use"
                            );
                        }
                        if (index >= 0 && index < _enumsByIndex.length) {
                            return _enumsByIndex[index];
                        }
                    } catch (NumberFormatException e) {
                        // fine, ignore, was not an integer
                    }
                }
            }
        }
        if ((_enumDefaultValue != null)
                && ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)) {
            return _enumDefaultValue;
        }
        if (!ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
            return ctxt.handleWeirdStringValue(_enumClass(), name,
                    "value not one of declared Enum instance names: %s", lookup.keys());
        }
        return null;
    }
}
