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

package com.jn.easyjson.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.databind.*;
import com.jn.easyjson.core.codec.dialect.PropertyCodecConfiguration;
import com.jn.easyjson.jackson.JacksonConstants;
import com.jn.easyjson.jackson.Jacksons;
import com.jn.easyjson.jackson.ext.EasyJsonObjectMapper;
import com.jn.langx.util.Emptys;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer<T extends Date> extends JsonSerializer<T> implements ContextualSerializer {
    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider ctx) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        DateFormat df = null;
        String pattern= null;

        if(Jacksons.getBooleanAttr(ctx, JacksonConstants.ENABLE_CUSTOM_CONFIGURATION)){
            PropertyCodecConfiguration propertyCodecConfiguration = Jacksons.getPropertyCodecConfiguration(gen);
            if(propertyCodecConfiguration!=null){
                df = propertyCodecConfiguration.getDateFormat();
                pattern = propertyCodecConfiguration.getDatePattern();
            }
        }
        if(df==null) {
            df = Jacksons.getDateFormatAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_DATE_FORMAT_ATTR_KEY);
        }
        if(Emptys.isEmpty(pattern)) {
            pattern = Jacksons.getStringAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_PATTERN_ATTR_KEY);
        }
        boolean usingToString = Jacksons.getBooleanAttr(ctx, JacksonConstants.SERIALIZE_DATE_USING_TO_STRING_ATTR_KEY);
        if (df == null && pattern != null && !pattern.isEmpty()) {
            df = new SimpleDateFormat(pattern);
        }

        if (df != null) {
            gen.writeString(df.format(value));
            return;
        }
        if (usingToString) {
            gen.writeString(value.toString());
        }
        gen.writeNumber(value.getTime());
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property, JavaType type) throws JsonMappingException {
        if (type.getRawClass() == Date.class) {
            return this;
        }
        return null;
    }
}
