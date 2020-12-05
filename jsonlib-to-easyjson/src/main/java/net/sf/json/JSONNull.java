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

package net.sf.json;

import java.io.IOException;
import java.io.Writer;

/**
 * JSONNull is equivalent to the value that JavaScript calls null, whilst Java's
 * null is equivalent to the value that JavaScript calls undefined.
 *
 * @author JSON.org
 */
public final class JSONNull implements JSON {
    /**
     * singleton instance
     */
    private static JSONNull instance;

    static {
        instance = new JSONNull();
    }

    /**
     * Returns the singleton instance of JSONNull
     */
    public static JSONNull getInstance() {
        return instance;
    }

    private JSONNull() {

    }

    /**
     * A Null object is equal to the null value and to itself.
     *
     * @param object An object to test for nullness.
     * @return true if the object parameter is the JSONObject.NULL object or
     * null.
     */
    @Override
    public boolean equals(Object object) {
        return object == null || object == this || object == instance
                || (object instanceof JSONObject && ((JSONObject) object).isNullObject())
                || "null".equals(object);
    }

    @Override
    public int hashCode() {
        return 37 + "null".hashCode();
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        throw new JSONException("Object is null");
    }

    @Override
    public int size() {
        throw new JSONException("Object is null");
    }

    /**
     * Get the "null" string value.
     *
     * @return The string "null".
     */
    @Override
    public String toString() {
        return "null";
    }

    @Override
    public String toString(int indentFactor) {
        return toString();
    }

    @Override
    public String toString(int indentFactor, int indent) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < indent; i += 1) {
            sb.append(' ');
        }
        sb.append(toString());
        return sb.toString();
    }

    @Override
    public Writer write(Writer writer) {
        try {
            writer.write(toString());
            return writer;
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }
}