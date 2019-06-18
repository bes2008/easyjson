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

package com.github.fangjinuo.easyjson.core.exclusion;

import com.github.fangjinuo.easyjson.core.annotation.Ignore;
import com.github.fangjinuo.easyjson.core.util.FieldAttributes;

public class IgnoreAnnotationExclusion implements Exclusion{
    @Override
    public boolean shouldSkipField(FieldAttributes f, boolean serialize) {
        Ignore ignore = f.getAnnotation(Ignore.class);
        if(ignore==null) {
            return false;
        }
        return (serialize && ignore.write()) || (!serialize && ignore.read());
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz, boolean serialize) {
        return false;
    }
}
