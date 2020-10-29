/*
 * Copyright 2013-2014 Richard M. Hightower
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * __________                              _____          __   .__
 * \______   \ ____   ____   ____   /\    /     \ _____  |  | _|__| ____    ____
 *  |    |  _//  _ \ /  _ \ /    \  \/   /  \ /  \\__  \ |  |/ /  |/    \  / ___\
 *  |    |   (  <_> |  <_> )   |  \ /\  /    Y    \/ __ \|    <|  |   |  \/ /_/  >
 *  |______  /\____/ \____/|___|  / \/  \____|__  (____  /__|_ \__|___|  /\___  /
 *         \/                   \/              \/     \/     \/       \//_____/
 *      ____.                     ___________   _____    ______________.___.
 *     |    |____ ___  _______    \_   _____/  /  _  \  /   _____/\__  |   |
 *     |    \__  \\  \/ /\__  \    |    __)_  /  /_\  \ \_____  \  /   |   |
 * /\__|    |/ __ \\   /  / __ \_  |        \/    |    \/        \ \____   |
 * \________(____  /\_/  (____  / /_______  /\____|__  /_______  / / ______|
 *               \/           \/          \/         \/        \/  \/
 */

package io.advantageous.boon.json.implementation;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.file.Files;
import com.jn.langx.util.reflect.type.Types;
import io.advantageous.boon.core.Exceptions;
import io.advantageous.boon.core.IO;
import io.advantageous.boon.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;


public class ObjectMapperImpl implements ObjectMapper {
    private static final Logger logger = LoggerFactory.getLogger(ObjectMapperImpl.class);
    private JSON json;

    private final JsonParserFactory parserFactory;
    private final JsonSerializerFactory serializerFactory;

    public ObjectMapperImpl(JsonParserFactory parserFactory, JsonSerializerFactory serializerFactory) {
        this.parserFactory = parserFactory;
        this.serializerFactory = serializerFactory;

        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        jsonBuilder.serializeNulls(serializerFactory.isIncludeNulls());
        jsonBuilder.setLenient(!parserFactory.isStrict());
        json = jsonBuilder.build();

    }

    public ObjectMapperImpl() {
        this(new JsonParserFactory(), new JsonSerializerFactory().useFieldsOnly());
    }


    @Override
    public <T> T readValue(final String src, final Class<T> valueType) {
        return json.fromJson(src, valueType);
    }

