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

package com.jn.easyjson.tests.entity.struct;


import com.jn.langx.util.BasedStringAccessor;
import com.jn.langx.util.Objects;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class SqlRequestContext<R extends SqlRequest> extends BasedStringAccessor<String, Map<String, Object>> {
    private R request;

    public SqlRequestContext() {
        setTarget(new HashMap<String, Object>());
    }

    public R getRequest() {
        return request;
    }

    @SuppressWarnings("unchecked")
    public void setRequest(R sqlRequest) {
        this.request = sqlRequest;
        this.request.setContext(this);
    }

    @Override
    public Object get(String key) {
        return getTarget().get(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        Object value = getTarget().get(key);
        return value == null ? defaultValue : value.toString();
    }

    @Override
    public void set(String key, Object value) {
        getTarget().put(key, value);
    }

    public boolean isPagingRequest() {
        return false;
    }

    public boolean isOrderByRequest() {
        if (!(this.getRequest() instanceof SelectRequest)) {
            return false;
        }
        SelectRequest request = (SelectRequest) this.getRequest();
        if (!request.needOrderBy()) {
            return false;
        }
        if (request.getOrderByAsString().contains("?")) {
            return false;
        }
        return true;
    }

    public void clear(){
        if(Objects.isNotNull(request)) {
            request.clear();
        }
        request = null;
    }
}
