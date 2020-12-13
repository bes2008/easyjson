package com.jn.easyjson.gson.typeadapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.jn.easyjson.gson.GsonJSONBuilder;
import com.jn.langx.util.reflect.Reflects;

import java.util.Calendar;
import java.util.Date;

public class CalendarTypeAdapterFactory extends EasyjsonAbstractTypeAdapterFactory{
    private DateTypeAdapterFactory dateTypeAdapterFactory;

    public CalendarTypeAdapterFactory(GsonJSONBuilder jsonBuilder) {
        super(jsonBuilder);
        this.dateTypeAdapterFactory = new DateTypeAdapterFactory(jsonBuilder);
    }

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (Reflects.isSubClassOrEquals(Calendar.class, type.getRawType())) {
            DateTypeAdapter dateTypeAdapter = (DateTypeAdapter) dateTypeAdapterFactory.create(gson, TypeToken.get(Date.class));
            CalendarTypeAdapter adapter = new CalendarTypeAdapter();
            adapter.setJSONBuilder(jsonBuilder);
            adapter.setDateTypeAdapter(dateTypeAdapter);
            return adapter;
        }
        return null;
    }
}
