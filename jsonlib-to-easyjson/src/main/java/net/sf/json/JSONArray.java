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

package net.sf.json;


import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.util.type.Primitives;
import net.sf.json.util.JSONUtils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;


/**
 * A JSONArray is an ordered sequence of values. Its external text form is a
 * string wrapped in square brackets with commas separating the values. The
 * internal form is an object having <code>get</code> and <code>opt</code>
 * methods for accessing the values by index, and <code>element</code> methods
 * for adding or replacing values. The values can be any of these types:
 * <code>Boolean</code>, <code>JSONArray</code>, <code>JSONObject</code>,
 * <code>Number</code>, <code>String</code>, or the
 * <code>JSONNull object</code>.
 * <p>
 * The constructor can convert a JSON text into a Java object. The
 * <code>toString</code> method converts to JSON text.
 * <p>
 * A <code>get</code> method returns a value if one can be found, and throws
 * an exception if one cannot be found. An <code>opt</code> method returns a
 * default value instead of throwing an exception, and so is useful for
 * obtaining optional values.
 * <p>
 * The generic <code>get()</code> and <code>opt()</code> methods return an
 * object which you can cast or query for type. There are also typed
 * <code>get</code> and <code>opt</code> methods that do type checking and
 * type coersion for you.
 * <p>
 * The texts produced by the <code>toString</code> methods strictly conform to
 * JSON syntax rules. The constructors are more forgiving in the texts they will
 * accept:
 * <ul>
 * <li>An extra <code>,</code>&nbsp;<small>(comma)</small> may appear just
 * before the closing bracket.</li>
 * <li>The <code>null</code> value will be inserted when there is
 * <code>,</code>&nbsp;<small>(comma)</small> elision.</li>
 * <li>Strings may be quoted with <code>'</code>&nbsp;<small>(single quote)</small>.</li>
 * <li>Strings do not need to be quoted at all if they do not begin with a
 * quote or single quote, and if they do not contain leading or trailing spaces,
 * and if they do not contain any of these characters:
 * <code>{ } [ ] / \ : , = ; #</code> and if they do not look like numbers and
 * if they are not the reserved words <code>true</code>, <code>false</code>,
 * or <code>null</code>.</li>
 * <li>Values can be separated by <code>;</code> <small>(semicolon)</small>
 * as well as by <code>,</code> <small>(comma)</small>.</li>
 * <li>Numbers may have the <code>0-</code> <small>(octal)</small> or
 * <code>0x-</code> <small>(hex)</small> prefix.</li>
 * <li>Comments written in the slashshlash, slashstar, and hash conventions
 * will be ignored.</li>
 * </ul>
 *
 * @author JSON.org
 */
public final class JSONArray extends AbstractJSON implements JSON, List, Comparable {
    /**
     * Creates a JSONArray.<br>
     * Inspects the object type to call the correct JSONArray factory method.
     * Accepts JSON formatted strings, arrays, Collections and Enums.
     *
     * @param object
     * @throws JSONException if the object can not be converted to a proper
     *                       JSONArray.
     */
    public static JSONArray fromObject(Object object) {
        return fromObject(object, new JsonConfig());
    }

    /**
     * Creates a JSONArray.<br>
     * Inspects the object type to call the correct JSONArray factory method.
     * Accepts JSON formatted strings, arrays, Collections and Enums.
     *
     * @param object
     * @throws JSONException if the object can not be converted to a proper
     *                       JSONArray.
     */
    public static JSONArray fromObject(Object object, JsonConfig jsonConfig) {
        Object jsonobj = JsonMapper.fromJavaObject(object, jsonConfig);
        JSONArray jsonArray = new JSONArray();
        if (jsonobj != JSONNull.getInstance()) {
            if (Primitives.isPrimitive(jsonobj.getClass())) {
                jsonArray.element(jsonobj);
            }
            if (jsonobj instanceof JSONArray) {
                return (JSONArray) jsonobj;
            }
            if (jsonobj instanceof JSONObject) {
                jsonArray.element(jsonobj);
            }
        }

        return jsonArray;
    }

    /**
     * Get the collection type from a getter or setter, or null if no type was
     * found.<br/>
     * Contributed by [Matt Small @ WaveMaker].
     */
    public static Class[] getCollectionType(PropertyDescriptor pd, boolean useGetter)
            throws JSONException {

        Type type;
        if (useGetter) {
            Method m = pd.getReadMethod();
            type = m.getGenericReturnType();
        } else {
            Method m = pd.getWriteMethod();
            Type[] gpts = m.getGenericParameterTypes();

            if (1 != gpts.length) {
                throw new JSONException("method " + m + " is not a standard setter");
            }
            type = gpts[0];
        }

        if (!(type instanceof ParameterizedType)) {
            return null;
            // throw new JSONException("type not instanceof ParameterizedType:
            // "+type.getClass());
        }

        ParameterizedType pType = (ParameterizedType) type;
        Type[] actualTypes = pType.getActualTypeArguments();

        Class[] ret = new Class[actualTypes.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (Class) actualTypes[i];
        }

        return ret;
    }

