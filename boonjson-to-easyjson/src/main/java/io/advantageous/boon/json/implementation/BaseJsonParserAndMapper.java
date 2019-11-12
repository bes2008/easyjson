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
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.PrimitiveArrays;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.reflect.type.Types;
import io.advantageous.boon.core.*;
import io.advantageous.boon.core.reflection.Mapper;
import io.advantageous.boon.json.JsonParser;
import io.advantageous.boon.json.JsonParserAndMapper;
import io.advantageous.boon.primitive.CharBuf;
import io.advantageous.boon.primitive.InMemoryInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaseJsonParserAndMapper implements JsonParserAndMapper {
    private static final Logger logger = LoggerFactory.getLogger(BaseJsonParserAndMapper.class);
    private JSON json;
    protected final JsonParser parser;
    private final CharBuf builder = CharBuf.create(20);
    private final Mapper mapper;

    @Override
    public Charset getCharset() {
        return charset == null ? parser.getCharset() : charset;
    }

    protected Charset charset = Charsets.UTF_8;


    protected int bufSize = 1024;
    private char[] copyBuf;


    public BaseJsonParserAndMapper(JsonParser parser, Mapper mapper) {
        this.parser = parser;
        this.mapper = mapper;
        json = JSONBuilderProvider.simplest();
    }


    protected final <T> T convert(Class<T> clz, Object object) {
        if (object == null) {
            return null;
        }

        TypeType coerceTo = TypeType.getType(clz);

        switch (coerceTo) {
            case MAP:
            case LIST:
            case OBJECT:
                return (T) object;

        }

        TypeType coerceFrom = TypeType.getType(object.getClass());

        switch (coerceFrom) {

            case VALUE_MAP:
                return mapper.fromValueMap((Map<String, Value>) object, clz);

            case MAP:
                return mapper.fromMap((Map<String, Object>) object, clz);

            case VALUE:
                return (T) ((Value) object).toValue();

            case LIST:
                return (T) mapper.convertListOfMapsToObjects((List<Map>) object, clz);

            default:
                if (Typ.isBasicTypeOrCollection(clz)) {
                    return Conversions.coerce(coerceTo, clz, object);
                } else {
                    return (T) object;
                }
        }

    }


    public void setCharset(Charset charset) {
        this.charset = charset;
    }


    @Override
    public Map<String, Object> parseMap(String jsonString) {
        return (Map<String, Object>) parse(jsonString);
    }

    @Override
    public <T> List<T> parseList(Class<T> componentType, String jsonString) {
        return json.fromJson(jsonString, Types.getListParameterizedType(componentType));
    }


    @Override
    public <T> List<T> parseList(Class<T> componentType, Reader reader) {
        try {
            String str = IOs.toString(reader);
            return parseList(componentType, str);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private Charset getCharsetForParse() {
        return getCharsetForParse(null);
    }

    private Charset getCharsetForParse(Charset charset) {
        if (charset != null) {
            return charset;
        }
        if (charset != null) {
            return charset;
        }

        if (parser.getCharset() != null) {
            return parser.getCharset();
        }
        return Charsets.UTF_8;
    }

    @Override
    public <T> List<T> parseList(Class<T> componentType, InputStream input) {
        try {
            String str = IOs.toString(input, getCharsetForParse());
            return parseList(componentType, str);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public <T> List<T> parseList(Class<T> componentType, InputStream input, Charset charset) {
        try {
            String str = IOs.toString(input, getCharsetForParse(charset));
            return parseList(componentType, str);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public <T> List<T> parseList(Class<T> componentType, byte[] jsonBytes) {
        return parseList(componentType, new String(jsonBytes, getCharsetForParse()));
    }

    @Override
    public <T> List<T> parseList(Class<T> componentType, byte[] jsonBytes, Charset charset) {
        return parseList(componentType, new String(jsonBytes, getCharsetForParse(charset)));
    }

    @Override
    public <T> List<T> parseList(Class<T> componentType, char[] chars) {
        return parseList(componentType, new String(chars));
    }

    @Override
    public <T> List<T> parseList(Class<T> componentType, CharSequence jsonSeq) {
        return parseList(componentType, jsonSeq.toString());
    }

    public <T> T parseClassFromFile(Class<T> clazz, String fileName) {
        try {
            InputStream stream = new FileInputStream(fileName);
            return parse(clazz, stream);
        } catch (FileNotFoundException ex) {
            return null;
        }
    }


    @Override
    public <T> List<T> parseListFromFile(Class<T> componentType, String fileName) {
        try {
            InputStream stream = new FileInputStream(fileName);
            return parseList(componentType, stream);
        } catch (FileNotFoundException ex) {
            return null;
        }
    }


    @Override
    public <T> T parse(Class<T> type, String jsonString) {
        return json.fromJson(jsonString, type);
    }

    @Override
    public <T> T parse(Class<T> type, byte[] bytes) {
        return parse(type, new String(bytes, getCharsetForParse()));
    }

    @Override
    public <T> T parse(Class<T> type, byte[] bytes, Charset charset) {
        return parse(type, new String(bytes, getCharsetForParse(charset)));
    }


    @Override
    public Date parseDate(String jsonString) {
        return parse(Date.class, jsonString);
    }

    @Override
    public Date parseDate(InputStream input) {
        try {
            String str = IOs.toString(input, getCharsetForParse());
            return parseDate(str);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Date parseDate(InputStream input, Charset charset) {
        try {
            String str = IOs.toString(input, getCharsetForParse(charset));
            return parseDate(str);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Date parseDate(byte[] jsonBytes) {
        return parseDate(new String(jsonBytes, getCharsetForParse()));
    }

    @Override
    public Date parseDate(byte[] jsonBytes, Charset charset) {
        return parseDate(new String(jsonBytes, getCharsetForParse(charset)));
    }

    @Override
    public Date parseDate(char[] chars) {
        return parseDate(new String(chars));
    }

    @Override
    public Date parseDate(CharSequence jsonSeq) {
        return parseDate(jsonSeq.toString());
    }

    @Override
    public Date parseDateFromFile(String fileName) {
        try {
            InputStream stream = new FileInputStream(fileName);
            return parseDate(stream);
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    @Override
    public float[] parseFloatArray(String jsonString) {
        List<Float> list = json.fromJson(jsonString, Types.getListParameterizedType(Float.class));
        return PrimitiveArrays.unwrap(Collects.toArray(list, Float[].class), true);
    }

    @Override
    public double[] parseDoubleArray(String jsonString) {
        List<Double> list = json.fromJson(jsonString, Types.getListParameterizedType(Double.class));
        return PrimitiveArrays.unwrap(Collects.toArray(list, Double[].class), true);
    }

    @Override
    public long[] parseLongArray(String jsonString) {
        List<Long> list = json.fromJson(jsonString, Types.getListParameterizedType(Long.class));
        return PrimitiveArrays.unwrap(Collects.toArray(list, Long[].class), true);
    }

    @Override
    public int[] parseIntArray(String jsonString) {
        List<Integer> list = json.fromJson(jsonString, Types.getListParameterizedType(Integer.class));
        return PrimitiveArrays.unwrap(Collects.toArray(list, Integer[].class), true);
    }

    @Override
    public <T extends Enum> T parseEnum(Class<T> type, String jsonString) {
        return json.fromJson(jsonString, type);
    }

    @Override
    public short parseShort(String jsonString) {
        return json.fromJson(jsonString, Short.class);
    }

    @Override
    public byte parseByte(String jsonString) {
        return json.fromJson(jsonString, Byte.class);
    }

    @Override
    public char parseChar(String jsonString) {
        return json.fromJson(jsonString, Character.class);
    }

    @Override
    public char[] parseCharArray(String jsonString) {
        List<Character> list = json.fromJson(jsonString, Types.getListParameterizedType(Character.class));
        return PrimitiveArrays.unwrap(Collects.toArray(list, Character[].class), true);
    }

    @Override
    public byte[] parseByteArray(String jsonString) {
        List<Byte> list = json.fromJson(jsonString, Types.getListParameterizedType(Byte.class));
        return PrimitiveArrays.unwrap(Collects.toArray(list, Byte[].class), true);
    }

    @Override
    public short[] parseShortArray(String jsonString) {
        List<Short> list = json.fromJson(jsonString, Types.getListParameterizedType(Short.class));
        return PrimitiveArrays.unwrap(Collects.toArray(list, Short[].class), true);
    }


    @Override
    public int parseInt(String jsonString) {
        return parse(Integer.class, jsonString);
    }

    @Override
    public int parseInt(InputStream input) {
        return parse(Integer.class, input);
    }

    @Override
    public int parseInt(InputStream input, Charset charset) {
        return parse(Integer.class, input, charset);
    }

    @Override
    public int parseInt(byte[] jsonBytes) {
        return parse(Integer.class, jsonBytes);
    }

    @Override
    public int parseInt(byte[] jsonBytes, Charset charset) {
        return parse(Integer.class, jsonBytes, charset);
    }

    @Override
    public int parseInt(char[] chars) {
        return parse(Integer.class, chars);
    }

    @Override
    public int parseInt(CharSequence jsonSeq) {
        return parse(Integer.class, jsonSeq);
    }

    @Override
    public int parseIntFromFile(String fileName) {
        return parseClassFromFile(Integer.class, fileName);
    }

    @Override
    public long parseLong(String jsonString) {
        return parse(Long.class, jsonString);
    }

    @Override
    public long parseLong(InputStream input) {
        return parse(Long.class, input);
    }

    @Override
    public long parseLong(InputStream input, Charset charset) {
        return parse(Long.class, input, charset);
    }

    @Override
    public long parseLong(byte[] jsonBytes) {
        return parse(Long.class, jsonBytes);
    }

    @Override
    public long parseLong(byte[] jsonBytes, Charset charset) {
        return parse(Long.class, jsonBytes, charset);
    }

    @Override
    public long parseLong(char[] chars) {
        return parse(Long.class, chars);
    }

    @Override
    public long parseLong(CharSequence jsonSeq) {
        return parse(Long.class, jsonSeq);
    }

    @Override
    public long parseLongFromFile(String fileName) {
        return parseClassFromFile(Long.class, fileName);
    }

    @Override
    public String parseString(String value) {
        return value;
    }

    @Override
    public String parseString(InputStream value) {
        return parse(String.class, value);
    }

    @Override
    public String parseString(InputStream value, Charset charset) {
        return parse(String.class, value, charset);
    }

    @Override
    public String parseString(byte[] value) {
        return parse(String.class, value);
    }

    @Override
    public String parseString(byte[] value, Charset charset) {
        return parse(String.class, value, charset);
    }

    @Override
    public String parseString(char[] value) {
        return parse(String.class, value);
    }

    @Override
    public String parseString(CharSequence value) {
        return parse(String.class, value);
    }

    @Override
    public String parseStringFromFile(String value) {
        return parse(String.class, value);
    }


    @Override
    public double parseDouble(String value) {
        return parse(Double.class, value);
    }

    @Override
    public double parseDouble(InputStream value) {
        return parse(Double.class, value);
    }

    @Override
    public double parseDouble(byte[] value) {
        return parse(Double.class, value);
    }

    @Override
    public double parseDouble(char[] value) {
        return parse(Double.class, value);
    }

    @Override
    public double parseDouble(CharSequence value) {
        return parse(Double.class, value);
    }

    @Override
    public double parseDouble(byte[] value, Charset charset) {
        return parse(Double.class, value, charset);
    }

    @Override
    public double parseDouble(InputStream value, Charset charset) {
        return parse(Double.class, value, charset);
    }

    @Override
    public double parseDoubleFromFile(String fileName) {
        return parseClassFromFile(Double.class, fileName);
    }


    @Override
    public float parseFloat(String value) {
        return parse(Float.class, value);
    }

    @Override
    public float parseFloat(InputStream value) {
        return parse(Float.class, value);
    }

    @Override
    public float parseFloat(byte[] value) {
        return parse(Float.class, value);
    }

    @Override
    public float parseFloat(char[] value) {
        return parse(Float.class, value);
    }

    @Override
    public float parseFloat(CharSequence value) {
        return parse(Float.class, value);
    }

    @Override
    public float parseFloat(byte[] value, Charset charset) {
        return parse(Float.class, value, charset);
    }

    @Override
    public float parseFloat(InputStream value, Charset charset) {
        return parse(Float.class, value, charset);
    }

    @Override
    public float parseFloatFromFile(String fileName) {
        return parseClassFromFile(Float.class, fileName);
    }


    @Override
    public BigDecimal parseBigDecimal(String value) {
        return parse(BigDecimal.class, value);
    }

    @Override
    public BigDecimal parseBigDecimal(InputStream value) {
        return parse(BigDecimal.class, value);
    }

    @Override
    public BigDecimal parseBigDecimal(byte[] value) {
        return parse(BigDecimal.class, value);
    }

    @Override
    public BigDecimal parseBigDecimal(char[] value) {
        return parse(BigDecimal.class, value);
    }

    @Override
    public BigDecimal parseBigDecimal(CharSequence value) {
        return parse(BigDecimal.class, value);
    }

    @Override
    public BigDecimal parseBigDecimal(byte[] value, Charset charset) {
        return parse(BigDecimal.class, value, charset);
    }

    @Override
    public BigDecimal parseBigDecimal(InputStream value, Charset charset) {
        return parse(BigDecimal.class, value, charset);
    }

    @Override
    public BigDecimal parseBigDecimalFromFile(String fileName) {
        return parseClassFromFile(BigDecimal.class, fileName);
    }

    @Override
    public BigInteger parseBigInteger(String value) {
        return parse(BigInteger.class, value);
    }

    @Override
    public BigInteger parseBigInteger(InputStream value) {
        return parse(BigInteger.class, value);
    }

    @Override
    public BigInteger parseBigInteger(byte[] value) {
        return parse(BigInteger.class, value);
    }

    @Override
    public BigInteger parseBigInteger(char[] value) {
        return parse(BigInteger.class, value);
    }

    @Override
    public BigInteger parseBigInteger(CharSequence value) {
        return parse(BigInteger.class, value);
    }

    @Override
    public BigInteger parseBigInteger(byte[] value, Charset charset) {
        return parse(BigInteger.class, value, charset);
    }

    @Override
    public BigInteger parseBigInteger(InputStream value, Charset charset) {
        return parse(BigInteger.class, value, charset);
    }

    @Override
    public BigInteger parseBigIntegerFile(String fileName) {
        return parseClassFromFile(BigInteger.class, fileName);
    }


    @Override
    public Object parseDirect(byte[] value) {
        builder.addAsUTF(value);
        return parse(builder.readForRecycle());
    }


    @Override
    public <T> T parseDirect(Class<T> type, byte[] value) {
        builder.addAsUTF(value);
        return parse(type, builder.readForRecycle());
    }


    @Override
    public <T> T parseAsStream(Class<T> type, byte[] value) {
        return this.parse(type, new InMemoryInputStream(value));
    }


    @Override
    public <T> T parse(Class<T> type, CharSequence charSequence) {
        return parse(type, charSequence.toString());
    }

    @Override
    public <T> T parse(Class<T> type, char[] chars) {
        return convert(type, parse(chars));
    }

    @Override
    public Object parseAsStream(byte[] value) {
        return this.parse(new InMemoryInputStream(value));
    }

    @Override
    public Object parseFile(String fileName) {
        try {
            Path filePath = Paths.get(fileName);
            long size = Files.size(filePath);
            size = size > 2000000000 ? 1000000 : size;
            if (copyBuf == null) {
                copyBuf = new char[bufSize];
            }

            Reader reader = Files.newBufferedReader(Paths.get(fileName), charset);
            fileInputBuf = IO.read(reader, fileInputBuf, (int) size, copyBuf);
            return parse(fileInputBuf.readForRecycle());
        } catch (IOException ex) {
            return Exceptions.handle(Object.class, fileName, ex);
        }

    }

    @Override
    public void close() {

    }


    private CharBuf fileInputBuf;

    @Override
    public <T> T parse(Class<T> type, Reader reader) {

        if (copyBuf == null) {
            copyBuf = new char[bufSize];
        }

        fileInputBuf = IO.read(reader, fileInputBuf, bufSize, copyBuf);
        return parse(type, fileInputBuf.readForRecycle());

    }


    @Override
    public <T> T parse(Class<T> type, InputStream input) {
        if (copyBuf == null) {
            copyBuf = new char[bufSize];
        }

        fileInputBuf = IO.read(input, fileInputBuf, charset, bufSize, copyBuf);
        return parse(type, fileInputBuf.readForRecycle());
    }


    @Override
    public <T> T parse(Class<T> type, InputStream input, Charset charset) {
        fileInputBuf = IO.read(input, fileInputBuf, charset, bufSize, copyBuf);
        return parse(type, fileInputBuf.readForRecycle());
    }


    @Override
    public <T> T parseFile(Class<T> type, String fileName) {
        try {
            Path filePath = Paths.get(fileName);
            long size = Files.size(filePath);
            size = size > 2_000_000_000 ? 1_000_000 : size;
            Reader reader = Files.newBufferedReader(Paths.get(fileName), charset);
            fileInputBuf = IO.read(reader, fileInputBuf, (int) size, copyBuf);
            return parse(type, fileInputBuf.readForRecycle());
        } catch (IOException ex) {
            return Exceptions.handle(type, fileName, ex);
        }
    }


    @Override
    public Map<String, Object> parseMap(char[] value) {
        return (Map<String, Object>) parse(value);
    }

    @Override
    public Map<String, Object> parseMap(byte[] value) {
        return (Map<String, Object>) parse(value);
    }

    @Override
    public Map<String, Object> parseMap(byte[] value, Charset charset) {
        return (Map<String, Object>) parse(value, charset);
    }

    @Override
    public Map<String, Object> parseMap(InputStream value, Charset charset) {
        return (Map<String, Object>) parse(value, charset);
    }

    @Override
    public Map<String, Object> parseMap(CharSequence value) {
        return (Map<String, Object>) parse(value);
    }

    @Override
    public Map<String, Object> parseMap(InputStream value) {
        return (Map<String, Object>) parse(value);
    }

    @Override
    public Map<String, Object> parseMap(Reader value) {
        return (Map<String, Object>) parse(value);
    }

    @Override
    public Map<String, Object> parseMapFromFile(String file) {
        return (Map<String, Object>) parseFile(file);
    }


    @Override
    public Object parse(String jsonString) {
        return parser.parse(jsonString);
    }

    @Override
    public Object parse(byte[] bytes) {
        return parser.parse(bytes);
    }

    @Override
    public Object parse(byte[] bytes, Charset charset) {
        return parser.parse(bytes, charset);
    }

    @Override
    public Object parse(CharSequence charSequence) {
        return parser.parse(charSequence);
    }


    @Override
    public Object parse(char[] chars) {
        return parser.parse(chars);
    }

    @Override
    public Object parse(Reader reader) {
        return parser.parse(reader);
    }

    @Override
    public Object parse(InputStream input) {
        return parser.parse(input);
    }

    @Override
    public Object parse(InputStream input, Charset charset) {
        return parser.parse(input, charset);
    }


}
