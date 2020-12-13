package com.jn.easyjson.jackson;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.Reflects;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateSeries {

    private static List<Class> supportedDateTypes = Collects.<Class>newArrayList(
            Date.class,
            java.sql.Date.class,
            Timestamp.class,
            Calendar.class,
            GregorianCalendar.class
    );

    public static boolean isSupported(Class type) {
        return Reflects.isSubClassOrEquals(Date.class, type)
                || Reflects.isSubClassOrEquals(Calendar.class, type)
                || supportedDateTypes.contains(type);
    }

}
