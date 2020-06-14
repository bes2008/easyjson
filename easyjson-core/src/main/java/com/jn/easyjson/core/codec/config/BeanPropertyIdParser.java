/*
 * Copyright 2020 the original author or authors.
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

package com.jn.easyjson.core.codec.config;

import com.jn.langx.Parser;
import com.jn.langx.util.Strings;

public class BeanPropertyIdParser implements Parser<String, BeanPropertyId> {
    @Override
    public BeanPropertyId parse(String qualifiedId) {
        String[] segments = Strings.split(qualifiedId, "#");
        if (segments.length == 0) {
            return null;
        }
        BeanPropertyId propertyId = new BeanPropertyId();
        propertyId.setBeanClass(segments[0]);
        if (segments.length > 1) {
            propertyId.setPropertyName(segments[1]);
        }
        return propertyId;
    }
}
