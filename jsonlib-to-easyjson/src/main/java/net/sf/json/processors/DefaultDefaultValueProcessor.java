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

package net.sf.json.processors;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.util.JSONUtils;

/**
 * Base implementation for DefaultDefaultValueProcessor.
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class DefaultDefaultValueProcessor implements DefaultValueProcessor {
    @Override
    public Object getDefaultValue(Class type) {
        if (JSONUtils.isArray(type)) {
            return new JSONArray();
        } else if (JSONUtils.isNumber(type)) {
            if (JSONUtils.isDouble(type)) {
                return new Double(0);
            } else {
                return new Integer(0);
            }
        } else if (JSONUtils.isBoolean(type)) {
            return Boolean.FALSE;
        } else if (JSONUtils.isString(type)) {
            return "";
        }
        return JSONNull.getInstance();
    }
}