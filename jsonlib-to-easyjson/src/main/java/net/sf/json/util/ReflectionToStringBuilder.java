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

package net.sf.json.util;

import java.util.ArrayList;
import java.util.Collection;

public class ReflectionToStringBuilder {
    public static String[] toNoNullStringArray(Collection collection) {
        return collection == null ? new String[0] : toNoNullStringArray(collection.toArray());
    }

    public static String[] toNoNullStringArray(Object[] array) {
        ArrayList list = new ArrayList(array.length);

        for (int i = 0; i < array.length; ++i) {
            Object e = array[i];
            if (e != null) {
                list.add(e.toString());
            }
        }

        return (String[]) ((String[]) list.toArray(new String[0]));
    }
}
