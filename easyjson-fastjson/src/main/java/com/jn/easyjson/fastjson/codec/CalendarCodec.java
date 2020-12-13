package com.jn.easyjson.fastjson.codec;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.Modifiers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.Types;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.*;

public class CalendarCodec  implements ObjectSerializer, ObjectDeserializer, Typed  {
    private DateCodec dateCodec;
    private DateFormat dateFormat;
    private TimeZone timeZone = TimeZone.getDefault();

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateCodec getDateCodec() {
        return dateCodec;
    }

    public void setDateCodec(DateCodec dateCodec) {
        this.dateCodec = dateCodec;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Date date = dateCodec.<Date>deserialze(parser, Date.class, fieldName);
        if(date==null) {
            return null;
        }
        TimeZone tz = this.timeZone;
        if(dateFormat!=null){
            tz = dateFormat.getTimeZone();
        }
        Calendar c = null;
        Constructor defaultCtor = (Constructor<Calendar>) Reflects.getConstructor(Types.toClass(type));
        if (defaultCtor == null || !Modifiers.isPublic(defaultCtor)) {
            c = Calendar.getInstance(tz);
        }else {
            try {
                c = (Calendar) defaultCtor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(c!=null) {
            c.setTimeZone(tz);
            c.setTimeInMillis(date.getTime());
        }
        return (T)c;

    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {

    }

    @Override
    public List<Type> applyTo() {
        return Collects.<Type>newArrayList(Calendar.class, GregorianCalendar.class);
    }
}
