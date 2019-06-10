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

package com.github.fangjinuo.easyjson.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.lang.reflect.Type;
import java.text.DateFormat;

public class Jacksons {
    public static boolean isJacksonJavaType(Type type) {
        try {
            JavaType jType = (JavaType) type;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static JavaType toJavaType(Type type) {
        return (JavaType) type;
    }

    public static boolean getBooleanAttr(DeserializationContext ctx, String key) {
        if (ctx == null || key == null) {
            return false;
        }
        return getBoolean(ctx.getAttribute(key));
    }

    public static boolean getBooleanAttr(SerializerProvider sp, String key) {
        if (sp == null || key == null) {
            return false;
        }
        return getBoolean(sp.getAttribute(key));
    }

    public static boolean getBoolean(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj.toString().toLowerCase().equals("true");
    }

    public static DateFormat getDateFormatAttr(DeserializationContext ctx, String key) {
        if (ctx == null || key == null) {
            return null;
        }
        return asDateFormat(ctx.getAttribute(key));
    }

    public static DateFormat getDateFormatAttr(SerializerProvider sp, String key) {
        if (sp == null || key == null) {
            return null;
        }
        return asDateFormat(sp.getAttribute(key));
    }

    private static DateFormat asDateFormat(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof DateFormat) {
            return (DateFormat) object;
        }
        return null;
    }


    public static String getStringAttr(DeserializationContext ctx, String key) {
        if (ctx == null || key == null) {
            return null;
        }
        return asString(ctx.getAttribute(key));
    }

    public static String getStringAttr(SerializerProvider sp, String key) {
        if (sp == null || key == null) {
            return null;
        }
        return asString(sp.getAttribute(key));
    }

    private static String asString(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return (String) object;
        }
        return null;
    }
}
