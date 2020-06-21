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
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.JsonTreeNodes;
import com.jn.langx.util.reflect.type.Primitives;
import net.sf.json.processors.JsonVerifier;
import net.sf.json.util.JSONUtils;
import net.sf.json.util.PropertySetStrategy;

import java.io.IOException;
import java.io.Writer;
import java.util.*;


/**
 * A JSONObject is an unordered collection of name/value pairs. Its external
 * form is a string wrapped in curly braces with colons between the names and
 * values, and commas between the values and names. The internal form is an
 * object having <code>get</code> and <code>opt</code> methods for accessing
 * the values by name, and <code>put</code> methods for adding or replacing
 * values by name. The values can be any of these types: <code>Boolean</code>,
 * <code>JSONArray</code>, <code>JSONObject</code>, <code>Number</code>,
 * <code>String</code>, or the <code>JSONNull</code> object. A JSONObject
 * constructor can be used to convert an external form JSON text into an
 * internal form whose values can be retrieved with the <code>get</code> and
 * <code>opt</code> methods, or to convert values into a JSON text using the
 * <code>element</code> and <code>toString</code> methods. A
 * <code>get</code> method returns a value if one can be found, and throws an
 * exception if one cannot be found. An <code>opt</code> method returns a
 * default value instead of throwing an exception, and so is useful for
 * obtaining optional values.
 * <p>
 * The generic <code>get()</code> and <code>opt()</code> methods return an
 * object, which you can cast or query for type. There are also typed
 * <code>get</code> and <code>opt</code> methods that do type checking and
 * type coercion for you.
 * <p>
 * The <code>put</code> methods adds values to an object. For example,
 * <p>
 * <pre>
 *     myString = new JSONObject().put("JSON", "Hello, World!").toString();</pre>
 * <p>
 * produces the string <code>{"JSON": "Hello, World"}</code>.
 * <p>
 * The texts produced by the <code>toString</code> methods strictly conform to
 * the JSON syntax rules. The constructors are more forgiving in the texts they
 * will accept:
 * <ul>
 * <li>An extra <code>,</code>&nbsp;<small>(comma)</small> may appear just
 * before the closing brace.</li>
 * <li>Strings may be quoted with <code>'</code>&nbsp;<small>(single quote)</small>.</li>
 * <li>Strings do not need to be quoted at all if they do not begin with a
 * quote or single quote, and if they do not contain leading or trailing spaces,
 * and if they do not contain any of these characters:
 * <code>{ } [ ] / \ : , = ; #</code> and if they do not look like numbers and
 * if they are not the reserved words <code>true</code>, <code>false</code>,
 * or <code>null</code>.</li>
 * <li>Keys can be followed by <code>=</code> or <code>=></code> as well as
 * by <code>:</code>.</li>
 * <li>Values can be followed by <code>;</code> <small>(semicolon)</small>
 * as well as by <code>,</code> <small>(comma)</small>.</li>
 * <li>Numbers may have the <code>0-</code> <small>(octal)</small> or
 * <code>0x-</code> <small>(hex)</small> prefix.</li>
 * <li>Comments written in the slashshlash, slashstar, and hash conventions
 * will be ignored.</li>
 * </ul>
 *
 * @author JSON.org
 */
public final class JSONObject extends AbstractJSON implements JSON, Map, Comparable {

    /**
     * identifies this object as null
     */
    private boolean nullObject;

    /**
     * The Map where the JSONObject's properties are kept.
     */
    private Map properties;


    /**
     * Creates a JSONObject.<br>
     * Inspects the object type to call the correct JSONObject factory method.
     * Accepts JSON formatted strings, Maps, DynaBeans and JavaBeans.
     *
     * @param object
     * @throws JSONException if the object can not be converted to a proper
     *                       JSONObject.
     */

    public static JSONObject fromObject(Object object) {
        return fromObject(object, new JsonConfig());
    }

    /**
     * Creates a JSONObject.<br>
     * Inspects the object type to call the correct JSONObject factory method.
     * Accepts JSON formatted strings, Maps, DynaBeans and JavaBeans.
     *
     * @param object
     * @throws JSONException if the object can not be converted to a proper
     *                       JSONObject.
     */
    public static JSONObject fromObject(Object object, JsonConfig jsonConfig) {
        Object jsonObj = JsonMapper.fromJavaObject(object, jsonConfig);
        if (jsonObj == null || jsonObj == JSONNull.getInstance()) {
            return new JSONObject(true);
        }

        if (Primitives.isPrimitive(jsonObj.getClass())) {
            throw new JSONException("primitive type value " + jsonObj + " is not a JSONObject");
        }

        if (jsonObj instanceof JSONArray) {
            throw new JSONException("JSONArray is not a JSONObject");
        }
        return (JSONObject) jsonObj;
    }


