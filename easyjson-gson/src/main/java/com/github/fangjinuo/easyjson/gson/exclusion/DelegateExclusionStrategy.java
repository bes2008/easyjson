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

package com.github.fangjinuo.easyjson.gson.exclusion;

import com.github.fangjinuo.easyjson.core.exclusion.Exclusion;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class DelegateExclusionStrategy implements ExclusionStrategy {
    private Exclusion exclusion;
    private boolean serialize;

    public DelegateExclusionStrategy(Exclusion exclusion, boolean serialize) {
        this.exclusion = exclusion;
        this.serialize = serialize;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return exclusion.shouldSkipField(new GsonFieldAttributesAdapter(f, null), serialize);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return exclusion.shouldSkipClass(clazz, serialize);
    }
}
