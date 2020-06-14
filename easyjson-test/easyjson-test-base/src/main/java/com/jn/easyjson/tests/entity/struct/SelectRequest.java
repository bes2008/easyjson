/*
 * Copyright 2020 the original author or authors.
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

package com.jn.easyjson.tests.entity.struct;


import com.jn.langx.util.Objects;

@SuppressWarnings("rawtypes")
public class SelectRequest<R extends SelectRequest, C extends SqlRequestContext<R>> extends SqlRequest<R, C> {
    /** serialVersionUID **/
    private static final long serialVersionUID = 1L;
    private OrderBy orderBy;
    private int timeout;

    public String getOrderByAsString() {
        return Objects.isNull(this.orderBy) ? "" : this.orderBy.toString();
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public SelectRequest<R, C> setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public boolean needOrderBy() {
        if (this.orderBy == null || !this.orderBy.isValid()) {
            return false;
        }
        return true;
    }


    public int getTimeout() {
        return this.timeout;
    }

    public SelectRequest<R, C> setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
}
