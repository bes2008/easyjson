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

package com.jn.easyjson.core;

import com.jn.easyjson.core.node.JsonArrayNode;
import com.jn.easyjson.core.node.JsonNullNode;
import com.jn.easyjson.core.node.JsonObjectNode;
import com.jn.easyjson.core.node.JsonPrimitiveNode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;

public abstract class JsonTreeNode {
    /**
     * Returns a deep copy of this element. Immutable elements like primitives
     * and nulls are not copied.
     *
     * @since 2.8.2
     */
    public abstract JsonTreeNode deepCopy();

    /**
     * provides check for verifying if this element is an array or not.
     *
     * @return true if this element is of type {@link JsonArrayNode}, false otherwise.
     */
    public boolean isJsonArrayNode() {
        return this instanceof JsonArrayNode;
    }

    /**
     * provides check for verifying if this element is a Json object or not.
     *
     * @return true if this element is of type {@link JsonObjectNode}, false otherwise.
     */
    public boolean isJsonObjectNode() {
        return this instanceof JsonObjectNode;
    }

    /**
     * provides check for verifying if this element is a primitive or not.
     *
     * @return true if this element is of type {@link JsonPrimitiveNode}, false otherwise.
     */
    public boolean isJsonPrimitiveNode() {
        return this instanceof JsonPrimitiveNode;
    }

    /**
     * provides check for verifying if this element represents a null value or not.
     *
     * @return true if this element is of type {@link JsonNullNode}, false otherwise.
     * @since 1.2
     */
    public boolean isJsonNullNode() {
        return this instanceof JsonNullNode;
    }

    /**
     * convenience method to get this element as a {@link JsonObjectNode}. If the element is of some
     * other type, a {@link IllegalStateException} will result. Hence it is best to use this method
     * after ensuring that this element is of the desired type by calling {@link #isJsonObjectNode()}
     * first.
     *
     * @return get this element as a {@link JsonObjectNode}.
     * @throws IllegalStateException if the element is of another type.
     */
    public JsonObjectNode getAsJsonObjectNode() {
        if (isJsonObjectNode()) {
            return (JsonObjectNode) this;
        }
        throw new IllegalStateException("Not a JSON Object: " + this);
    }

    /**
     * convenience method to get this element as a {@link JsonArrayNode}. If the element is of some
     * other type, a {@link IllegalStateException} will result. Hence it is best to use this method
     * after ensuring that this element is of the desired type by calling {@link #isJsonArrayNode()}
     * first.
     *
     * @return get this element as a {@link JsonArrayNode}.
     * @throws IllegalStateException if the element is of another type.
     */
    public JsonArrayNode getAsJsonArrayNode() {
        if (isJsonArrayNode()) {
            return (JsonArrayNode) this;
        }
        throw new IllegalStateException("Not a JSON Array: " + this);
    }

    /**
     * convenience method to get this element as a {@link JsonPrimitiveNode}. If the element is of some
     * other type, a {@link IllegalStateException} will result. Hence it is best to use this method
     * after ensuring that this element is of the desired type by calling {@link #isJsonPrimitiveNode()}
     * first.
     *
     * @return get this element as a {@link JsonPrimitiveNode}.
     * @throws IllegalStateException if the element is of another type.
     */
    public JsonPrimitiveNode getAsJsonPrimitiveNode() {
        if (isJsonPrimitiveNode()) {
            return (JsonPrimitiveNode) this;
        }
        throw new IllegalStateException("Not a JSON Primitive: " + this);
    }

    /**
     * convenience method to get this element as a {@link JsonNullNode}. If the element is of some
     * other type, a {@link IllegalStateException} will result. Hence it is best to use this method
     * after ensuring that this element is of the desired type by calling {@link #isJsonNullNode()}
     * first.
     *
     * @return get this element as a {@link JsonNullNode}.
     * @throws IllegalStateException if the element is of another type.
     * @since 1.2
     */
    public JsonNullNode getAsJsonNullNode() {
        if (isJsonNullNode()) {
            return (JsonNullNode) this;
        }
        throw new IllegalStateException("Not a JSON Null: " + this);
    }

