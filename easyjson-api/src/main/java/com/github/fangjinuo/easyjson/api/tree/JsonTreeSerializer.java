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

package com.github.fangjinuo.easyjson.api.tree;

import com.github.fangjinuo.easyjson.api.JsonException;
import com.github.fangjinuo.easyjson.api.JsonTreeNode;
import com.github.fangjinuo.easyjson.api.tree.stream.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class JsonTreeSerializer {
    private boolean serializeNulls = false;
    private boolean prettyFormat = false;

    public boolean isSerializeNulls() {
        return serializeNulls;
    }

    public void setSerializeNulls(boolean serializeNulls) {
        this.serializeNulls = serializeNulls;
    }

    public boolean isPrettyFormat() {
        return prettyFormat;
    }

    public void setPrettyFormat(boolean prettyFormat) {
        this.prettyFormat = prettyFormat;
    }

    /**
     * Converts a tree of {@link JsonTreeNode}s into its equivalent JSON representation.
     *
     * @param jsonElement root of a tree of {@link JsonTreeNode}s
     * @return JSON String representation of the tree
     * @since 1.4
     */
    public String toJson(JsonTreeNode jsonElement) {
        StringWriter writer = new StringWriter();
        toJson(jsonElement, writer);
        return writer.toString();
    }

    /**
     * Writes out the equivalent JSON for a tree of {@link JsonTreeNode}s.
     *
     * @param jsonElement root of a tree of {@link JsonTreeNode}s
     * @param writer Writer to which the Json representation needs to be written
     * @throws JsonException if there was a problem writing to the writer
     * @since 1.4
     */
    public void toJson(JsonTreeNode jsonElement, Appendable writer) throws JsonException {
        try {
            JsonWriter jsonWriter = newJsonWriter(Streams.writerForAppendable(writer));
            toJson(jsonElement, jsonWriter);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }


    /**
     * Returns a new JSON writer configured for the settings on this JSON instance.
     */
    private JsonWriter newJsonWriter(Writer writer) throws IOException {
        JsonWriter jsonWriter = new JsonWriter(writer);
        if (prettyFormat) {
            jsonWriter.setIndent("  ");
        }
        jsonWriter.setSerializeNulls(serializeNulls);
        return jsonWriter;
    }

    /**
     * Writes the JSON for {@code jsonElement} to {@code writer}.
     * @throws JsonException if there was a problem writing to the writer
     */
    public void toJson(JsonTreeNode jsonElement, JsonWriter writer) throws JsonException {
        boolean oldLenient = writer.isLenient();
        writer.setLenient(true);
        boolean oldHtmlSafe = writer.isHtmlSafe();
        writer.setHtmlSafe(true);
        boolean oldSerializeNulls = writer.getSerializeNulls();
        writer.setSerializeNulls(serializeNulls);
        try {
            Streams.write(jsonElement, writer);
        } catch (IOException e) {
            throw new JsonException(e);
        } catch (AssertionError e) {
            throw new JsonException("AssertionError " + e.getMessage(), e);
        } finally {
            writer.setLenient(oldLenient);
            writer.setHtmlSafe(oldHtmlSafe);
            writer.setSerializeNulls(oldSerializeNulls);
        }
    }

}