    /**
     * Creates a bean from a JSONObject, with a specific target class.<br>
     */
    public static Object toBean(JSONObject jsonObject, Class beanClass) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(beanClass);
        return toBean(jsonObject, jsonConfig);
    }

    /**
     * Creates a bean from a JSONObject, with a specific target class.<br>
     * If beanClass is null, this method will return a graph of DynaBeans. Any
     * attribute that is a JSONObject and matches a key in the classMap will be
     * converted to that target class.<br>
     * The classMap has the following conventions:
     * <ul>
     * <li>Every key must be an String.</li>
     * <li>Every value must be a Class.</li>
     * <li>A key may be a regular expression.</li>
     * </ul>
     */
    public static Object toBean(JSONObject jsonObject, Class beanClass, Map classMap) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(beanClass);
        return toBean(jsonObject, jsonConfig);
    }

    /**
     * Creates a bean from a JSONObject, with the specific configuration.
     */
    public static Object toBean(JSONObject jsonObject, JsonConfig jsonConfig) {
        return toBean(jsonObject, null, jsonConfig);
    }

    /**
     * Creates a bean from a JSONObject, with the specific configuration.
     */
    public static Object toBean(JSONObject jsonObject, Object root, JsonConfig jsonConfig) {
        Class clazz = root !=null ? root.getClass() : null;
        if (clazz == null) {
            clazz = jsonConfig.getRootClass();
        }
        com.jn.easyjson.core.JSON json = JsonMapper.buildJSON(jsonConfig);
        JsonTreeNode node1 = json.fromJson(json.toJson(root));
        JsonTreeNode node2 = JsonMapper.toJsonTreeNode(jsonObject);

        if (clazz == null) {
            return JsonTreeNodes.toJavaObject(JsonTreeNodes.combine(node1, node2));
        }
        return json.fromJson(json.toJson(JsonTreeNodes.combine(node1, node2)), clazz);
    }


    /**
     * Sets a property on the target bean.<br>
     * Bean may be a Map or a POJO.
     */
    private static void setProperty(Object bean, String key, Object value, JsonConfig jsonConfig)
            throws Exception {
        PropertySetStrategy propertySetStrategy = jsonConfig.getPropertySetStrategy() != null ? jsonConfig.getPropertySetStrategy()
                : PropertySetStrategy.DEFAULT;
        propertySetStrategy.setProperty(bean, key, value, jsonConfig);
    }

    private static void setValue(JSONObject jsonObject, String key, Object value, Class type,
                                 JsonConfig jsonConfig, boolean bypass) {
        boolean accumulated = false;
        if (value == null) {
            value = jsonConfig.findDefaultValueProcessor(type)
                    .getDefaultValue(type);
            if (!JsonVerifier.isValidJsonValue(value)) {
                throw new JSONException("Value is not a valid JSON value. " + value);
            }
        }
        if (jsonObject.properties.containsKey(key)) {
            if (String.class.isAssignableFrom(type)) {
                Object o = jsonObject.opt(key);
                if (o instanceof JSONArray) {
                    ((JSONArray) o).addString((String) value);
                } else {
                    jsonObject.properties.put(key, new JSONArray().element(o)
                            .addString((String) value));
                }
            } else {
                jsonObject.accumulate(key, value, jsonConfig);
            }
            accumulated = true;
        } else {
            if (bypass || String.class.isAssignableFrom(type)) {
                jsonObject.properties.put(key, value);
            } else {
                jsonObject.setInternal(key, value, jsonConfig);
            }
        }

        value = jsonObject.opt(key);
        if (accumulated) {
            JSONArray array = (JSONArray) value;
            value = array.get(array.size() - 1);
        }
        firePropertySetEvent(key, value, accumulated, jsonConfig);
    }

    // ------------------------------------------------------


    /**
     * Construct an empty JSONObject.
     */
    public JSONObject() {
        this.properties = new LinkedHashMap();
    }

    /**
     * Creates a JSONObject that is null.
     */
    public JSONObject(boolean isNull) {
        this();
        this.nullObject = isNull;
    }

    /**
     * Accumulate values under a key. It is similar to the element method except
     * that if there is already an object stored under the key then a JSONArray
     * is stored under the key to hold all of the accumulated values. If there is
     * already a JSONArray, then the new value is appended to it. In contrast,
     * the replace method replaces the previous value.
     *
     * @param key   A key string.
     * @param value An object to be accumulated under the key.
     * @return this.
     * @throws JSONException If the value is an invalid number or if the key is
     *                       null.
     */
    public JSONObject accumulate(String key, boolean value) {
        return _accumulate(key, value ? Boolean.TRUE : Boolean.FALSE, new JsonConfig());
    }

    /**
     * Accumulate values under a key. It is similar to the element method except
     * that if there is already an object stored under the key then a JSONArray
     * is stored under the key to hold all of the accumulated values. If there is
     * already a JSONArray, then the new value is appended to it. In contrast,
     * the replace method replaces the previous value.
     *
     * @param key   A key string.
     * @param value An object to be accumulated under the key.
     * @return this.
     * @throws JSONException If the value is an invalid number or if the key is
     *                       null.
     */
    public JSONObject accumulate(String key, double value) {
        return _accumulate(key, Double.valueOf(value), new JsonConfig());
    }

    /**
     * Accumulate values under a key. It is similar to the element method except
     * that if there is already an object stored under the key then a JSONArray
     * is stored under the key to hold all of the accumulated values. If there is
     * already a JSONArray, then the new value is appended to it. In contrast,
     * the replace method replaces the previous value.
     *
     * @param key   A key string.
     * @param value An object to be accumulated under the key.
     * @return this.
     * @throws JSONException If the value is an invalid number or if the key is
     *                       null.
     */
    public JSONObject accumulate(String key, int value) {
        return _accumulate(key, Integer.valueOf(value), new JsonConfig());
    }

    /**
     * Accumulate values under a key. It is similar to the element method except
     * that if there is already an object stored under the key then a JSONArray
     * is stored under the key to hold all of the accumulated values. If there is
     * already a JSONArray, then the new value is appended to it. In contrast,
     * the replace method replaces the previous value.
     *
     * @param key   A key string.
     * @param value An object to be accumulated under the key.
     * @return this.
     * @throws JSONException If the value is an invalid number or if the key is
     *                       null.
     */
    public JSONObject accumulate(String key, long value) {
        return _accumulate(key, Long.valueOf(value), new JsonConfig());
    }

    /**
     * Accumulate values under a key. It is similar to the element method except
     * that if there is already an object stored under the key then a JSONArray
     * is stored under the key to hold all of the accumulated values. If there is
     * already a JSONArray, then the new value is appended to it. In contrast,
     * the replace method replaces the previous value.
     *
     * @param key   A key string.
     * @param value An object to be accumulated under the key.
     * @return this.
     * @throws JSONException If the value is an invalid number or if the key is
     *                       null.
     */
    public JSONObject accumulate(String key, Object value) {
        return _accumulate(key, value, new JsonConfig());
    }

    /**
     * Accumulate values under a key. It is similar to the element method except
     * that if there is already an object stored under the key then a JSONArray
     * is stored under the key to hold all of the accumulated values. If there is
     * already a JSONArray, then the new value is appended to it. In contrast,
     * the replace method replaces the previous value.
     *
     * @param key   A key string.
     * @param value An object to be accumulated under the key.
     * @return this.
     * @throws JSONException If the value is an invalid number or if the key is
     *                       null.
     */
    public JSONObject accumulate(String key, Object value, JsonConfig jsonConfig) {
        return _accumulate(key, value, jsonConfig);
    }

    public void accumulateAll(Map map) {
        accumulateAll(map, new JsonConfig());
    }

    public void accumulateAll(Map map, JsonConfig jsonConfig) {
        if (map instanceof JSONObject) {
            for (Iterator entries = map.entrySet()
                    .iterator(); entries.hasNext(); ) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                accumulate(key, value, jsonConfig);
            }
        } else {
            for (Iterator entries = map.entrySet()
                    .iterator(); entries.hasNext(); ) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = String.valueOf(entry.getKey());
                Object value = entry.getValue();
                accumulate(key, value, jsonConfig);
            }
        }
    }

    @Override
    public void clear() {
        properties.clear();
    }

    @Override
    public int compareTo(Object obj) {
        if (obj != null && (obj instanceof JSONObject)) {
            JSONObject other = (JSONObject) obj;
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
    public boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(value, new JsonConfig());
    }

    public boolean containsValue(Object value, JsonConfig jsonConfig) {
        try {
            value = processValue(value, jsonConfig);
        } catch (JSONException e) {
            return false;
        }
        return properties.containsValue(value);
    }

    /**
     * Remove a name and its value, if present.
     *
     * @param key A key string.
     * @return this.
     */
    public JSONObject discard(String key) {
        verifyIsNull();
        this.properties.remove(key);
        return this;
    }

    /**
     * Put a key/boolean pair in the JSONObject.
     *
     * @param key   A key string.
     * @param value A boolean which is the value.
     * @return this.
     * @throws JSONException If the key is null.
     */
    public JSONObject element(String key, boolean value) {
        verifyIsNull();
        return element(key, value ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Put a key/value pair in the JSONObject, where the value will be a
     * JSONArray which is produced from a Collection.
     *
     * @param key   A key string.
     * @param value A Collection value.
     * @return this.
     * @throws JSONException
     */
    public JSONObject element(String key, Collection value) {
        return element(key, value, new JsonConfig());
    }

    /**
     * Put a key/value pair in the JSONObject, where the value will be a
     * JSONArray which is produced from a Collection.
     *
     * @param key   A key string.
     * @param value A Collection value.
     * @return this.
     * @throws JSONException
     */
    public JSONObject element(String key, Collection value, JsonConfig jsonConfig) {
        if (!(value instanceof JSONArray)) {
            value = JSONArray.fromObject(value, jsonConfig);
        }
        return setInternal(key, value, jsonConfig);
    }

    /**
     * Put a key/double pair in the JSONObject.
     *
     * @param key   A key string.
     * @param value A double which is the value.
     * @return this.
     * @throws JSONException If the key is null or if the number is invalid.
     */
    public JSONObject element(String key, double value) {
        verifyIsNull();
        Double d = new Double(value);
        JSONUtils.testValidity(d);
        return element(key, d);
    }

    /**
     * Put a key/int pair in the JSONObject.
     *
     * @param key   A key string.
     * @param value An int which is the value.
     * @return this.
     * @throws JSONException If the key is null.
     */
    public JSONObject element(String key, int value) {
        verifyIsNull();
        return element(key, new Integer(value));
    }

    /**
     * Put a key/long pair in the JSONObject.
     *
     * @param key   A key string.
     * @param value A long which is the value.
     * @return this.
     * @throws JSONException If the key is null.
     */
    public JSONObject element(String key, long value) {
        verifyIsNull();
        return element(key, new Long(value));
    }

    /**
     * Put a key/value pair in the JSONObject, where the value will be a
     * JSONObject which is produced from a Map.
     *
     * @param key   A key string.
     * @param value A Map value.
     * @return this.
     * @throws JSONException
     */
    public JSONObject element(String key, Map value) {
        return element(key, value, new JsonConfig());
    }

    /**
     * Put a key/value pair in the JSONObject, where the value will be a
     * JSONObject which is produced from a Map.
     *
     * @param key   A key string.
     * @param value A Map value.
     * @return this.
     * @throws JSONException
     */
    public JSONObject element(String key, Map value, JsonConfig jsonConfig) {
        verifyIsNull();
        if (value instanceof JSONObject) {
            return setInternal(key, value, jsonConfig);
        } else {
            return element(key, JSONObject.fromObject(value, jsonConfig), jsonConfig);
        }
    }

    /**
     * Put a key/value pair in the JSONObject. If the value is null, then the key
     * will be removed from the JSONObject if it is present.<br>
     * If there is a previous value assigned to the key, it will call accumulate.
     *
     * @param key   A key string.
     * @param value An object which is the value. It should be of one of these
     *              types: Boolean, Double, Integer, JSONArray, JSONObject, Long,
     *              String, or the JSONNull object.
     * @return this.
     * @throws JSONException If the value is non-finite number or if the key is
     *                       null.
     */
    public JSONObject element(String key, Object value) {
        return element(key, value, new JsonConfig());
    }

    /**
     * Put a key/value pair in the JSONObject. If the value is null, then the key
     * will be removed from the JSONObject if it is present.<br>
     * If there is a previous value assigned to the key, it will call accumulate.
     *
     * @param key   A key string.
     * @param value An object which is the value. It should be of one of these
     *              types: Boolean, Double, Integer, JSONArray, JSONObject, Long,
     *              String, or the JSONNull object.
     * @return this.
     * @throws JSONException If the value is non-finite number or if the key is
     *                       null.
     */
    public JSONObject element(String key, Object value, JsonConfig jsonConfig) {
        verifyIsNull();
        if (key == null) {
            throw new JSONException("Null key.");
        }
        if (value != null) {
            value = processValue(key, value, jsonConfig);
            _setInternal(key, value, jsonConfig);
        } else {
            remove(key);
        }
        return this;
    }

    /**
     * Put a key/value pair in the JSONObject, but only if the key and the value
     * are both non-null.
     *
     * @param key   A key string.
     * @param value An object which is the value. It should be of one of these
     *              types: Boolean, Double, Integer, JSONArray, JSONObject, Long,
     *              String, or the JSONNull object.
     * @return this.
     * @throws JSONException If the value is a non-finite number.
     */
    public JSONObject elementOpt(String key, Object value) {
        return elementOpt(key, value, new JsonConfig());
    }

    /**
     * Put a key/value pair in the JSONObject, but only if the key and the value
     * are both non-null.
     *
     * @param key   A key string.
     * @param value An object which is the value. It should be of one of these
     *              types: Boolean, Double, Integer, JSONArray, JSONObject, Long,
     *              String, or the JSONNull object.
     * @return this.
     * @throws JSONException If the value is a non-finite number.
     */
    public JSONObject elementOpt(String key, Object value, JsonConfig jsonConfig) {
        verifyIsNull();
        if (key != null && value != null) {
            element(key, value, jsonConfig);
        }
        return this;
    }

    @Override
    public Set entrySet() {
        return Collections.unmodifiableSet(properties.entrySet());
    }

    @Override
    public boolean equals(Object obj) {
        String str1 = toString();
        String str2 = JSONBuilderProvider.simplest().toJson(JsonMapper.toJsonTreeNode(this));
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }

    @Override
    public Object get(Object key) {
        if (key instanceof String) {
            return get((String) key);
        }
        return null;
    }

    /**
     * Get the value object associated with a key.
     *
     * @param key A key string.
     * @return The object associated with the key.
     * @throws JSONException if this.isNull() returns true.
     */
    public Object get(String key) {
        verifyIsNull();
        return this.properties.get(key);
    }

    /**
     * Get the boolean value associated with a key.
     *
     * @param key A key string.
     * @return The truth.
     * @throws JSONException if the value is not a Boolean or the String "true"
     *                       or "false".
     */
    public boolean getBoolean(String key) {
        verifyIsNull();
        Object o = get(key);
        if (o != null) {
            if (o.equals(Boolean.FALSE)
                    || (o instanceof String && ((String) o).equalsIgnoreCase("false"))) {
                return false;
            } else if (o.equals(Boolean.TRUE)
                    || (o instanceof String && ((String) o).equalsIgnoreCase("true"))) {
                return true;
            }
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a Boolean.");
    }

    /**
     * Get the double value associated with a key.
     *
     * @param key A key string.
     * @return The numeric value.
     * @throws JSONException if the key is not found or if the value is not a
     *                       Number object and cannot be converted to a number.
     */
    public double getDouble(String key) {
        verifyIsNull();
        Object o = get(key);
        if (o != null) {
            try {
                return o instanceof Number ? ((Number) o).doubleValue()
                        : Double.parseDouble((String) o);
            } catch (Exception e) {
                throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a number.");
            }
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a number.");
    }

    /**
     * Get the int value associated with a key. If the number value is too large
     * for an int, it will be clipped.
     *
     * @param key A key string.
     * @return The integer value.
     * @throws JSONException if the key is not found or if the value cannot be
     *                       converted to an integer.
     */
    public int getInt(String key) {
        verifyIsNull();
        Object o = get(key);
        if (o != null) {
            return o instanceof Number ? ((Number) o).intValue() : (int) getDouble(key);
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a number.");
    }

    /**
     * Get the JSONArray value associated with a key.
     *
     * @param key A key string.
     * @return A JSONArray which is the value.
     * @throws JSONException if the key is not found or if the value is not a
     *                       JSONArray.
     */
    public JSONArray getJSONArray(String key) {
        verifyIsNull();
        Object o = get(key);
        if (o != null && o instanceof JSONArray) {
            return (JSONArray) o;
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a JSONArray.");
    }

    /**
     * Get the JSONObject value associated with a key.
     *
     * @param key A key string.
     * @return A JSONObject which is the value.
     * @throws JSONException if the key is not found or if the value is not a
     *                       JSONObject.
     */
    public JSONObject getJSONObject(String key) {
        verifyIsNull();
        Object o = get(key);
        if (JSONNull.getInstance()
                .equals(o)) {
            return new JSONObject(true);
        } else if (o instanceof JSONObject) {
            return (JSONObject) o;
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a JSONObject.");
    }

    /**
     * Get the long value associated with a key. If the number value is too long
     * for a long, it will be clipped.
     *
     * @param key A key string.
     * @return The long value.
     * @throws JSONException if the key is not found or if the value cannot be
     *                       converted to a long.
     */
    public long getLong(String key) {
        verifyIsNull();
        Object o = get(key);
        if (o != null) {
            return o instanceof Number ? ((Number) o).longValue() : (long) getDouble(key);
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] is not a number.");
    }

    /**
     * Get the string associated with a key.
     *
     * @param key A key string.
     * @return A string which is the value.
     * @throws JSONException if the key is not found.
     */
    public String getString(String key) {
        verifyIsNull();
        Object o = get(key);
        if (o != null) {
            return o.toString();
        }
        throw new JSONException("JSONObject[" + JSONUtils.quote(key) + "] not found.");
    }

    /**
     * Determine if the JSONObject contains a specific key.
     *
     * @param key A key string.
     * @return true if the key exists in the JSONObject.
     */
    public boolean has(String key) {
        verifyIsNull();
        return this.properties.containsKey(key);
    }

    @Override
    public int hashCode() {
        int hashcode = 19;
        if (isNullObject()) {
            return hashcode + JSONNull.getInstance()
                    .hashCode();
        }
        for (Iterator entries = properties.entrySet()
                .iterator(); entries.hasNext(); ) {
            Map.Entry entry = (Map.Entry) entries.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            hashcode += key.hashCode() + JSONUtils.hashCode(value);
        }
        return hashcode;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        // verifyIsNull();
        return this.properties.isEmpty();
    }

    /**
     * Returs if this object is a null JSONObject.
     */
    public boolean isNullObject() {
        return nullObject;
    }

    /**
     * Get an enumeration of the keys of the JSONObject.
     *
     * @return An iterator of the keys.
     */
    public Iterator keys() {
        verifyIsNull();
        return keySet().iterator();
    }

    @Override
    public Set keySet() {
        return Collections.unmodifiableSet(properties.keySet());
    }

    /**
     * Produce a JSONArray containing the names of the elements of this
     * JSONObject.
     *
     * @return A JSONArray containing the key strings, or null if the JSONObject
     * is empty.
     */
    public JSONArray names() {
        verifyIsNull();
        JSONArray ja = new JSONArray();
        Iterator keys = keys();
        while (keys.hasNext()) {
            ja.element(keys.next());
        }
        return ja;
    }

    /**
     * Produce a JSONArray containing the names of the elements of this
     * JSONObject.
     *
     * @return A JSONArray containing the key strings, or null if the JSONObject
     * is empty.
     */
    public JSONArray names(JsonConfig jsonConfig) {
        verifyIsNull();
        JSONArray ja = new JSONArray();
        Iterator keys = keys();
        while (keys.hasNext()) {
            ja.element(keys.next(), jsonConfig);
        }
        return ja;
    }

    /**
     * Get an optional value associated with a key.
     *
     * @param key A key string.
     * @return An object which is the value, or null if there is no value.
     */
    public Object opt(String key) {
        verifyIsNull();
        return key == null ? null : this.properties.get(key);
    }

    /**
     * Get an optional boolean associated with a key. It returns false if there
     * is no such key, or if the value is not Boolean.TRUE or the String "true".
     *
     * @param key A key string.
     * @return The truth.
     */
    public boolean optBoolean(String key) {
        verifyIsNull();
        return optBoolean(key, false);
    }

    /**
     * Get an optional boolean associated with a key. It returns the defaultValue
     * if there is no such key, or if it is not a Boolean or the String "true" or
     * "false" (case insensitive).
     *
     * @param key          A key string.
     * @param defaultValue The default.
     * @return The truth.
     */
    public boolean optBoolean(String key, boolean defaultValue) {
        verifyIsNull();
        try {
            return getBoolean(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get an optional double associated with a key, or NaN if there is no such
     * key or if its value is not a number. If the value is a string, an attempt
     * will be made to evaluate it as a number.
     *
     * @param key A string which is the key.
     * @return An object which is the value.
     */
    public double optDouble(String key) {
        verifyIsNull();
        return optDouble(key, Double.NaN);
    }

    /**
     * Get an optional double associated with a key, or the defaultValue if there
     * is no such key or if its value is not a number. If the value is a string,
     * an attempt will be made to evaluate it as a number.
     *
     * @param key          A key string.
     * @param defaultValue The default.
     * @return An object which is the value.
     */
    public double optDouble(String key, double defaultValue) {
        verifyIsNull();
        try {
            Object o = opt(key);
            return o instanceof Number ? ((Number) o).doubleValue()
                    : new Double((String) o).doubleValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get an optional int value associated with a key, or zero if there is no
     * such key or if the value is not a number. If the value is a string, an
     * attempt will be made to evaluate it as a number.
     *
     * @param key A key string.
     * @return An object which is the value.
     */
    public int optInt(String key) {
        verifyIsNull();
        return optInt(key, 0);
    }

    /**
     * Get an optional int value associated with a key, or the default if there
     * is no such key or if the value is not a number. If the value is a string,
     * an attempt will be made to evaluate it as a number.
     *
     * @param key          A key string.
     * @param defaultValue The default.
     * @return An object which is the value.
     */
    public int optInt(String key, int defaultValue) {
        verifyIsNull();
        try {
            return getInt(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get an optional JSONArray associated with a key. It returns null if there
     * is no such key, or if its value is not a JSONArray.
     *
     * @param key A key string.
     * @return A JSONArray which is the value.
     */
    public JSONArray optJSONArray(String key) {
        verifyIsNull();
        Object o = opt(key);
        return o instanceof JSONArray ? (JSONArray) o : null;
    }

    /**
     * Get an optional JSONObject associated with a key. It returns null if there
     * is no such key, or if its value is not a JSONObject.
     *
     * @param key A key string.
     * @return A JSONObject which is the value.
     */
    public JSONObject optJSONObject(String key) {
        verifyIsNull();
        Object o = opt(key);
        return o instanceof JSONObject ? (JSONObject) o : null;
    }

    /**
     * Get an optional long value associated with a key, or zero if there is no
     * such key or if the value is not a number. If the value is a string, an
     * attempt will be made to evaluate it as a number.
     *
     * @param key A key string.
     * @return An object which is the value.
     */
    public long optLong(String key) {
        verifyIsNull();
        return optLong(key, 0);
    }

    /**
     * Get an optional long value associated with a key, or the default if there
     * is no such key or if the value is not a number. If the value is a string,
     * an attempt will be made to evaluate it as a number.
     *
     * @param key          A key string.
     * @param defaultValue The default.
     * @return An object which is the value.
     */
    public long optLong(String key, long defaultValue) {
        verifyIsNull();
        try {
            return getLong(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get an optional string associated with a key. It returns an empty string
     * if there is no such key. If the value is not a string and is not null,
     * then it is coverted to a string.
     *
     * @param key A key string.
     * @return A string which is the value.
     */
    public String optString(String key) {
        verifyIsNull();
        return optString(key, "");
    }

    /**
     * Get an optional string associated with a key. It returns the defaultValue
     * if there is no such key.
     *
     * @param key          A key string.
     * @param defaultValue The default.
     * @return A string which is the value.
     */
    public String optString(String key, String defaultValue) {
        verifyIsNull();
        Object o = opt(key);
        return o != null ? o.toString() : defaultValue;
    }

    @Override
    public Object put(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null.");
        }
        Object previous = properties.get(key);
        element(String.valueOf(key), value);
        return previous;
    }

    public void putAll(Map map) {
        putAll(map, new JsonConfig());
    }

    public void putAll(Map map, JsonConfig jsonConfig) {
        if (map instanceof JSONObject) {
            for (Iterator entries = map.entrySet()
                    .iterator(); entries.hasNext(); ) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                this.properties.put(key, value);
            }
        } else {
            for (Iterator entries = map.entrySet()
                    .iterator(); entries.hasNext(); ) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = String.valueOf(entry.getKey());
                Object value = entry.getValue();
                element(key, value, jsonConfig);
            }
        }
    }

    @Override
    public Object remove(Object key) {
        return properties.remove(key);
    }

    /**
     * Remove a name and its value, if present.
     *
     * @param key The name to be removed.
     * @return The value that was associated with the name, or null if there was
     * no value.
     */
    public Object remove(String key) {
        verifyIsNull();
        return this.properties.remove(key);
    }

    /**
     * Get the number of keys stored in the JSONObject.
     *
     * @return The number of keys in the JSONObject.
     */
    @Override
    public int size() {
        // verifyIsNull();
        return this.properties.size();
    }

    /**
     * Produce a JSONArray containing the values of the members of this
     * JSONObject.
     *
     * @param names A JSONArray containing a list of key strings. This determines
     *              the sequence of the values in the result.
     * @return A JSONArray of values.
     * @throws JSONException If any of the values are non-finite numbers.
     */
    public JSONArray toJSONArray(JSONArray names) {
        verifyIsNull();
        if (names == null || names.size() == 0) {
            return null;
        }
        JSONArray ja = new JSONArray();
        for (int i = 0; i < names.size(); i += 1) {
            ja.element(this.opt(names.getString(i)));
        }
        return ja;
    }

    /**
     * Make a JSON text of this JSONObject. For compactness, no whitespace is
     * added. If this would not result in a syntactically correct JSON text, then
     * null will be returned instead.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return a printable, displayable, portable, transmittable representation
     * of the object, beginning with <code>{</code>&nbsp;<small>(left
     * brace)</small> and ending with <code>}</code>&nbsp;<small>(right
     * brace)</small>.
     */
    @Override
    public String toString() {
        return JSONBuilderProvider.simplest().toJson(JsonMapper.toJsonTreeNode(this));
    }

    /**
     * Make a prettyprinted JSON text of this JSONObject.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param indentFactor The number of spaces to add to each level of
     *                     indentation.
     * @return a printable, displayable, portable, transmittable representation
     * of the object, beginning with <code>{</code>&nbsp;<small>(left
     * brace)</small> and ending with <code>}</code>&nbsp;<small>(right
     * brace)</small>.
     * @throws JSONException If the object contains an invalid number.
     */
    @Override
    public String toString(int indentFactor) {
        return JSONBuilderProvider.create().prettyFormat(indentFactor > 0).build().toJson(JsonMapper.toJsonTreeNode(this));
    }

    /**
     * Make a prettyprinted JSON text of this JSONObject.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param indentFactor The number of spaces to add to each level of
     *                     indentation.
     * @param indent       The indentation of the top level.
     * @return a printable, displayable, transmittable representation of the
     * object, beginning with <code>{</code>&nbsp;<small>(left brace)</small>
     * and ending with <code>}</code>&nbsp;<small>(right brace)</small>.
     * @throws JSONException If the object contains an invalid number.
     */
    @Override
    public String toString(int indentFactor, int indent) {
        return JSONBuilderProvider.create().prettyFormat(indentFactor > 0 || indent > 0).build().toJson(JsonMapper.toJsonTreeNode(this));
    }

    @Override
    public Collection values() {
        return Collections.unmodifiableCollection(properties.values());
    }

    /**
     * Write the contents of the JSONObject as JSON text to a writer. For
     * compactness, no whitespace is added.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return The writer.
     * @throws JSONException
     */
    @Override
    public Writer write(Writer writer) {
        try {
            writer.write(toString());
        } catch (IOException e) {
            throw new JSONException(e);
        }
        return writer;
    }

    private JSONObject _accumulate(String key, Object value, JsonConfig jsonConfig) {
        if (isNullObject()) {
            throw new JSONException("Can't accumulate on null object");
        }

        if (!has(key)) {
            setInternal(key, value, jsonConfig);
        } else {
            Object o = opt(key);
            if (o instanceof JSONArray) {
                ((JSONArray) o).element(value, jsonConfig);
            } else {
                setInternal(key, new JSONArray().element(o)
                        .element(value, jsonConfig), jsonConfig);
            }
        }

        return this;
    }

    @Override
    protected Object _processValue(Object value, JsonConfig jsonConfig) {
        return JsonMapper.fromJavaObject(value, jsonConfig);
    }

    /**
     * Put a key/value pair in the JSONObject.
     *
     * @param key   A key string.
     * @param value An object which is the value. It should be of one of these
     *              types: Boolean, Double, Integer, JSONArray, JSONObject, Long,
     *              String, or the JSONNull object.
     * @return this.
     * @throws JSONException If the value is non-finite number or if the key is
     *                       null.
     */
    private JSONObject _setInternal(String key, Object value, JsonConfig jsonConfig) {
        verifyIsNull();
        if (key == null) {
            throw new JSONException("Null key.");
        }
        this.properties.put(key, value);
        return this;
    }

    private Object processValue(Object value, JsonConfig jsonConfig) {
        return _processValue(value, jsonConfig);
    }

    private Object processValue(String key, Object value, JsonConfig jsonConfig) {
        return _processValue(value, jsonConfig);
    }

    /**
     * Put a key/value pair in the JSONObject.
     *
     * @param key   A key string.
     * @param value An object which is the value. It should be of one of these
     *              types: Boolean, Double, Integer, JSONArray, JSONObject, Long,
     *              String, or the JSONNull object.
     * @return this.
     * @throws JSONException If the value is non-finite number or if the key is
     *                       null.
     */
    private JSONObject setInternal(String key, Object value, JsonConfig jsonConfig) {
        return _setInternal(key, processValue(key, value, jsonConfig), jsonConfig);
    }

    /**
     * Checks if this object is a "null" object.
     */
    private void verifyIsNull() {
        if (isNullObject()) {
            throw new JSONException("null object");
        }
    }
}
