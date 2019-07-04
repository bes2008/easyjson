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

import java.util.Set;

/**
 * Base class for finding a matching JsonBeanProcessor.<br>
 * <ul>
 * <li>DEFAULT - matches the target class with equals().</li>
 * </ul>
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public abstract class JsonBeanProcessorMatcher {
    /**
     * Matches the target with equals()
     */
    public static final JsonBeanProcessorMatcher DEFAULT = new DefaultJsonBeanProcessorMatcher();

    /**
     * Returns the matching class calculated with the target class and the
     * provided set.
     *
     * @param target the target class to match
     * @param set    a set of possible matches
     */
    public abstract Object getMatch(Class target, Set set);

    private static final class DefaultJsonBeanProcessorMatcher extends JsonBeanProcessorMatcher {
        @Override
        public Object getMatch(Class target, Set set) {
            if (target != null && set != null && set.contains(target)) {
                return target;
            }
            return null;
        }
    }
}