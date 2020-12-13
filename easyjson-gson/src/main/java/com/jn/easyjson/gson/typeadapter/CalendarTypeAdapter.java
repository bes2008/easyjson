package com.jn.easyjson.gson.typeadapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.jn.langx.exception.RuntimeIOException;
import com.jn.langx.util.reflect.Modifiers;
import com.jn.langx.util.reflect.Reflects;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CalendarTypeAdapter extends EasyjsonAbstractTypeAdapter<Calendar> {

    private DateTypeAdapter dateTypeAdapter;

    public void setDateTypeAdapter(DateTypeAdapter dateTypeAdapter) {
        this.dateTypeAdapter = dateTypeAdapter;
    }

    @Override
    public void write(JsonWriter out, Calendar value) throws IOException {
        dateTypeAdapter.write(out, value.getTime());
    }

    @Override
    public Calendar read(JsonReader in) throws IOException {
        Date date = dateTypeAdapter.read(in);

        Constructor defaultCtor = (Constructor<Calendar>) Reflects.getConstructor(getDataClass());
        TimeZone tz;
        DateFormat dateFormat = jsonBuilder.serializeUseDateFormat();
        if(dateFormat!=null){
            tz = dateFormat.getTimeZone();
        }else {
            tz = jsonBuilder.serializeUsingTimeZone();
        }
        Calendar c = null;
        if (defaultCtor == null || !Modifiers.isPublic(defaultCtor)) {
            c = Calendar.getInstance(tz);
        }else {
            try {
                c = (Calendar) defaultCtor.newInstance();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        c.setTimeZone(tz);
        c.setTimeInMillis(date.getTime());
        return c;

    }
}
