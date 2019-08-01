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

package net.minidev.json;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilderProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A JSON array. JSONObject supports java.util.List interface.
 *
 * @author FangYidong &lt;fangyidong@yahoo.com.cn&gt;
 * @author Uriel Chemouni &lt;uchemouni@gmail.com&gt;
 */
public class JSONArray extends ArrayList<Object> implements List<Object>, JSONAwareEx, JSONStreamAwareEx {
    private static final long serialVersionUID = 9106884089231309568L;

    public static String toJSONString(List<? extends Object> list) {
        return toJSONString(list, JSONValue.COMPRESSION);
    }

    /**
     * Convert a list to JSON text. The result is a JSON array. If this list is
     * also a JSONAware, JSONAware specific behaviours will be omitted at this
     * top level.
     *
     * @param list
     * @param compression Indicate compression level
     * @return JSON text, or "null" if list is null.
     * @see net.minidev.json.JSONValue#toJSONString(Object)
     */
    public static String toJSONString(List<? extends Object> list, JSONStyle compression) {
        StringBuilder sb = new StringBuilder();
        try {
            writeJSONString(list, sb, compression);
        } catch (IOException e) {
            // Can not append on a string builder
        }
        return sb.toString();
    }

    /**
     * Encode a list into JSON text and write it to out. If this list is also a
     * JSONStreamAware or a JSONAware, JSONStreamAware and JSONAware specific
     * behaviours will be ignored at this top level.
     *
     * @see JSONValue#writeJSONString(Object, Appendable)
     */
    public static void writeJSONString(Iterable<? extends Object> list, Appendable out, JSONStyle compression)
            throws IOException {
        if (list == null) {
            out.append("null");
            return;
        }
        JSON json = JSONBuilderProvider.create().serializeNulls(!compression.ignoreNull()).prettyFormat(compression.indent()).build();
        out.append(json.toJson(JsonMapper.toJsonTreeNode(list)));
    }

    public static void writeJSONString(List<? extends Object> list, Appendable out) throws IOException {
        writeJSONString(list, out, JSONValue.COMPRESSION);
    }

    /**
     * Appends the specified element and returns this.
     * Handy alternative to add(E e) method.
     *
     * @param element element to be appended to this array.
     * @return this
     */
    public JSONArray appendElement(Object element) {
        add(element);
        return this;
    }

    public void merge(Object o2) {
        JSONObject.merge(this, o2);
    }

    /**
     * Explicitely Serialize Object as JSon String
     */
    @Override
    public String toJSONString() {
        return toJSONString(this, JSONValue.COMPRESSION);
    }

    @Override
    public String toJSONString(JSONStyle compression) {
        return toJSONString(this, compression);
    }

    /**
     * Override natif toStirng()
     */
    @Override
    public String toString() {
        return toJSONString();
    }

    /**
     * JSONAwareEx inferface
     *
     * @param compression compression param
     */
    public String toString(JSONStyle compression) {
        return toJSONString(compression);
    }

    @Override
    public void writeJSONString(Appendable out) throws IOException {
        writeJSONString(this, out, JSONValue.COMPRESSION);
    }

    @Override
    public void writeJSONString(Appendable out, JSONStyle compression) throws IOException {
        writeJSONString(this, out, compression);
    }
}
