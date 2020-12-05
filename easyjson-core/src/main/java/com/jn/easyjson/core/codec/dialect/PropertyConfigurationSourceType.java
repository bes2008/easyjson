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

package com.jn.easyjson.core.codec.dialect;

import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

public enum PropertyConfigurationSourceType implements CommonEnum {
    FIELD(0, "field", "字段"),
    GETTER(1, "getter", "get方法"),
    SETTER(2, "setter", "set方法");
    private EnumDelegate delegate;

    private PropertyConfigurationSourceType(int code, String name, String displayText) {
        delegate = new EnumDelegate(code, name, displayText);
    }

    @Override
    public int getCode() {
        return delegate.getCode();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getDisplayText() {
        return delegate.getDisplayText();
    }
}
