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

package com.jn.easyjson.tests.examples.struct;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import java.io.Serializable;
import java.util.Comparator;

public class OrderByItem implements Serializable {
    @NonNull
    private String expression;
    @Nullable
    private OrderByType type;

    /**
     * just for memory pagination
     */
    @Nullable
    private Comparator comparator;

    public OrderByItem() {
    }

    public OrderByItem(String expression) {
        this();
        setExpression(expression);
    }

    public OrderByItem(String expression, OrderByType type) {
        this(expression);
        this.type = type;
    }

    public OrderByItem(String expression, boolean asc) {
        this(expression, asc ? OrderByType.ASC : OrderByType.DESC);
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public OrderByType getType() {
        return type;
    }

    public void setType(OrderByType type) {
        this.type = type;
    }

    public Comparator getComparator() {
        return comparator;
    }

    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    @Override
    public String toString() {
        return " " + expression + " " + (type != null ? type.name() : "");
    }
}