    /**
     * convenience method to get this element as a boolean value.
     *
     * @return get this element as a primitive boolean value.
     * @throws ClassCastException    if the element is of not a {@link JsonPrimitiveNode} and is not a valid
     *                               boolean value.
     * @throws IllegalStateException if the element is of the type {@link JsonArrayNode} but contains
     *                               more than a single element.
     */
    public boolean getAsBoolean() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
     * convenience method to get this element as a {@link Boolean} value.
     *
     * @return get this element as a {@link Boolean} value.
     * @throws ClassCastException    if the element is of not a {@link JsonPrimitiveNode} and is not a valid
     *                               boolean value.
     * @throws IllegalStateException if the element is of the type {@link JsonArrayNode} but contains
     *                               more than a single element.
     */
    Boolean getAsBooleanWrapper() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
     * convenience method to get this element as a {@link Number}.
     *
     * @return get this element as a {@link Number}.
     * @throws ClassCastException    if the element is of not a {@link JsonPrimitiveNode} and is not a valid
     *                               number.
     * @throws IllegalStateException if the element is of the type {@link JsonArrayNode} but contains
     *                               more than a single element.
     */
    public Number getAsNumber() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
     * convenience method to get this element as a string value.
     *
     * @return get this element as a string value.
     * @throws ClassCastException    if the element is of not a {@link JsonPrimitiveNode} and is not a valid
     *                               string value.
     * @throws IllegalStateException if the element is of the type {@link JsonArrayNode} but contains
     *                               more than a single element.
     */
    public String getAsString() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
     * convenience method to get this element as a primitive double value.
     *
     * @return get this element as a primitive double value.
     * @throws ClassCastException    if the element is of not a {@link JsonPrimitiveNode} and is not a valid
     *                               double value.
     * @throws IllegalStateException if the element is of the type {@link JsonArrayNode} but contains
     *                               more than a single element.
     */
    public double getAsDouble() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
     * convenience method to get this element as a primitive float value.
     *
     * @return get this element as a primitive float value.
     * @throws ClassCastException    if the element is of not a {@link JsonPrimitiveNode} and is not a valid
     *                               float value.
     * @throws IllegalStateException if the element is of the type {@link JsonArrayNode} but contains
     *                               more than a single element.
     */
    public float getAsFloat() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
     * convenience method to get this element as a primitive long value.
     *
     * @return get this element as a primitive long value.
     * @throws ClassCastException    if the element is of not a {@link JsonPrimitiveNode} and is not a valid
     *                               long value.
     * @throws IllegalStateException if the element is of the type {@link JsonArrayNode} but contains
     *                               more than a single element.
     */
    public long getAsLong() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
     * convenience method to get this element as a primitive integer value.
     *
     * @return get this element as a primitive integer value.
     * @throws ClassCastException    if the element is of not a {@link JsonPrimitiveNode} and is not a valid
     *                               integer value.
     * @throws IllegalStateException if the element is of the type {@link JsonArrayNode} but contains
     *                               more than a single element.
     */
    public int getAsInt() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
     * convenience method to get this element as a primitive byte value.
     *
     * @return get this element as a primitive byte value.
     * @throws ClassCastException    if the element is of not a {@link JsonPrimitiveNode} and is not a valid
     *                               byte value.
     * @throws IllegalStateException if the element is of the type {@link JsonArrayNode} but contains
     *                               more than a single element.
     * @since 1.3
     */
    public byte getAsByte() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
     * convenience method to get this element as a primitive character value.
     *
     * @return get this element as a primitive char value.
     * @throws ClassCastException    if the element is of not a {@link JsonPrimitiveNode} and is not a valid
     *                               char value.
     * @throws IllegalStateException if the element is of the type {@link JsonArrayNode} but contains
     *                               more than a single element.
     * @since 1.3
     */
    public char getAsCharacter() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
     * convenience method to get this element as a {@link BigDecimal}.
     *
     * @return get this element as a {@link BigDecimal}.
     * @throws ClassCastException    if the element is of not a {@link JsonPrimitiveNode}.
     *                               * @throws NumberFormatException if the element is not a valid {@link BigDecimal}.
     * @throws IllegalStateException if the element is of the type {@link JsonArrayNode} but contains
     *                               more than a single element.
     * @since 1.2
     */
    public BigDecimal getAsBigDecimal() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
     * convenience method to get this element as a {@link BigInteger}.
     *
     * @return get this element as a {@link BigInteger}.
     * @throws ClassCastException    if the element is of not a {@link JsonPrimitiveNode}.
     * @throws NumberFormatException if the element is not a valid {@link BigInteger}.
     * @throws IllegalStateException if the element is of the type {@link JsonArrayNode} but contains
     *                               more than a single element.
     * @since 1.2
     */
    public BigInteger getAsBigInteger() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
     * convenience method to get this element as a primitive short value.
     *
     * @return get this element as a primitive short value.
     * @throws ClassCastException    if the element is of not a {@link JsonPrimitiveNode} and is not a valid
     *                               short value.
     * @throws IllegalStateException if the element is of the type {@link JsonArrayNode} but contains
     *                               more than a single element.
     */
    public short getAsShort() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
     * Returns a String representation of this node.
     */
    @Override
    public String toString() {
        if (isJsonNullNode()) {
            return null;
        }
        if (isJsonPrimitiveNode()) {
            JsonPrimitiveNode primitiveNode = getAsJsonPrimitiveNode();
            if (primitiveNode.isNumber()) {
                return this.getAsNumber()+"";
            }
            if (primitiveNode.isString()) {
                return "\""+this.getAsString()+"\"";
            }
            if (primitiveNode.isBoolean()) {
                return this.getAsBoolean()+"";
            }
            return primitiveNode.getAsString();
        }
        if (isJsonArrayNode()) {
            JsonArrayNode arrayNode = getAsJsonArrayNode();
            StringBuilder b = new StringBuilder();
            b.append("[");
            Iterator<JsonTreeNode> iter = arrayNode.iterator();
            while (iter.hasNext()) {
                JsonTreeNode element = iter.next();
                b.append(element.toString());
                if (iter.hasNext()) {
                    b.append(", ");
                }
            }
            b.append("]");
            return b.toString();
        }
        if (isJsonObjectNode()) {
            JsonObjectNode jsonObjectNode = getAsJsonObjectNode();
            StringBuilder b = new StringBuilder();
            b.append("{");
            Iterator<Map.Entry<String, JsonTreeNode>> iter = jsonObjectNode.propertySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, JsonTreeNode> property = iter.next();
                b.append("\"").append(property.getKey()).append("\": "); // key
                b.append(property.getValue().toString());
                if (iter.hasNext()) {
                    b.append(", ");
                }
            }
            b.append("}");
            return b.toString();
        }
        return JSONBuilderProvider.simplest().toJson(this);
    }
}
