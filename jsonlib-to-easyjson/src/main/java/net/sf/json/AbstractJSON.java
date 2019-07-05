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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Set;


/**
 * Base class for JSONObject and JSONArray.
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
abstract class AbstractJSON {
    private static class CycleSet extends ThreadLocal {
        protected Object initialValue() {
            return new SoftReference(new HashSet());
        }

        public Set getSet() {
            Set set = (Set) ((SoftReference) get()).get();
            if (set == null) {
                set = new HashSet();
                set(new SoftReference(set));
            }
            return set;
        }
    }

    private static CycleSet cycleSet = new CycleSet();

    private static final Logger log = LoggerFactory.getLogger(AbstractJSON.class);

    /**
     * Adds a reference for cycle detection check.
     *
     * @param instance the reference to add
     * @return true if the instance has not been added previously, false
     * otherwise.
     */
    protected static boolean addInstance(Object instance) {
        return getCycleSet().add(instance);
    }

    /**
     * Fires an end of array event.
     */
    protected static void fireArrayEndEvent(JsonConfig jsonConfig) {
    }

    /**
     * Fires a start of array event.
     */
    protected static void fireArrayStartEvent(JsonConfig jsonConfig) {
    }

    /**
     * Fires an element added event.
     *
     * @param index   the index where the element was added
     * @param element the added element
     */
    protected static void fireElementAddedEvent(int index, Object element, JsonConfig jsonConfig) {
    }

    /**
     * Fires an error event.
     *
     * @param jsone the thrown exception
     */
    protected static void fireErrorEvent(JSONException jsone, JsonConfig jsonConfig) {
    }

    /**
     * Fires an end of object event.
     */
    protected static void fireObjectEndEvent(JsonConfig jsonConfig) {
    }

    /**
     * Fires a start of object event.
     */
    protected static void fireObjectStartEvent(JsonConfig jsonConfig) {
    }

    /**
     * Fires a property set event.
     *
     * @param key         the name of the property
     * @param value       the value of the property
     * @param accumulated if the value has been accumulated over 'key'
     */
    protected static void firePropertySetEvent(String key, Object value, boolean accumulated,
                                               JsonConfig jsonConfig) {
    }

    /**
     * Fires a warning event.
     *
     * @param warning the warning message
     */
    protected static void fireWarnEvent(String warning, JsonConfig jsonConfig) {
    }

    /**
     * Removes a reference for cycle detection check.
     */
    protected static void removeInstance(Object instance) {
        Set set = getCycleSet();
        set.remove(instance);
        if (set.size() == 0) {
            cycleSet.remove();
        }
    }

    protected Object _processValue(Object value, JsonConfig jsonConfig) {
        return JsonMapper.fromJavaObject(value, jsonConfig);
    }

    private static Set getCycleSet() {
        return cycleSet.getSet();
    }
}