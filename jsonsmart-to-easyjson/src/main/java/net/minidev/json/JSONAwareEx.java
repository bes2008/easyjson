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

package net.minidev.json;

/**
 * Beans that support advanced output of JSON text shall implement this interface.
 * <p>
 * Adding compressions and formating features
 *
 * @author Uriel Chemouni &lt;uchemouni@gmail.com&gt;
 */

public interface JSONAwareEx extends JSONAware {
    /**
     * @return JSON text
     */
    String toJSONString(JSONStyle compression);
}
