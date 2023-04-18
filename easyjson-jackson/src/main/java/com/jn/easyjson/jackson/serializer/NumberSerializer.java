/*
 * Copyright 2019 the original author or authors.
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

package com.jn.easyjson.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.jn.easyjson.jackson.Jacksons;

import java.io.IOException;

import static com.jn.easyjson.jackson.JacksonConstants.SERIALIZE_LONG_USING_STRING_ATTR_KEY;
import static com.jn.easyjson.jackson.JacksonConstants.SERIALIZE_NUMBER_USING_STRING_ATTR_KEY;

public class NumberSerializer extends JsonSerializer<Number> {

    @Override
    public void serialize(Number value, JsonGenerator gen, SerializerProvider sp) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        boolean longUsingString = Jacksons.getBoolean(sp.getAttribute(SERIALIZE_LONG_USING_STRING_ATTR_KEY));
        boolean usingString = Jacksons.getBoolean(sp.getAttribute(SERIALIZE_NUMBER_USING_STRING_ATTR_KEY));
        if (longUsingString) {
            Class typeOfSrc = value.getClass();
            if (Long.class == typeOfSrc || long.class == typeOfSrc) {
                gen.writeString(value.toString());
                return;
            } else {
                gen.writeNumber(value.toString());
                return;
            }
        }
        if (usingString) {
            gen.writeString(value.toString());
            return;
        }
        gen.writeNumber(value.toString());
    }

}
