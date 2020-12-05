/*
 * Copyright 2020 the original author or authors.
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

package com.jn.easyjson.fastjson.ext;

import com.alibaba.fastjson.util.FieldInfo;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

public class FieldInfos {
    public static boolean isMethod(@NonNull FieldInfo info) {
        Preconditions.checkNotNull(info);
        return info.field == null && info.method != null;
    }

    public static boolean isField(@NonNull FieldInfo info) {
        Preconditions.checkNotNull(info);
        return info.field != null;
    }
}
