package com.jn.easyjson.gson.typeadapter;


import com.google.gson.TypeAdapter;
import com.jn.easyjson.core.JSONBuilderAware;
import com.jn.easyjson.gson.GsonJSONBuilder;

import java.lang.reflect.Field;

public abstract class EasyjsonAbstractTypeAdapter<T> extends TypeAdapter<T> implements JSONBuilderAware<GsonJSONBuilder>, FieldAware {
    // 使用 TypeAdapter API 时需要
    protected FieldOrClassWrapper currentFieldOrClass;
    protected GsonJSONBuilder jsonBuilder;

    @Override
    public GsonJSONBuilder getJSONBuilder() {
        return this.jsonBuilder;
    }

    @Override
    public void setJSONBuilder(GsonJSONBuilder jsonBuilder) {
        this.jsonBuilder = jsonBuilder;
    }

    public void setField(Field currentField) {
        this.currentFieldOrClass = new FieldOrClassWrapper(currentField);
    }

    @Override
    public void setClass(Class cls) {
        this.currentFieldOrClass = new FieldOrClassWrapper(cls);
    }
    @Override
    public Class getDataClass() {
        if(this.currentFieldOrClass!=null) {
            return this.currentFieldOrClass.getDataClass();
        }
        return null;
    }

    public Class getDeclaringClass(){
        if(this.currentFieldOrClass==null){
            return null;
        }
        return this.currentFieldOrClass.getDeclaringClass();
    }

    public boolean isField(){
        return this.currentFieldOrClass!=null && this.currentFieldOrClass.isField();
    }

}
