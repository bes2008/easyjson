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

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Enumeration;


public class EnumerationSerializer implements ObjectSerializer {
    public static EnumerationSerializer instance = new EnumerationSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullListAsEmpty);
            return;
        }

        Type elementType = null;
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            if (fieldType instanceof ParameterizedType) {
                ParameterizedType param = (ParameterizedType) fieldType;
                elementType = param.getActualTypeArguments()[0];
            }
        }

        Enumeration<?> e = (Enumeration<?>) object;

        SerialContext context = serializer.context;
        serializer.setContext(context, object, fieldName, 0);

        try {
            int i = 0;
            out.append('[');
            while (e.hasMoreElements()) {
                Object item = e.nextElement();
                if (i++ != 0) {
                    out.append(',');
                }

                if (item == null) {
                    out.writeNull();
                    continue;
                }

                ObjectSerializer itemSerializer = serializer.getObjectWriter(item.getClass());
                itemSerializer.write(serializer, item, i - 1, elementType, 0);
            }
            out.append(']');
        } finally {
            serializer.context = context;
        }
    }
}
