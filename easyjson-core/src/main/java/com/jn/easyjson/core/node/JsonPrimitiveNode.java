/*
 * Copyright 2019 the original author or authors.
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

package com.jn.easyjson.core.node;

import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.util.LazilyParsedNumber;
import com.jn.easyjson.core.util.Preconditions;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonPrimitiveNode extends JsonTreeNode {
    private static final Class<?>[] PRIMITIVE_TYPES = {int.class, long.class, short.class,
            float.class, double.class, byte.class, boolean.class, char.class, Integer.class, Long.class,
            Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class};

    private Object value;

    /**
     * Create a primitive containing a boolean value.
     *
     * @param bool the value to create the primitive with.
     */
    public JsonPrimitiveNode(Boolean bool) {
        setValue(bool);
    }

    /**
     * Create a primitive containing a {@link Number}.
     *
     * @param number the value to create the primitive with.
     */
    public JsonPrimitiveNode(Number number) {
        setValue(number);
    }

    /**
     * Create a primitive containing a String value.
     *
     * @param string the value to create the primitive with.
     */
    public JsonPrimitiveNode(String string) {
        setValue(string);
    }

    /**
     * Create a primitive containing a character. The character is turned into a one character String
     * since Json only supports String.
     *
     * @param c the value to create the primitive with.
     */
    public JsonPrimitiveNode(Character c) {
        setValue(c);
    }

    /**
     * Create a primitive using the specified Object. It must be an instance of {@link Number}, a
     * Java primitive type, or a String.
     *
     * @param primitive the value to create the primitive with.
     */
    public JsonPrimitiveNode(Object primitive) {
        setValue(primitive);
    }

    /**
     * Returns the same value as primitives are immutable.
     *
     * @since 2.8.2
     */
    @Override
    public JsonPrimitiveNode deepCopy() {
        return this;
    }

    void setValue(Object primitive) {
        if (primitive instanceof Character) {
            // convert characters to strings since in JSON, characters are represented as a single
            // character string
            char c = ((Character) primitive).charValue();
            this.value = String.valueOf(c);
        } else {
            Preconditions.checkArgument(primitive instanceof Number
                    || isPrimitiveOrString(primitive));
            this.value = primitive;
        }
    }

    /**
     * Check whether this primitive contains a boolean value.
     *
     * @return true if this primitive contains a boolean value, false otherwise.
     */
    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    /**
     * convenience method to get this element as a {@link Boolean}.
     *
     * @return get this element as a {@link Boolean}.
     */
    protected Boolean getAsBooleanWrapper() {
        return (Boolean) value;
    }

    /**
     * convenience method to get this element as a boolean value.
     *
     * @return get this element as a primitive boolean value.
     */
    @Override
    public boolean getAsBoolean() {
        if (isBoolean()) {
            return getAsBooleanWrapper().booleanValue();
        } else {
            // Check to see if the value as a String is "true" in any case.
            return Boolean.parseBoolean(getAsString());
        }
    }

    /**
     * Check whether this primitive contains a Number.
     *
     * @return true if this primitive contains a Number, false otherwise.
     */
    public boolean isNumber() {
        return value instanceof Number;
    }

    /**
     * convenience method to get this element as a Number.
     *
     * @return get this element as a Number.
     * @throws NumberFormatException if the value contained is not a valid Number.
     */
    @Override
    public Number getAsNumber() {
        return value instanceof String ? new LazilyParsedNumber((String) value) : (Number) value;
    }

    /**
     * Check whether this primitive contains a String value.
     *
     * @return true if this primitive contains a String value, false otherwise.
     */
    public boolean isString() {
        return value instanceof String;
    }

    /**
     * convenience method to get this element as a String.
     *
     * @return get this element as a String.
     */
    @Override
    public String getAsString() {
        if (isNumber()) {
            return getAsNumber().toString();
        } else if (isBoolean()) {
            return getAsBooleanWrapper().toString();
        } else if (isChar()) {
            return "" + value;
        } else {
            return (String) value;
        }
    }

    public boolean isDouble() {
        if (isNumber()) {
            Number n = getAsNumber();
            return n.getClass() == Double.class || n.getClass() == double.class;
        }
        return false;
    }

    public boolean isLong() {
        if (isNumber()) {
            Number n = getAsNumber();
            return n.getClass() == Long.class || n.getClass() == long.class;
        }
        return false;
    }

    public boolean isFloat() {
        if (isNumber()) {
            Number n = getAsNumber();
            return n.getClass() == Float.class || n.getClass() == float.class;
        }
        return false;
    }

    public boolean isInteger() {
        if (isNumber()) {
            Number n = getAsNumber();
            return n.getClass() == Integer.class || n.getClass() == int.class;
        }
        return false;
    }

    public boolean isShort() {
        if (isNumber()) {
            Number n = getAsNumber();
            return n.getClass() == Short.class || n.getClass() == short.class;
        }
        return false;
    }

    public boolean isByte() {
        if (isNumber()) {
            Number n = getAsNumber();
            return n.getClass() == Byte.class || n.getClass() == byte.class;
        }
        return false;
    }

    public boolean isBigInteger() {
        if (isNumber()) {
            Number n = getAsNumber();
            return n instanceof BigInteger;
        }
        return false;
    }

    public boolean isChar() {
        return value instanceof Character || value.getClass() == char.class;
    }

    public boolean isBigDecimal() {
        if (isNumber()) {
            Number n = getAsNumber();
            return n instanceof BigDecimal;
        }
        return false;
    }

    public Object getValue() {
        return value;
    }

    /**
     * convenience method to get this element as a primitive double.
     *
     * @return get this element as a primitive double.
     * @throws NumberFormatException if the value contained is not a valid double.
     */
    @Override
    public double getAsDouble() {
        return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
    }

    /**
     * convenience method to get this element as a {@link BigDecimal}.
     *
     * @return get this element as a {@link BigDecimal}.
     * @throws NumberFormatException if the value contained is not a valid {@link BigDecimal}.
     */
    @Override
    public BigDecimal getAsBigDecimal() {
        return value instanceof BigDecimal ? (BigDecimal) value : new BigDecimal(value.toString());
    }

    /**
     * convenience method to get this element as a {@link BigInteger}.
     *
     * @return get this element as a {@link BigInteger}.
     * @throws NumberFormatException if the value contained is not a valid {@link BigInteger}.
     */
    @Override
    public BigInteger getAsBigInteger() {
        return value instanceof BigInteger ?
                (BigInteger) value : new BigInteger(value.toString());
    }

    /**
     * convenience method to get this element as a float.
     *
     * @return get this element as a float.
     * @throws NumberFormatException if the value contained is not a valid float.
     */
    @Override
    public float getAsFloat() {
        return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
    }

    /**
     * convenience method to get this element as a primitive long.
     *
     * @return get this element as a primitive long.
     * @throws NumberFormatException if the value contained is not a valid long.
     */
    @Override
    public long getAsLong() {
        return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
    }

    /**
     * convenience method to get this element as a primitive short.
     *
     * @return get this element as a primitive short.
     * @throws NumberFormatException if the value contained is not a valid short value.
     */
    @Override
    public short getAsShort() {
        return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
    }

    /**
     * convenience method to get this element as a primitive integer.
     *
     * @return get this element as a primitive integer.
     * @throws NumberFormatException if the value contained is not a valid integer.
     */
    @Override
    public int getAsInt() {
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    @Override
    public byte getAsByte() {
        return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
    }

    @Override
    public char getAsCharacter() {
        return getAsString().charAt(0);
    }

    private static boolean isPrimitiveOrString(Object target) {
        if (target instanceof String) {
            return true;
        }

        Class<?> classOfPrimitive = target.getClass();
        for (Class<?> standardPrimitive : PRIMITIVE_TYPES) {
            if (standardPrimitive.isAssignableFrom(classOfPrimitive)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (value == null) {
            return 31;
        }
        // Using recommended hashing algorithm from Effective Java for longs and doubles
        if (isIntegral(this)) {
            long value = getAsNumber().longValue();
            return (int) (value ^ (value >>> 32));
        }
        if (value instanceof Number) {
            long value = Double.doubleToLongBits(getAsNumber().doubleValue());
            return (int) (value ^ (value >>> 32));
        }
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        JsonPrimitiveNode other = (JsonPrimitiveNode) obj;
        if (value == null) {
            return other.value == null;
        }
        if (isIntegral(this) && isIntegral(other)) {
            return getAsNumber().longValue() == other.getAsNumber().longValue();
        }
        if (value instanceof Number && other.value instanceof Number) {
            double a = getAsNumber().doubleValue();
            // Java standard types other than double return true for two NaN. So, need
            // special handling for double.
            double b = other.getAsNumber().doubleValue();
            return a == b || (Double.isNaN(a) && Double.isNaN(b));
        }
        return value.equals(other.value);
    }

    /**
     * Returns true if the specified number is an integral type
     * (Long, Integer, Short, Byte, BigInteger)
     */
    public static boolean isIntegral(JsonPrimitiveNode primitive) {
        if (primitive.value instanceof Number) {
            Number number = (Number) primitive.value;
            return number instanceof BigInteger || number instanceof Long || number instanceof Integer
                    || number instanceof Short || number instanceof Byte;
        }
        return false;
    }
}
