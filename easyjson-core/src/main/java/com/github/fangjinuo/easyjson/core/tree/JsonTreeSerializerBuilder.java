/*
 *  Copyright 2019 the original author or authors.
 *
 *  Licensed under the LGPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.github.fangjinuo.easyjson.core.tree;

public class JsonTreeSerializerBuilder {
    private boolean serializeNulls = false;
    private boolean prettyFormat = false;

    public JsonTreeSerializerBuilder setSerializeNulls(boolean serializeNulls) {
        this.serializeNulls = serializeNulls;
        return this;
    }

    public JsonTreeSerializerBuilder setPrettyFormat(boolean prettyFormat) {
        this.prettyFormat = prettyFormat;
        return this;
    }

    public JsonTreeSerializer build(){
        JsonTreeSerializer writer = new JsonTreeSerializer();
        writer.setPrettyFormat(prettyFormat);
        writer.setSerializeNulls(serializeNulls);
        return writer;
    }
}
