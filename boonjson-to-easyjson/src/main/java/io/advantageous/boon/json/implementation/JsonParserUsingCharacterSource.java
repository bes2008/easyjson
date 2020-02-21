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

import io.advantageous.boon.core.LazyMap;
import io.advantageous.boon.core.reflection.BeanUtils;
import io.advantageous.boon.json.JsonException;
import io.advantageous.boon.primitive.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Converts an input JSON String into Java objects works with String or char array
 * as input. Produces an Object which can be any of the basic JSON types mapped
 * to Java.
 */
public class JsonParserUsingCharacterSource extends BaseJsonParser {

    private CharacterSource characterSource;

    public JsonParserUsingCharacterSource() {
    }


    protected String exceptionDetails(String message) {
        return characterSource.errorDetails(message);
    }


    protected final Object decodeJsonObject() {
        LazyMap map = new LazyMap();

        try {

            CharacterSource characterSource = this.characterSource;

            if (characterSource.currentChar() == '{') {
                characterSource.nextChar();
            }


            while (characterSource.hasChar()) {

                characterSource.skipWhiteSpace();


                if (characterSource.currentChar() == DOUBLE_QUOTE) {

                    String key = decodeString();
                    //puts ("key", key);

                    if (internKeys) {
                        String keyPrime = internedKeysCache.get(key);
                        if (keyPrime == null) {
                            key = key.intern();
                            internedKeysCache.put(key, key);
                        } else {
                            key = keyPrime;
                        }
                    }

                    characterSource.skipWhiteSpace();
                    if (characterSource.currentChar() != COLON) {

                        complain("expecting current character to be : but was " + charDescription(characterSource.currentChar()) + "\n");
                    }

                    characterSource.nextChar();

                    characterSource.skipWhiteSpace();

                    Object value = decodeValue();


                    characterSource.skipWhiteSpace();

                    map.put(key, value);


                }

                int ch = characterSource.currentChar();
                if (ch == '}') {
                    characterSource.nextChar();
                    break;
                } else if (ch == ',') {
                    characterSource.nextChar();
                    continue;
                } else {
                    complain(
                            "expecting '}' or ',' but got current char " + charDescription(ch));

                }
            }
        } catch (Exception ex) {
            if (ex instanceof JsonException) {
                throw ex;
            }
            throw new JsonException(exceptionDetails("Unable to parse JSON object"), ex);
        }


        return map;
    }


    protected final void complain(String complaint) {
        throw new JsonException(exceptionDetails(complaint));
    }


    private final Object decodeValue() {
        CharacterSource characterSource = this.characterSource;
        Object value = null;
        characterSource.skipWhiteSpace();

        switch (characterSource.currentChar()) {

            case '"':
                value = decodeString();
                break;


            case 't':
                value = decodeTrue();
                break;

            case 'f':
                value = decodeFalse();
                break;

            case 'n':
                value = decodeNull();
                break;

            case '[':
                value = decodeJsonArray();
                break;

            case '{':
                value = decodeJsonObject();
                break;

            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                value = decodeNumber(false);
                break;

            case '-':
                value = decodeNumber(true);
                break;

            default:
                throw new JsonException(exceptionDetails("Unable to determine the " +
                        "current character, it is not a string, number, array, or object"));

        }

        return value;
    }


    private final Object decodeNumber(boolean negative) {

        char[] chars = characterSource.readNumber();
        Object value = null;

        if (CharScanner.hasDecimalChar(chars, negative)) {
            value = CharScanner.parseDouble(chars);
        } else if (CharScanner.isInteger(chars)) {
            value = CharScanner.parseInt(chars);
        } else if (CharScanner.isLong(chars)) {
            value = CharScanner.parseLong(chars);
        }

        return value;
    }


    protected static final char[] NULL = Chr.chars("null");

    protected final Object decodeNull() {
        if (!characterSource.consumeIfMatch(NULL)) {
            throw new JsonException(exceptionDetails("null not parse properly"));
        }
        return null;
    }


