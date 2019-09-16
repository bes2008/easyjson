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

package com.alibaba.fastjson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @since 1.2.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JSONPOJOBuilder {
    /**
     * Property to use for re-defining which zero-argument method
     * is considered the actual "build-method": method called after
     * all data has been bound, and the actual instance needs to
     * be instantiated.
     * <p>
     * Default value is "build".
     */
    public String buildMethod() default "build";

    /**
     * Property used for (re)defining name prefix to use for
     * auto-detecting "with-methods": methods that are similar to
     * "set-methods" (in that they take an argument), but that
     * may also return the new builder instance to use
     * (which may be 'this', or a new modified builder instance).
     * Note that in addition to this prefix, it is also possible
     * to use {@link JSONField}
     * annotation to indicate "with-methods".
     * <p>
     * Default value is "with", so that method named "withValue()"
     * would be used for binding JSON property "value" (using type
     * indicated by the argument; or one defined with annotations.
     */
    public String withPrefix() default "with";

}
