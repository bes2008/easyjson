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
package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.easyjson.FastEasyJsons;
import com.alibaba.fastjson.util.IOUtils;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPOutputStream;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class JSONSerializer extends SerializeFilterable {

    public final SerializeWriter out;
    protected final SerializeConfig config;
    protected IdentityHashMap<Object, SerialContext> references = null;
    protected SerialContext context;
    protected TimeZone timeZone = JSON.defaultTimeZone;
    protected Locale locale = JSON.defaultLocale;
    private int indentCount = 0;
    private String indent = "\t";
    private String dateFormatPattern;
    private DateFormat dateFormat;

    public JSONSerializer() {
        this(new SerializeWriter(), SerializeConfig.getGlobalInstance());
    }

    public JSONSerializer(SerializeWriter out) {
        this(out, SerializeConfig.getGlobalInstance());
    }

    public JSONSerializer(SerializeConfig config) {
        this(new SerializeWriter(), config);
    }

    public JSONSerializer(SerializeWriter out, SerializeConfig config) {
        this.out = out;
        this.config = config;
    }

    public static void write(Writer out, Object object) {
        SerializeWriter writer = new SerializeWriter();
        try {
            JSONSerializer serializer = new JSONSerializer(writer);
            serializer.write(object);
            writer.writeTo(out);
        } catch (IOException ex) {
            throw new JSONException(ex.getMessage(), ex);
        } finally {
            writer.close();
        }
    }


    public static void write(SerializeWriter out, Object object) {
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.write(object);
    }

    public String getDateFormatPattern() {
        if (dateFormat instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) dateFormat).toPattern();
        }
        return dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (dateFormat == null) {
            if (dateFormatPattern != null) {
                dateFormat = new SimpleDateFormat(dateFormatPattern, locale);
                dateFormat.setTimeZone(timeZone);
            }
        }

        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormatPattern = dateFormat;
        if (this.dateFormat != null) {
            this.dateFormat = null;
        }
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
        if (dateFormatPattern != null) {
            dateFormatPattern = null;
        }
    }

    public SerialContext getContext() {
        return context;
    }

    public void setContext(SerialContext context) {
        this.context = context;
    }

    public void setContext(SerialContext parent, Object object, Object fieldName, int features) {
        this.setContext(parent, object, fieldName, features, 0);
    }

    public void setContext(SerialContext parent, Object object, Object fieldName, int features, int fieldFeatures) {
        if (out.disableCircularReferenceDetect) {
            return;
        }

        this.context = new SerialContext(parent, object, fieldName, features, fieldFeatures);
        if (references == null) {
            references = new IdentityHashMap<Object, SerialContext>();
        }
        this.references.put(object, context);
    }

    public void setContext(Object object, Object fieldName) {
        this.setContext(context, object, fieldName, 0);
    }

    public void popContext() {
        if (context != null) {
            this.context = this.context.parent;
        }
    }

    public final boolean isWriteClassName(Type fieldType, Object obj) {
        return out.isEnabled(SerializerFeature.WriteClassName) //
                && (fieldType != null //
                || (!out.isEnabled(SerializerFeature.NotWriteRootClassName)) //
                || (context != null && (context.parent != null)));
    }

    public boolean containsReference(Object value) {
        if (references == null) {
            return false;
        }

        SerialContext refContext = references.get(value);
        if (refContext == null) {
            return false;
        }

        if (value == Collections.emptyMap()) {
            return false;
        }

        Object fieldName = refContext.fieldName;

        return fieldName == null || fieldName instanceof Integer || fieldName instanceof String;
    }

    public void writeReference(Object object) {
        SerialContext context = this.context;
        Object current = context.object;

        if (object == current) {
            out.write("{\"$ref\":\"@\"}");
            return;
        }

        SerialContext parentContext = context.parent;

        if (parentContext != null) {
            if (object == parentContext.object) {
                out.write("{\"$ref\":\"..\"}");
                return;
            }
        }

        SerialContext rootContext = context;
        for (; ; ) {
            if (rootContext.parent == null) {
                break;
            }
            rootContext = rootContext.parent;
        }

        if (object == rootContext.object) {
            out.write("{\"$ref\":\"$\"}");
        } else {
            out.write("{\"$ref\":\"");
            String path = references.get(object).toString();
            out.write(path);
            out.write("\"}");
        }
    }

    public boolean checkValue(SerializeFilterable filterable) {
        return (valueFilters != null && valueFilters.size() > 0) //
                || (contextValueFilters != null && contextValueFilters.size() > 0) //
                || (filterable.valueFilters != null && filterable.valueFilters.size() > 0)
                || (filterable.contextValueFilters != null && filterable.contextValueFilters.size() > 0)
                || out.writeNonStringValueAsString;
    }

    public boolean hasNameFilters(SerializeFilterable filterable) {
        return (nameFilters != null && nameFilters.size() > 0) //
                || (filterable.nameFilters != null && filterable.nameFilters.size() > 0);
    }

    public boolean hasPropertyFilters(SerializeFilterable filterable) {
        return (propertyFilters != null && propertyFilters.size() > 0) //
                || (filterable.propertyFilters != null && filterable.propertyFilters.size() > 0);
    }

    public int getIndentCount() {
        return indentCount;
    }

    public void incrementIndent() {
        indentCount++;
    }

    public void decrementIdent() {
        indentCount--;
    }

    public void println() {
        out.write('\n');
        for (int i = 0; i < indentCount; ++i) {
            out.write(indent);
        }
    }

    public SerializeWriter getWriter() {
        return out;
    }

    public String toString() {
        return out.toString();
    }

    public void config(SerializerFeature feature, boolean state) {
        out.config(feature, state);
    }

    public boolean isEnabled(SerializerFeature feature) {
        return out.isEnabled(feature);
    }

    public void writeNull() {
        this.out.writeNull();
    }

    public SerializeConfig getMapping() {
        return config;
    }

    public final void write(Object object) {
        String json = FastEasyJsons.getJsonBuilder(this.out.getFeatures()).serializeUseDateFormat(this.getDateFormat()).build().toJson(object);
        this.out.write(json);
    }

    /**
     * @since 1.2.57
     */
    public final void writeAs(Object object, Class type) {
        String json = FastEasyJsons.getJsonBuilder(this.out.getFeatures()).serializeUseDateFormat(this.getDateFormat()).build().toJson(object, type);
        this.out.write(json);
    }

    public final void writeWithFieldName(Object object, Object fieldName) {
        writeWithFieldName(object, fieldName, null, 0);
    }

    protected final void writeKeyValue(char seperator, String key, Object value) {
        write(value);
    }

    public final void writeWithFieldName(Object object, Object fieldName, Type fieldType, int fieldFeatures) {
        write(object);
    }

    public final void writeWithFormat(Object object, String format) {
        write(object);
    }

    public final void write(String text) {
        write(text);
    }

    public ObjectSerializer getObjectWriter(Class<?> clazz) {
        return config.getObjectWriter(clazz);
    }

    public void close() {
        this.out.close();
    }

}
