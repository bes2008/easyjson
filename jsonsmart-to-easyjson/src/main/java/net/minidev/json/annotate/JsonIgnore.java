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

package net.minidev.json.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * block access to a field or to a getter or to a setter.
 * <p>
 * If field and getter are annotate with @JsonIgnore the field will be Writable
 * only
 * <p>
 * <p>
 * If field and setter are annotate with @JsonIgnore the field will be Readable
 * only
 * <p>
 * <p>
 * If getter and setter are annotate with @JsonIgnore the field will be
 * Read/Write using field if the field is public (default )
 *
 * @author uriel
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JsonSmartAnnotation
public @interface JsonIgnore {
    boolean value() default true;
}
