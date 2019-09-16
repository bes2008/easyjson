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

package com.alibaba.fastjson.support.spring;

/**
 * 一个简单的PO对象，包含原始输出对象和对应的过滤条件{@link PropertyPreFilters}
 *
 * @author yanquanyu
 * @author liuming
 */
public class FastJsonContainer {
    private Object value;
    private PropertyPreFilters filters;

    FastJsonContainer(Object body) {
        this.value = body;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public PropertyPreFilters getFilters() {
        return this.filters;
    }

    public void setFilters(PropertyPreFilters filters) {
        this.filters = filters;
    }
}