    protected static final char[] TRUE = Chr.chars("true");

    protected final boolean decodeTrue() {

        if (characterSource.consumeIfMatch(TRUE)) {
            return true;
        } else {
            throw new JsonException(exceptionDetails("true not parsed properly"));
        }
    }


    protected static char[] FALSE = Chr.chars("false");

    protected final boolean decodeFalse() {

        if (characterSource.consumeIfMatch(FALSE)) {
            return false;
        } else {
            throw new JsonException(exceptionDetails("false not parsed properly"));
        }

    }


    private CharBuf builder = CharBuf.create(20);

    private String decodeString() {

        CharacterSource characterSource = this.characterSource;


        characterSource.nextChar();


        char[] chars = characterSource.findNextChar('"', '\\');


        String value = null;
        if (characterSource.hadEscape()) {
            value = builder.decodeJsonString(chars).toString();
            builder.recycle();
        } else {
            value = new String(chars);
        }

        return value;
    }


    protected final List decodeJsonArray() {

        ArrayList<Object> list = null;

        boolean foundEnd = false;
        try {


            CharacterSource characterSource = this.characterSource;

            if (this.characterSource.currentChar() == '[') {
                characterSource.nextChar();
            }


            characterSource.skipWhiteSpace();



            /* the list might be empty  */
            if (this.characterSource.currentChar() == ']') {
                characterSource.nextChar();
                return Collections.EMPTY_LIST;
            }

            list = new ArrayList();


            do {

                characterSource.skipWhiteSpace();

                Object arrayItem = decodeValue();

                list.add(arrayItem);


                characterSource.skipWhiteSpace();

                int c = characterSource.currentChar();

                if (c == COMMA) {
                    characterSource.nextChar();
                    continue;
                } else if (c == CLOSED_BRACKET) {
                    foundEnd = true;
                    characterSource.nextChar();
                    break;
                } else {

                    String charString = charDescription(c);

                    complain(
                            String.format("expecting a ',' or a ']', " +
                                    " but got \nthe current character of  %s " +
                                    " on array index of %s \n", charString, list.size())
                    );

                }
            } while (characterSource.hasChar());


        } catch (Exception ex) {
            if (ex instanceof JsonException) {
                throw ex;
            }

            throw new JsonException(exceptionDetails("Unexpected issue"), ex);
        }

        if (!foundEnd) {
            throw new JsonException(exceptionDetails("Could not find end of JSON array"));

        }
        return list;

    }


    @Override
    public Object parse(char[] chars) {
        characterSource = new CharArrayCharacterSource(chars);
        return decodeValue();
    }


    @Override
    public Object parse(Reader reader) {

        if (reader instanceof StringReader) {
            try {
                String str = BeanUtils.idxStr(reader, "str");
                int length = BeanUtils.idxInt(reader, "length");
                int next = BeanUtils.idxInt(reader, "next");

                if (str != null && next == 0 && length == str.length()) {
                    return parse(str);
                }
            } catch (Exception ex) {
                //Boon.logger("JSON PARSER").fatal(ex);
                //throw new IllegalStateException(ex);
            }
        }


        characterSource = new ReaderCharacterSource(reader);
        return this.decodeValue();

    }


    @Override
    public Object parse(byte[] value, Charset charset) {
        if (value.length < 20_000) {
            characterSource = new CharArrayCharacterSource(new String(value, charset));
            return this.decodeValue();

        } else {
            return parse(new InMemoryInputStream(value), charset);
        }
    }


    IOInputStream ioInputStream;

    @Override
    public Object parse(InputStream inputStream, Charset charset) {

        if (inputStream instanceof ByteArrayInputStream) {

            return parse(new InputStreamReader(inputStream, charset));

        } else {
            ioInputStream = IOInputStream.input(ioInputStream, 50_000).input(inputStream);
            return parse(new InputStreamReader(ioInputStream, charset));

        }


    }


}