    @Override
    public <T> T readValue(File src, Class<T> valueType) {
        try {
            InputStream stream = new FileInputStream(src);
            return readValue(stream, valueType);
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    @Override
    public <T> T readValue(byte[] src, Class<T> valueType) {
        return readValue(new String(src, getCharsetForParse()), valueType);
    }

    @Override
    public <T> T readValue(char[] src, Class<T> valueType) {
        return readValue(new String(src), valueType);
    }

    @Override
    public <T> T readValue(Reader src, Class<T> valueType) {
        try {
            String str = IOs.toString(src);
            return readValue(str, valueType);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public <T> T readValue(InputStream src, Class<T> valueType) {
        try {
            String str = IOs.toString(src, getCharsetForParse());
            return readValue(str, valueType);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public <T extends Collection<C>, C> T readValue(String src, Class<T> valueType, Class<C> componentType) {
        Class<?> type = valueType;
        if (type == List.class) {
            return json.fromJson(src, Types.getListParameterizedType(componentType));
        } else if (type == Set.class) {
            Collection<C> list = json.fromJson(src, Types.getListParameterizedType(componentType));
            return (T) new HashSet(list);
        } else if (type == LinkedHashSet.class) {
            Collection<C> list = json.fromJson(src, Types.getListParameterizedType(componentType));
            return (T) new LinkedHashSet(list);
        } else {
            return json.fromJson(src, Types.getListParameterizedType(componentType));
        }
    }

    @Override
    public <T extends Collection<C>, C> T readValue(File src, Class<T> valueType, Class<C> componentType) {
        try {
            InputStream stream = new FileInputStream(src);
            return readValue(stream, valueType, componentType);
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    @Override
    public <T extends Collection<C>, C> T readValue(byte[] src, Class<T> valueType, Class<C> componentType) {
        return readValue(new String(src, getCharsetForParse()), valueType, componentType);
    }

    @Override
    public <T extends Collection<C>, C> T readValue(char[] src, Class<T> valueType, Class<C> componentType) {
        return readValue(new String(src), valueType, componentType);
    }

    @Override
    public <T extends Collection<C>, C> T readValue(Reader src, Class<T> valueType, Class<C> componentType) {
        try {
            String str = IOs.toString(src);
            return readValue(str, valueType, componentType);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public <T extends Collection<C>, C> T readValue(InputStream src, Class<T> valueType, Class<C> componentType) {
        try {
            String str = IOs.toString(src, getCharsetForParse());
            return readValue(str, valueType, componentType);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public <T extends Collection<C>, C> T readValue(byte[] src, Charset charset, Class<T> valueType, Class<C> componentType) {
        return readValue(new String(src, getCharsetForParse(charset)), valueType);
    }

    private Charset getCharsetForParse() {
        return getCharsetForParse(null);
    }

    private Charset getCharsetForParse(Charset charset) {
        if (charset != null) {
            return charset;
        }
        if (parserFactory.getCharset() != null) {
            return parserFactory.getCharset();
        }
        return Charsets.UTF_8;
    }

    @Override
    public <T extends Collection<C>, C> T readValue(InputStream src, Charset charset, Class<T> valueType, Class<C> componentType) {
        try {
            String str = IOs.toString(src, getCharsetForParse(charset));
            return readValue(str, valueType);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void writeValue(File dest, Object value) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = Files.openOutputStream(dest, true);
            IOs.write(json.toJson(value), fileOutputStream);
        } catch (IOException ex) {
            Throwables.throwAsRuntimeException(ex);
        } finally {
            IOs.close(fileOutputStream);
        }
    }

    @Override
    public void writeValue(OutputStream dest, Object value) {
        IO.writeNoClose(dest, json.toJson(value));
    }

    @Override
    public void writeValue(Writer dest, Object value) {
        char[] chars = json.toJson(value).toCharArray();
        try {
            dest.write(chars);
        } catch (IOException e) {
            Exceptions.handle(e);
        }
    }

    @Override
    public String writeValueAsString(Object value) {
        return json.toJson(value);
    }

    @Override
    public char[] writeValueAsCharArray(Object value) {
        return json.toJson(value).toCharArray();
    }

    @Override
    public byte[] writeValueAsBytes(Object value) {
        return json.toJson(value).getBytes(Charsets.UTF_8);
    }

    @Override
    public byte[] writeValueAsBytes(Object value, Charset charset) {
        return json.toJson(value).getBytes(charset);
    }

    @Override
    public JsonParserAndMapper parser() {
        return parserFactory.create();
    }

    @Override
    public JsonSerializer serializer() {
        return serializerFactory.create();
    }

    @Override
    public String toJson(Object value) {
        return this.writeValueAsString(value);
    }

    @Override
    public void toJson(Object value, Appendable appendable) {
        try {
            appendable.append(this.writeValueAsString(value));
        } catch (IOException e) {
            Exceptions.handle(e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        return readValue(json, clazz);
    }

    @Override
    public <T> T fromJson(byte[] bytes, Class<T> clazz) {
        return readValue(bytes, clazz);
    }

    @Override
    public <T> T fromJson(char[] chars, Class<T> clazz) {
        return readValue(chars, clazz);
    }

    @Override
    public <T> T fromJson(Reader reader, Class<T> clazz) {
        return readValue(reader, clazz);
    }

    @Override
    public <T> T fromJson(InputStream inputStream, Class<T> clazz) {
        return readValue(inputStream, clazz);
    }

    @Override
    public Object fromJson(String json) {
        return parser().parse(json);
    }

    @Override
    public Object fromJson(Reader reader) {
        return parser().parse(reader);
    }

    @Override
    public Object fromJson(byte[] bytes) {
        return parser().parse(bytes);
    }

    @Override
    public Object fromJson(char[] chars) {
        return parser().parse(chars);
    }

    @Override
    public Object fromJson(InputStream reader) {
        return parser().parse(reader);
    }
}
