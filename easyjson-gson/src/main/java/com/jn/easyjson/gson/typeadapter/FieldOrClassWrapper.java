package com.jn.easyjson.gson.typeadapter;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import java.lang.reflect.Field;

/**
 * 用于包装运行时的类、或者字段。
 *
 * 如果是 字段，则 field, dataClass 都不为空
 * 如果是 class，则 field为null
 */
class FieldOrClassWrapper implements FieldAware{
    @Nullable
    private Field field;
    @NonNull
    private Class dataClass;

    public FieldOrClassWrapper(Class dataClass){
        setClass(dataClass);
    }

    public FieldOrClassWrapper(Field field){
        setField(field);
        setClass(field.getType());
    }

    public Class getDeclaringClass(){
        if (isField()){
            return field.getDeclaringClass();
        }
        return null;
    }

    @Override
    public void setField(Field field) {
        this.field = field;
    }

    @Override
    public void setClass(Class cls) {
        this.dataClass =cls;
    }

    @Override
    public Class getDataClass() {
        return this.dataClass;
    }

    public boolean isField(){
        return field!=null;
    }

    public String getFieldName(){
        if(isField()){
            return field.getName();
        }
        return null;
    }
}
