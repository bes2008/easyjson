package com.jn.easyjson.jackson;

import com.jn.langx.util.collection.Collects;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class DateSeries {

    private static List<Class> supportedDateTypes = Collects.<Class>newArrayList(
            Date.class,
            java.sql.Date.class,
            Timestamp.class);

    public static boolean isSupported(Class type) {
        return Date.class.isAssignableFrom(type) || supportedDateTypes.contains(type);
    }

}
