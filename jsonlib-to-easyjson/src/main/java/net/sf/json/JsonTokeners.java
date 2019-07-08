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

package net.sf.json;

import net.sf.json.util.JSONTokener;

public class JsonTokeners {
    public static String readToString(JSONTokener jsonTokener) {
        if (jsonTokener == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(255);
        while (jsonTokener.more()) {
            stringBuilder.append(jsonTokener.next());
        }
        return stringBuilder.toString();
    }
}
