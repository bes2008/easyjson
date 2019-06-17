/*
 *  Copyright 2019 the original author or authors.
 *
 *  Licensed under the LGPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.github.fangjinuo.easyjson.api;

import com.github.fangjinuo.easyjson.api.util.FieldAttributes;

public interface Exclusion {

    /**
     * @param f the field object that is under test
     * @return true if the field should be ignored; otherwise false
     */
    public boolean shouldSkipField(FieldAttributes f);

    /**
     * @param clazz the class object that is under test
     * @return true if the class should be ignored; otherwise false
     */
    public boolean shouldSkipClass(Class<?> clazz);
}
