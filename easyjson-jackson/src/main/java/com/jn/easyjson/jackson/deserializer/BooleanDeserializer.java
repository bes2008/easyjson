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

package com.jn.easyjson.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jn.easyjson.jackson.Jacksons;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.jn.easyjson.jackson.JacksonConstants.SERIALIZE_BOOLEAN_USING_1_0_ATTR_KEY;

public class BooleanDeserializer extends JsonDeserializer<Boolean> implements ContextualDeserializer {

    private static final List<String> evalTrues = Arrays.asList(new String[]{"true", "on", "1"});

    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
        boolean using1_0 = Jacksons.getBooleanAttr(ctx, SERIALIZE_BOOLEAN_USING_1_0_ATTR_KEY);

        JsonToken curr = p.getCurrentToken();
        if (using1_0 && curr == JsonToken.VALUE_NUMBER_INT) {
            return p.getIntValue() == 1;
        }
        if (curr == JsonToken.VALUE_STRING) {
            return evalTrues.contains(p.getValueAsString().toLowerCase());
        }
        if (curr == JsonToken.VALUE_TRUE || curr == JsonToken.VALUE_FALSE) {
            return curr == JsonToken.VALUE_TRUE;
        }
        return false;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty beanProperty, Class<?> type) throws JsonMappingException {
        return null;
    }
}