    /**
     * Returns the number of dimensions suited for a java array.
     */
    public static int[] getDimensions(JSONArray jsonArray) {
        // short circuit for empty arrays
        if (jsonArray == null || jsonArray.isEmpty()) {
            return new int[]{0};
        }

        List dims = new ArrayList();
        processArrayDimensions(jsonArray, dims, 0);
        int[] dimensions = new int[dims.size()];
        int j = 0;
        for (Iterator i = dims.iterator(); i.hasNext(); ) {
            dimensions[j++] = ((Integer) i.next()).intValue();
        }
        return dimensions;
    }

    /**
     * Creates a java array from a JSONArray.
     */
    public static Object toArray(JSONArray jsonArray) {
        return toArray(jsonArray, new JsonConfig());
    }

    /**
     * Creates a java array from a JSONArray.
     */
    public static Object toArray(JSONArray jsonArray, Class objectClass) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        return toArray(jsonArray, jsonConfig);
    }

    /**
     * Creates a java array from a JSONArray.<br>
     * Any attribute is a JSONObject and matches a key in the classMap, it will
     * be converted to that target class.<br>
     * The classMap has the following conventions:
     * <ul>
     * <li>Every key must be an String.</li>
     * <li>Every value must be a Class.</li>
     * <li>A key may be a regular expression.</li>
     * </ul>
     */
    public static Object toArray(JSONArray jsonArray, Class objectClass, Map classMap) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        jsonConfig.setClassMap(classMap);
        return toArray(jsonArray, jsonConfig);
    }

    /**
     * Creates a java array from a JSONArray.<br>
     */
    public static Object toArray(JSONArray jsonArray, JsonConfig jsonConfig) {
        List list = toList(jsonArray, jsonConfig);
        return list.toArray(new Object[list.size()]);
    }

    /**
     * Creates a java array from a JSONArray.<br>
     */
    public static Object toArray(JSONArray jsonArray, Object root, JsonConfig jsonConfig) {
        Class clazz = jsonConfig.getRootClass();
        if (root != null && root.getClass() != clazz) {
            jsonConfig.setRootClass(root.getClass());
        }
        return toArray(jsonArray, jsonConfig);
    }

    /**
     * Returns a List or a Set taking generics into account.<br/>
     */
    public static Collection toCollection(JSONArray jsonArray) {
        return toCollection(jsonArray, new JsonConfig());
    }

    /**
     * Returns a List or a Set taking generics into account.<br/>
     */
    public static Collection toCollection(JSONArray jsonArray, Class objectClass) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        return toCollection(jsonArray, jsonConfig);
    }

    /**
     * Returns a List or a Set taking generics into account.<br/>
     * Contributed by [Matt Small @ WaveMaker].
     */
    public static Collection toCollection(JSONArray jsonArray, JsonConfig jsonConfig) {
        com.jn.easyjson.core.JSON json = JsonMapper.buildJSON(jsonConfig);
        if (jsonConfig.getRootClass() != null) {
            return json.fromJsonTreeNode(JsonMapper.toJsonTreeNode(jsonArray), jsonConfig.getRootClass());
        } else {
            return (Collection) JsonMapper.toJavaObject(jsonArray, jsonConfig);
        }
    }

    public static Collection toCollection(JSONArray jsonArray, Object root, JsonConfig jsonConfig) {
        Class clazz = jsonConfig.getRootClass();
        if (root != null && root.getClass() != clazz) {
            jsonConfig.setRootClass(root.getClass());
        }
        return toCollection(jsonArray, jsonConfig);
    }

    /**
     * Creates a List from a JSONArray.<br>
     *
     * @see #toCollection(JSONArray)
     * @deprecated replaced by toCollection
     */
    public static List toList(JSONArray jsonArray) {
        return toList(jsonArray, new JsonConfig());
    }

    /**
     * Creates a List from a JSONArray.
     *
     * @see #toCollection(JSONArray, Class)
     * @deprecated replaced by toCollection
     */
    public static List toList(JSONArray jsonArray, Class objectClass) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        return toList(jsonArray, jsonConfig);
    }

    /**
     * Creates a List from a JSONArray.<br>
     * Any attribute is a JSONObject and matches a key in the classMap, it will
     * be converted to that target class.<br>
     * The classMap has the following conventions:
     * <ul>
     * <li>Every key must be an String.</li>
     * <li>Every value must be a Class.</li>
     * <li>A key may be a regular expression.</li>
     * </ul>
     *
     * @deprecated replaced by toCollection
     */
    public static List toList(JSONArray jsonArray, Class objectClass, Map classMap) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        jsonConfig.setClassMap(classMap);
        return toList(jsonArray, jsonConfig);
    }

    /**
     * Creates a List from a JSONArray.<br>
     *
     * @see #toCollection(JSONArray, JsonConfig)
     * @deprecated replaced by toCollection
     */
    public static List toList(JSONArray jsonArray, JsonConfig jsonConfig) {
        Collection collection = toCollection(jsonArray, jsonConfig);
        if (collection != null) {
            if (collection instanceof List) {
                return (List) collection;
            } else {
                return new ArrayList(collection);
            }
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Creates a List from a JSONArray.<br>
     */
    public static List toList(JSONArray jsonArray, Object root, JsonConfig jsonConfig) {
        Class clazz = jsonConfig.getRootClass();
        if (root != null && root.getClass() != clazz) {
            jsonConfig.setRootClass(root.getClass());
        }
        return toList(jsonArray, jsonConfig);
    }


    private static void processArrayDimensions(JSONArray jsonArray, List dims, int index) {
        if (dims.size() <= index) {
            dims.add(new Integer(jsonArray.size()));
        } else {
            int i = ((Integer) dims.get(index)).intValue();
            if (jsonArray.size() > i) {
                dims.set(index, new Integer(jsonArray.size()));
            }
        }
        for (Iterator i = jsonArray.iterator(); i.hasNext(); ) {
            Object item = i.next();
            if (item instanceof JSONArray) {
                processArrayDimensions((JSONArray) item, dims, index + 1);
            }
        }
    }

    // ------------------------------------------------------

    /**
     * The List where the JSONArray's properties are kept.
     */
    private List elements;

    /**
     * A flag for XML processing.
     */
    private boolean expandElements;

    /**
     * Construct an empty JSONArray.
     */
    public JSONArray() {
        this.elements = new ArrayList();
    }

    @Override
    public void add(int index, Object value) {
        add(index, value, new JsonConfig());
    }

    public void add(int index, Object value, JsonConfig jsonConfig) {
        this.elements.add(index, processValue(value, jsonConfig));
    }

    @Override
    public boolean add(Object value) {
        return add(value, new JsonConfig());
    }

    public boolean add(Object value, JsonConfig jsonConfig) {
        element(value, jsonConfig);
        return true;
    }

    @Override
    public boolean addAll(Collection collection) {
        return addAll(collection, new JsonConfig());
    }

    public boolean addAll(Collection collection, JsonConfig jsonConfig) {
        if (collection == null || collection.size() == 0) {
            return false;
        }
        for (Iterator i = collection.iterator(); i.hasNext(); ) {
            element(i.next(), jsonConfig);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection collection) {
        return addAll(index, collection, new JsonConfig());
    }

    public boolean addAll(int index, Collection collection, JsonConfig jsonConfig) {
        if (collection == null || collection.size() == 0) {
            return false;
        }
        int offset = 0;
        for (Iterator i = collection.iterator(); i.hasNext(); ) {
            this.elements.add(index + (offset++), processValue(i.next(), jsonConfig));
        }
        return true;
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Override
    public int compareTo(Object obj) {
        if (obj != null && (obj instanceof JSONArray)) {
            JSONArray other = (JSONArray) obj;
            int size1 = size();
            int size2 = other.size();
            if (size1 < size2) {
                return -1;
            } else if (size1 > size2) {
                return 1;
            } else if (this.equals(other)) {
                return 0;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return contains(o, new JsonConfig());
    }

    public boolean contains(Object o, JsonConfig jsonConfig) {
        return elements.contains(processValue(o, jsonConfig));
    }

    @Override
    public boolean containsAll(Collection collection) {
        return containsAll(collection, new JsonConfig());
    }

    public boolean containsAll(Collection collection, JsonConfig jsonConfig) {
        return elements.containsAll(fromObject(collection, jsonConfig));
    }

    /**
     * Remove an element, if present.
     *
     * @param index the index of the element.
     * @return this.
     */
    public JSONArray discard(int index) {
        elements.remove(index);
        return this;
    }

    /**
     * Remove an element, if present.
     *
     * @return this.
     */
    public JSONArray discard(Object o) {
        elements.remove(o);
        return this;
    }

    /**
     * Append a boolean value. This increases the array's length by one.
     *
     * @param value A boolean value.
     * @return this.
     */
    public JSONArray element(boolean value) {
        return element(value ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Append a value in the JSONArray, where the value will be a JSONArray which
     * is produced from a Collection.
     *
     * @param value A Collection value.
     * @return this.
     */
    public JSONArray element(Collection value) {
        return element(value, new JsonConfig());
    }

    /**
     * Append a value in the JSONArray, where the value will be a JSONArray which
     * is produced from a Collection.
     *
     * @param value A Collection value.
     * @return this.
     */
    public JSONArray element(Collection value, JsonConfig jsonConfig) {
        if (value instanceof JSONArray) {
            elements.add(value);
            return this;
        } else {
            return element(JsonMapper.fromJavaObject(value));
        }
    }

    /**
     * Append a double value. This increases the array's length by one.
     *
     * @param value A double value.
     * @return this.
     * @throws JSONException if the value is not finite.
     */
    public JSONArray element(double value) {
        Double d = new Double(value);
        JSONUtils.testValidity(d);
        return element(d);
    }

    /**
     * Append an int value. This increases the array's length by one.
     *
     * @param value An int value.
     * @return this.
     */
    public JSONArray element(int value) {
        return element(new Integer(value));
    }

    /**
     * Put or replace a boolean value in the JSONArray. If the index is greater
     * than the length of the JSONArray, then null elements will be added as
     * necessary to pad it out.
     *
     * @param index The subscript.
     * @param value A boolean value.
     * @return this.
     * @throws JSONException If the index is negative.
     */
    public JSONArray element(int index, boolean value) {
        return element(index, value ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Put a value in the JSONArray, where the value will be a JSONArray which is
     * produced from a Collection.
     *
     * @param index The subscript.
     * @param value A Collection value.
     * @return this.
     * @throws JSONException If the index is negative or if the value is not
     *                       finite.
     */
    public JSONArray element(int index, Collection value) {
        return element(index, value, new JsonConfig());
    }

    /**
     * Put a value in the JSONArray, where the value will be a JSONArray which is
     * produced from a Collection.
     *
     * @param index The subscript.
     * @param value A Collection value.
     * @return this.
     * @throws JSONException If the index is negative or if the value is not
     *                       finite.
     */
    public JSONArray element(int index, Collection value, JsonConfig jsonConfig) {
        if (value instanceof JSONArray) {
            if (index < 0) {
                throw new JSONException("JSONArray[" + index + "] not found.");
            }
            if (index < size()) {
                elements.set(index, value);
            } else {
                while (index != size()) {
                    element(JSONNull.getInstance());
                }
                element(value, jsonConfig);
            }
            return this;
        } else {
            return element(index, JsonMapper.fromJavaObject(value, jsonConfig));
        }
    }

    /**
     * Put or replace a double value. If the index is greater than the length of
     * the JSONArray, then null elements will be added as necessary to pad it
     * out.
     *
     * @param index The subscript.
     * @param value A double value.
     * @return this.
     * @throws JSONException If the index is negative or if the value is not
     *                       finite.
     */
    public JSONArray element(int index, double value) {
        return element(index, new Double(value));
    }

    /**
     * Put or replace an int value. If the index is greater than the length of
     * the JSONArray, then null elements will be added as necessary to pad it
     * out.
     *
     * @param index The subscript.
     * @param value An int value.
     * @return this.
     * @throws JSONException If the index is negative.
     */
    public JSONArray element(int index, int value) {
        return element(index, new Integer(value));
    }

    /**
     * Put or replace a long value. If the index is greater than the length of
     * the JSONArray, then null elements will be added as necessary to pad it
     * out.
     *
     * @param index The subscript.
     * @param value A long value.
     * @return this.
     * @throws JSONException If the index is negative.
     */
    public JSONArray element(int index, long value) {
        return element(index, new Long(value));
    }

    /**
     * Put a value in the JSONArray, where the value will be a JSONObject which
     * is produced from a Map.
     *
     * @param index The subscript.
     * @param value The Map value.
     * @return this.
     * @throws JSONException If the index is negative or if the the value is an
     *                       invalid number.
     */
    public JSONArray element(int index, Map value) {
        return element(index, value, new JsonConfig());
    }

    /**
     * Put a value in the JSONArray, where the value will be a JSONObject which
     * is produced from a Map.
     *
     * @param index The subscript.
     * @param value The Map value.
     * @return this.
     * @throws JSONException If the index is negative or if the the value is an
     *                       invalid number.
     */
    public JSONArray element(int index, Map value, JsonConfig jsonConfig) {
        if (value instanceof JSONObject) {
            if (index < 0) {
                throw new JSONException("JSONArray[" + index + "] not found.");
            }
            if (index < size()) {
                elements.set(index, value);
            } else {
                while (index != size()) {
                    element(JSONNull.getInstance());
                }
                element(value, jsonConfig);
            }
            return this;
        } else {
            return element(index, JSONObject.fromObject(value, jsonConfig));
        }
    }

    /**
     * Put or replace an object value in the JSONArray. If the index is greater
     * than the length of the JSONArray, then null elements will be added as
     * necessary to pad it out.
     *
     * @param index The subscript.
     * @param value An object value. The value should be a Boolean, Double,
     *              Integer, JSONArray, JSONObject, JSONFunction, Long, String,
     *              JSONString or the JSONNull object.
     * @return this.
     * @throws JSONException If the index is negative or if the the value is an
     *                       invalid number.
     */
    public JSONArray element(int index, Object value) {
        return element(index, value, new JsonConfig());
    }

    /**
     * Put or replace an object value in the JSONArray. If the index is greater
     * than the length of the JSONArray, then null elements will be added as
     * necessary to pad it out.
     *
     * @param index The subscript.
     * @param value An object value. The value should be a Boolean, Double,
     *              Integer, JSONArray, JSONObject, JSONFunction, Long, String,
     *              JSONString or the JSONNull object.
     * @return this.
     * @throws JSONException If the index is negative or if the the value is an
     *                       invalid number.
     */
    public JSONArray element(int index, Object value, JsonConfig jsonConfig) {
        JSONUtils.testValidity(value);
        if (index < 0) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        if (index < size()) {
            this.elements.set(index, processValue(value, jsonConfig));
        } else {
            while (index != size()) {
                element(JSONNull.getInstance());
            }
            element(value, jsonConfig);
        }
        return this;
    }

    /**
     * Put or replace a String value in the JSONArray. If the index is greater
     * than the length of the JSONArray, then null elements will be added as
     * necessary to pad it out.<br>
     * The string may be a valid JSON formatted string, in tha case, it will be
     * transformed to a JSONArray, JSONObject or JSONNull.
     *
     * @param index The subscript.
     * @param value A String value.
     * @return this.
     * @throws JSONException If the index is negative or if the the value is an
     *                       invalid number.
     */
    public JSONArray element(int index, String value) {
        return element(index, value, new JsonConfig());
    }

    /**
     * Put or replace a String value in the JSONArray. If the index is greater
     * than the length of the JSONArray, then null elements will be added as
     * necessary to pad it out.<br>
     * The string may be a valid JSON formatted string, in tha case, it will be
     * transformed to a JSONArray, JSONObject or JSONNull.
     *
     * @param index The subscript.
     * @param value A String value.
     * @return this.
     * @throws JSONException If the index is negative or if the the value is an
     *                       invalid number.
     */
    public JSONArray element(int index, String value, JsonConfig jsonConfig) {
        if (index < 0) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        if (index < size()) {
            if (value == null) {
                this.elements.set(index, "");
            } else if (JSONUtils.mayBeJSON(value)) {
                try {
                    this.elements.set(index, JSONSerializer.toJSON(value, jsonConfig));
                } catch (JSONException jsone) {
                    this.elements.set(index, JSONUtils.stripQuotes(value));
                }
            } else {
                this.elements.set(index, JSONUtils.stripQuotes(value));
            }
        } else {
            while (index != size()) {
                element(JSONNull.getInstance());
            }
            element(value, jsonConfig);
        }
        return this;
    }

    /**
     * Append an JSON value. This increases the array's length by one.
     *
     * @param value An JSON value.
     * @return this.
     */
    public JSONArray element(JSONNull value) {
        this.elements.add(value);
        return this;
    }

    /**
     * Append an JSON value. This increases the array's length by one.
     *
     * @param value An JSON value.
     * @return this.
     */
    public JSONArray element(JSONObject value) {
        this.elements.add(value);
        return this;
    }

    /**
     * Append an long value. This increases the array's length by one.
     *
     * @param value A long value.
     * @return this.
     */
    public JSONArray element(long value) {
        return element(JSONUtils.transformNumber(new Long(value)));
    }

    /**
     * Put a value in the JSONArray, where the value will be a JSONObject which
     * is produced from a Map.
     *
     * @param value A Map value.
     * @return this.
     */
    public JSONArray element(Map value) {
        return element(value, new JsonConfig());
    }

    /**
     * Put a value in the JSONArray, where the value will be a JSONObject which
     * is produced from a Map.
     *
     * @param value A Map value.
     * @return this.
     */
    public JSONArray element(Map value, JsonConfig jsonConfig) {
        if (value instanceof JSONObject) {
            elements.add(value);
            return this;
        } else {
            return element(JSONObject.fromObject(value, jsonConfig));
        }
    }

    /**
     * Append an object value. This increases the array's length by one.
     *
     * @param value An object value. The value should be a Boolean, Double,
     *              Integer, JSONArray, JSONObject, JSONFunction, Long, String,
     *              JSONString or the JSONNull object.
     * @return this.
     */
    public JSONArray element(Object value) {
        return element(value, new JsonConfig());
    }

    /**
     * Append an object value. This increases the array's length by one.
     *
     * @param value An object value. The value should be a Boolean, Double,
     *              Integer, JSONArray, JSONObject, JSONFunction, Long, String,
     *              JSONString or the JSONNull object.
     * @return this.
     */
    public JSONArray element(Object value, JsonConfig jsonConfig) {
        return addValue(value, jsonConfig);
    }

    /**
     * Append a String value. This increases the array's length by one.<br>
     * The string may be a valid JSON formatted string, in tha case, it will be
     * transformed to a JSONArray, JSONObject or JSONNull.
     *
     * @param value A String value.
     * @return this.
     */
    public JSONArray element(String value) {
        return element(value, new JsonConfig());
    }

    /**
     * Append a String value. This increases the array's length by one.<br>
     * The string may be a valid JSON formatted string, in tha case, it will be
     * transformed to a JSONArray, JSONObject or JSONNull.
     *
     * @param value A String value.
     * @return this.
     */
    public JSONArray element(String value, JsonConfig jsonConfig) {
        if (value == null) {
            this.elements.add("");
        } else if (JSONUtils.hasQuotes(value)) {
            this.elements.add(value);
        } else if (JSONNull.getInstance().equals(value)) {
            this.elements.add(JSONNull.getInstance());
        } else if (JSONUtils.isJsonKeyword(value, jsonConfig)) {
            if (jsonConfig.isJavascriptCompliant() && "undefined".equals(value)) {
                this.elements.add(JSONNull.getInstance());
            } else {
                this.elements.add(value);
            }
        } else if (JSONUtils.mayBeJSON(value)) {
            try {
                this.elements.add(JSONSerializer.toJSON(value, jsonConfig));
            } catch (JSONException jsone) {
                this.elements.add(value);
            }
        } else {
            this.elements.add(value);
        }
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return true;
    }

    /**
     * Get the object value associated with an index.
     *
     * @param index The index must be between 0 and size() - 1.
     * @return An object value.
     */
    @Override
    public Object get(int index) {
        /*
         * Object o = opt( index ); if( o == null ){ throw new JSONException(
         * "JSONArray[" + index + "] not found." ); } return o;
         */
        return this.elements.get(index);
    }

    /**
     * Get the boolean value associated with an index. The string values "true"
     * and "false" are converted to boolean.
     *
     * @param index The index must be between 0 and size() - 1.
     * @return The truth.
     * @throws JSONException If there is no value for the index or if the value
     *                       is not convertable to boolean.
     */
    public boolean getBoolean(int index) {
        Object o = get(index);
        if (o != null) {
            if (o.equals(Boolean.FALSE)
                    || (o instanceof String && ((String) o).equalsIgnoreCase("false"))) {
                return false;
            } else if (o.equals(Boolean.TRUE)
                    || (o instanceof String && ((String) o).equalsIgnoreCase("true"))) {
                return true;
            }
        }
        throw new JSONException("JSONArray[" + index + "] is not a Boolean.");
    }

    /**
     * Get the double value associated with an index.
     *
     * @param index The index must be between 0 and size() - 1.
     * @return The value.
     * @throws JSONException If the key is not found or if the value cannot be
     *                       converted to a number.
     */
    public double getDouble(int index) {
        Object o = get(index);
        if (o != null) {
            try {
                return o instanceof Number ? ((Number) o).doubleValue()
                        : Double.parseDouble((String) o);
            } catch (Exception e) {
                throw new JSONException("JSONArray[" + index + "] is not a number.");
            }
        }
        throw new JSONException("JSONArray[" + index + "] is not a number.");
    }

    /**
     * Get the int value associated with an index.
     *
     * @param index The index must be between 0 and size() - 1.
     * @return The value.
     * @throws JSONException If the key is not found or if the value cannot be
     *                       converted to a number. if the value cannot be converted to a
     *                       number.
     */
    public int getInt(int index) {
        Object o = get(index);
        if (o != null) {
            return o instanceof Number ? ((Number) o).intValue() : (int) getDouble(index);
        }
        throw new JSONException("JSONArray[" + index + "] is not a number.");
    }

    /**
     * Get the JSONArray associated with an index.
     *
     * @param index The index must be between 0 and size() - 1.
     * @return A JSONArray value.
     * @throws JSONException If there is no value for the index. or if the value
     *                       is not a JSONArray
     */
    public JSONArray getJSONArray(int index) {
        Object o = get(index);
        if (o != null && o instanceof JSONArray) {
            return (JSONArray) o;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
    }

    /**
     * Get the JSONObject associated with an index.
     *
     * @param index subscript
     * @return A JSONObject value.
     * @throws JSONException If there is no value for the index or if the value
     *                       is not a JSONObject
     */
    public JSONObject getJSONObject(int index) {
        Object o = get(index);
        if (JSONNull.getInstance()
                .equals(o)) {
            return new JSONObject(true);
        } else if (o instanceof JSONObject) {
            return (JSONObject) o;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONObject.");
    }

    /**
     * Get the long value associated with an index.
     *
     * @param index The index must be between 0 and size() - 1.
     * @return The value.
     * @throws JSONException If the key is not found or if the value cannot be
     *                       converted to a number.
     */
    public long getLong(int index) {
        Object o = get(index);
        if (o != null) {
            return o instanceof Number ? ((Number) o).longValue() : (long) getDouble(index);
        }
        throw new JSONException("JSONArray[" + index + "] is not a number.");
    }

    /**
     * Get the string associated with an index.
     *
     * @param index The index must be between 0 and size() - 1.
     * @return A string value.
     * @throws JSONException If there is no value for the index.
     */
    public String getString(int index) {
        Object o = get(index);
        if (o != null) {
            return o.toString();
        }
        throw new JSONException("JSONArray[" + index + "] not found.");
    }

    @Override
    public int hashCode() {
        int hashcode = 29;

        for (Iterator e = elements.iterator(); e.hasNext(); ) {
            Object element = e.next();
            hashcode += JSONUtils.hashCode(element);
        }
        return hashcode;
    }

    @Override
    public int indexOf(Object o) {
        return elements.indexOf(o);
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    public boolean isExpandElements() {
        return expandElements;
    }

    /**
     * Returns an Iterator for this JSONArray
     */
    @Override
    public Iterator iterator() {
        return new JSONArrayListIterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return elements.lastIndexOf(o);
    }

    @Override
    public ListIterator listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator listIterator(int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        return new JSONArrayListIterator(index);
    }

    /**
     * Get the optional object value associated with an index.
     *
     * @param index The index must be between 0 and size() - 1.
     * @return An object value, or null if there is no object at that index.
     */
    public Object opt(int index) {
        return (index < 0 || index >= size()) ? null : this.elements.get(index);
    }

    /**
     * Get the optional boolean value associated with an index. It returns false
     * if there is no value at that index, or if the value is not Boolean.TRUE or
     * the String "true".
     *
     * @param index The index must be between 0 and size() - 1.
     * @return The truth.
     */
    public boolean optBoolean(int index) {
        return optBoolean(index, false);
    }

    /**
     * Get the optional boolean value associated with an index. It returns the
     * defaultValue if there is no value at that index or if it is not a Boolean
     * or the String "true" or "false" (case insensitive).
     *
     * @param index        The index must be between 0 and size() - 1.
     * @param defaultValue A boolean default.
     * @return The truth.
     */
    public boolean optBoolean(int index, boolean defaultValue) {
        try {
            return getBoolean(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get the optional double value associated with an index. NaN is returned if
     * there is no value for the index, or if the value is not a number and
     * cannot be converted to a number.
     *
     * @param index The index must be between 0 and size() - 1.
     * @return The value.
     */
    public double optDouble(int index) {
        return optDouble(index, Double.NaN);
    }

    /**
     * Get the optional double value associated with an index. The defaultValue
     * is returned if there is no value for the index, or if the value is not a
     * number and cannot be converted to a number.
     *
     * @param index        subscript
     * @param defaultValue The default value.
     * @return The value.
     */
    public double optDouble(int index, double defaultValue) {
        try {
            return getDouble(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get the optional int value associated with an index. Zero is returned if
     * there is no value for the index, or if the value is not a number and
     * cannot be converted to a number.
     *
     * @param index The index must be between 0 and size() - 1.
     * @return The value.
     */
    public int optInt(int index) {
        return optInt(index, 0);
    }

    /**
     * Get the optional int value associated with an index. The defaultValue is
     * returned if there is no value for the index, or if the value is not a
     * number and cannot be converted to a number.
     *
     * @param index        The index must be between 0 and size() - 1.
     * @param defaultValue The default value.
     * @return The value.
     */
    public int optInt(int index, int defaultValue) {
        try {
            return getInt(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get the optional JSONArray associated with an index.
     *
     * @param index subscript
     * @return A JSONArray value, or null if the index has no value, or if the
     * value is not a JSONArray.
     */
    public JSONArray optJSONArray(int index) {
        Object o = opt(index);
        return o instanceof JSONArray ? (JSONArray) o : null;
    }

    /**
     * Get the optional JSONObject associated with an index. Null is returned if
     * the key is not found, or null if the index has no value, or if the value
     * is not a JSONObject.
     *
     * @param index The index must be between 0 and size() - 1.
     * @return A JSONObject value.
     */
    public JSONObject optJSONObject(int index) {
        Object o = opt(index);
        return o instanceof JSONObject ? (JSONObject) o : null;
    }

    /**
     * Get the optional long value associated with an index. Zero is returned if
     * there is no value for the index, or if the value is not a number and
     * cannot be converted to a number.
     *
     * @param index The index must be between 0 and size() - 1.
     * @return The value.
     */
    public long optLong(int index) {
        return optLong(index, 0);
    }

    /**
     * Get the optional long value associated with an index. The defaultValue is
     * returned if there is no value for the index, or if the value is not a
     * number and cannot be converted to a number.
     *
     * @param index        The index must be between 0 and size() - 1.
     * @param defaultValue The default value.
     * @return The value.
     */
    public long optLong(int index, long defaultValue) {
        try {
            return getLong(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get the optional string value associated with an index. It returns an
     * empty string if there is no value at that index. If the value is not a
     * string and is not null, then it is coverted to a string.
     *
     * @param index The index must be between 0 and size() - 1.
     * @return A String value.
     */
    public String optString(int index) {
        return optString(index, "");
    }

    /**
     * Get the optional string associated with an index. The defaultValue is
     * returned if the key is not found.
     *
     * @param index        The index must be between 0 and size() - 1.
     * @param defaultValue The default value.
     * @return A String value.
     */
    public String optString(int index, String defaultValue) {
        Object o = opt(index);
        return o != null ? o.toString() : defaultValue;
    }

    @Override
    public Object remove(int index) {
        return elements.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        return elements.remove(o);
    }

    @Override
    public boolean removeAll(Collection collection) {
        return removeAll(collection, new JsonConfig());
    }

    public boolean removeAll(Collection collection, JsonConfig jsonConfig) {
        return elements.removeAll(fromObject(collection, jsonConfig));
    }

    @Override
    public boolean retainAll(Collection collection) {
        return retainAll(collection, new JsonConfig());
    }

    public boolean retainAll(Collection collection, JsonConfig jsonConfig) {
        return elements.retainAll(fromObject(collection, jsonConfig));
    }

    @Override
    public Object set(int index, Object value) {
        return set(index, value, new JsonConfig());
    }

    public Object set(int index, Object value, JsonConfig jsonConfig) {
        Object previous = get(index);
        element(index, value, jsonConfig);
        return previous;
    }

    public void setExpandElements(boolean expandElements) {
        this.expandElements = expandElements;
    }

    /**
     * Get the number of elements in the JSONArray, included nulls.
     *
     * @return The length (or size).
     */
    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        return elements.subList(fromIndex, toIndex);
    }

    /**
     * Produce an Object[] with the contents of this JSONArray.
     */
    @Override
    public Object[] toArray() {
        return this.elements.toArray();
    }

    @Override
    public Object[] toArray(Object[] array) {
        return elements.toArray(array);
    }

    /**
     * Produce a JSONObject by combining a JSONArray of names with the values of
     * this JSONArray.
     *
     * @param names A JSONArray containing a list of key strings. These will be
     *              paired with the values.
     * @return A JSONObject, or null if there are no names or if this JSONArray
     * has no values.
     * @throws JSONException If any of the names are null.
     */
    public JSONObject toJSONObject(JSONArray names) {
        if (names == null || names.size() == 0 || size() == 0) {
            return null;
        }
        JSONObject jo = new JSONObject();
        for (int i = 0; i < names.size(); i++) {
            jo.element(names.getString(i), this.opt(i));
        }
        return jo;
    }

    /**
     * Make a JSON text of this JSONArray. For compactness, no unnecessary
     * whitespace is added. If it is not possible to produce a syntactically
     * correct JSON text then null will be returned instead. This could occur if
     * the array contains an invalid number.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return a printable, displayable, transmittable representation of the
     * array.
     */
    @Override
    public String toString() {
        return JSONBuilderProvider.simplest().toJson(JsonMapper.toJsonTreeNode(this));
    }

    /**
     * Make a prettyprinted JSON text of this JSONArray. Warning: This method
     * assumes that the data structure is acyclical.
     *
     * @param indentFactor The number of spaces to add to each level of
     *                     indentation.
     * @return a printable, displayable, transmittable representation of the
     * object, beginning with <code>[</code>&nbsp;<small>(left
     * bracket)</small> and ending with <code>]</code>&nbsp;<small>(right
     * bracket)</small>.
     * @throws JSONException
     */
    @Override
    public String toString(int indentFactor) {
        return JSONBuilderProvider.create().prettyFormat(indentFactor > 0).build().toJson(JsonMapper.toJsonTreeNode(this));
    }

    /**
     * Make a prettyprinted JSON text of this JSONArray. Warning: This method
     * assumes that the data structure is acyclical.
     *
     * @param indentFactor The number of spaces to add to each level of
     *                     indentation.
     * @param indent       The indention of the top level.
     * @return a printable, displayable, transmittable representation of the
     * array.
     * @throws JSONException
     */
    @Override
    public String toString(int indentFactor, int indent) {
        return JSONBuilderProvider.create().prettyFormat(indentFactor > 0 || indent > 0).build().toJson(JsonMapper.toJsonTreeNode(this));
    }

    /**
     * Write the contents of the JSONArray as JSON text to a writer. For
     * compactness, no whitespace is added.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return The writer.
     * @throws JSONException
     */
    @Override
    public Writer write(Writer writer) {
        String str = toString();
        try {
            writer.write(str);
        } catch (IOException e) {
            throw new JSONException(e);
        }
        return writer;
    }

    /**
     * Adds a String without performing any conversion on it.
     */
    protected JSONArray addString(String str) {
        if (str != null) {
            elements.add(str);
        }
        return this;
    }

    /**
     * Append an object value. This increases the array's length by one.
     *
     * @param value An object value. The value should be a Boolean, Double,
     *              Integer, JSONArray, JSONObject, JSONFunction, Long, String,
     *              JSONString or the JSONNull object.
     * @return this.
     */
    private JSONArray _addValue(Object value, JsonConfig jsonConfig) {
        this.elements.add(value);
        return this;
    }

    /**
     * Append an object value. This increases the array's length by one.
     *
     * @param value An object value. The value should be a Boolean, Double,
     *              Integer, JSONArray, JSONObject, JSONFunction, Long, String,
     *              JSONString or the JSONNull object.
     * @return this.
     */
    private JSONArray addValue(Object value, JsonConfig jsonConfig) {
        return _addValue(processValue(value, jsonConfig), jsonConfig);
    }

    private Object processValue(Object value, JsonConfig jsonConfig) {
        return JsonMapper.fromJavaObject(value, jsonConfig);
    }

    private class JSONArrayListIterator implements ListIterator {
        int currentIndex = 0;
        int lastIndex = -1;

        JSONArrayListIterator() {

        }

        JSONArrayListIterator(int index) {
            currentIndex = index;
        }

        @Override
        public boolean hasNext() {
            return currentIndex != size();
        }

        @Override
        public Object next() {
            try {
                Object next = get(currentIndex);
                lastIndex = currentIndex++;
                return next;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            if (lastIndex == -1) {
                throw new IllegalStateException();
            }
            try {
                JSONArray.this.remove(lastIndex);
                if (lastIndex < currentIndex) {
                    currentIndex--;
                }
                lastIndex = -1;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasPrevious() {
            return currentIndex != 0;
        }

        @Override
        public Object previous() {
            try {
                int index = currentIndex - 1;
                Object previous = get(index);
                lastIndex = currentIndex = index;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public int nextIndex() {
            return currentIndex;
        }

        @Override
        public int previousIndex() {
            return currentIndex - 1;
        }

        @Override
        public void set(Object obj) {
            if (lastIndex == -1) {
                throw new IllegalStateException();
            }

            try {
                JSONArray.this.set(lastIndex, obj);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void add(Object obj) {
            try {
                JSONArray.this.add(currentIndex++, obj);
                lastIndex = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
