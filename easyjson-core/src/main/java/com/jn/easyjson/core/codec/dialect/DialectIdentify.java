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

package com.jn.easyjson.core.codec.dialect;

import com.jn.langx.util.Objects;
import com.jn.langx.util.hash.HashCodeBuilder;

public final class DialectIdentify {
    private String id;
    private String libUrl;

    public DialectIdentify(){}

    public DialectIdentify(String id, String libUrl){
        setId(id);
        setLibUrl(libUrl);
    }

    public boolean isFastjson(){
        return "fastjson".equals(id);
    }

    public boolean isGson(){
        return "gson".equals(id);
    }

    public boolean isJackson(){
        return "jackson".equals(id);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibUrl() {
        return this.libUrl;
    }

    public void setLibUrl(String libUrl) {
        this.libUrl = libUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DialectIdentify)) return false;

        DialectIdentify that = (DialectIdentify) o;

        if (!Objects.equals(id, that.id)) {
            return false;
        }

        if (!Objects.equals(libUrl, that.libUrl)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().with(id).with(libUrl).build();
    }
}
