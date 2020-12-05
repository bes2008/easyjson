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
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jn.easyjson.jackson.Jacksons;

import java.io.IOException;

import static com.jn.easyjson.jackson.JacksonConstants.SERIALIZE_BOOLEAN_USING_1_0_ATTR_KEY;
import static com.jn.easyjson.jackson.JacksonConstants.SERIALIZE_BOOLEAN_USING_ON_OFF_ATTR_KEY;

public class BooleanSerializer extends JsonSerializer<Boolean> {

    @Override
    public void serialize(Boolean value, JsonGenerator gen, SerializerProvider sp) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        boolean using1_0 = Jacksons.getBoolean(sp.getAttribute(SERIALIZE_BOOLEAN_USING_1_0_ATTR_KEY));
        boolean usingOnOff = Jacksons.getBoolean(sp.getAttribute(SERIALIZE_BOOLEAN_USING_ON_OFF_ATTR_KEY));

        if (usingOnOff) {
            gen.writeString(value ? "on" : "off");
            return;
        }
        if (using1_0) {
            gen.writeNumber(value ? 1 : 0);
            return;
        }
        gen.writeBoolean(value);
    }
}
