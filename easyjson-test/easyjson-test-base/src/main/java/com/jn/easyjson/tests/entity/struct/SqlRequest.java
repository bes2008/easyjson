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


import com.jn.easyjson.core.annotation.Ignore;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("rawtypes")
public class SqlRequest<R extends SqlRequest, C extends SqlRequestContext<R>> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * the dialect, every Dialect is also a likeEscaper;
     */
    private String dialect;
    /**
     * the customer likeEscaper
     */
    private transient LikeEscaper likeEscaper;

    private List<Integer> likeParameterIndexes = null;


    private Boolean escapeLikeParameter; // will be used for prepared statement
    @Ignore
    private C context;


    private Integer fetchSize;
    private int maxRows = -1;

    public String getDialect() {
        return dialect;
    }

    @SuppressWarnings("unchecked")
    public R setDialect(String dialect) {
        this.dialect = dialect;
        return (R) this;
    }

    public Boolean getEscapeLikeParameter() {
        return escapeLikeParameter == Boolean.TRUE;
    }

    @SuppressWarnings("unchecked")
    public R setEscapeLikeParameter(Boolean escapeLikeParameter) {
        this.escapeLikeParameter = escapeLikeParameter;
        return (R) this;
    }

    public LikeEscaper getLikeEscaper() {
        return likeEscaper;
    }

    @SuppressWarnings("unchecked")
    public R setLikeEscaper(LikeEscaper likeEscaper) {
        this.likeEscaper = likeEscaper;
        return (R) this;
    }

    public C getContext() {
        return context;
    }

    @SuppressWarnings("unchecked")
    public R setContext(C context) {
        this.context = context;
        return (R) this;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public Integer getFetchSize() {
        return this.fetchSize;
    }

    @SuppressWarnings("unchecked")
    public R setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return (R) this;
    }

    public boolean isPagingRequest() {
        return false;
    }

    public List<Integer> getLikeParameterIndexes() {
        return likeParameterIndexes;
    }

    public void setLikeParameterIndexes(List<Integer> likeParameterIndexes) {
        this.likeParameterIndexes = likeParameterIndexes;
    }

    public void clear() {
        likeEscaper = null;
        context = null;
    }
}
