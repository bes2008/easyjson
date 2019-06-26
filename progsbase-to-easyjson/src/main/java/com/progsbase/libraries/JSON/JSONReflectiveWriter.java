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

package com.progsbase.libraries.JSON;

import JSON.structures.Element;
import com.github.fangjinuo.easyjson.core.JSONBuilderProvider;

import java.lang.reflect.Field;
import java.util.List;

import static JSON.StringElementMaps.StringElementMaps.SetStringElementMap;
import static JSON.json.json.*;

public class JSONReflectiveWriter {
    public static <T> boolean writeJSON(T t, StringReference jsonReference, StringReference errorMessage) {
        boolean success;

        try {
            jsonReference.string = writeJSON(t);
            success = true;
        } catch (JSONException e) {
            success = false;
            errorMessage.string = e.getMessage();
        }

        return success;
    }

    public static <T> String writeJSON(T t) throws JSONException{
        return JSONBuilderProvider.simplest().toJson(t);
    }

    private static <T> Element unjavaifyJSONValue(T t) throws JSONException {
        Element e;

        if(t == null) {
            e = CreateNullElement();
        }else if(t.getClass().isArray()) {
            e = unjavaifyJSONArrayArray(t);
        }else if(t.getClass().isEnum()) {
            e = unjavaifyJSONEnum(t);
        }else if(t instanceof List) {
            e = unjavaifyJSONArrayList(t);
        }else if(t instanceof String) {
            String s = (String)t;
            e = CreateStringElement(s.toCharArray());
        }else if(t instanceof Double) {
            Double n = (Double)t;
            e = CreateNumberElement(n);
        }else if(t instanceof Float) {
            Float n = (Float)t;
            e = CreateNumberElement(n);
        }else if(t instanceof Integer) {
            Integer n = (Integer)t;
            e = CreateNumberElement(n);
        }else if(t instanceof Long) {
            Long n = (Long)t;
            e = CreateNumberElement(n);
        }else if(t instanceof Short) {
            Short n = (Short)t;
            e = CreateNumberElement(n);
        }else if(t instanceof Byte) {
            Byte n = (Byte)t;
            e = CreateNumberElement(n);
        }else if(t instanceof Boolean) {
            Boolean b = (Boolean)t;
            e = CreateBooleanElement(b);
        }else{
            e = unjavaifyJSONObject(t);
        }

        return e;
    }

    private static <T> Element unjavaifyJSONEnum(T t) {
        return CreateStringElement(t.toString().toCharArray());
    }

    public static <T> Element unjavaifyJSONObject(T t) throws JSONException {
        Field[] fields = t.getClass().getFields();
        Element e = CreateObjectElement(fields.length);
        int i = 0;

        for(Field f : fields){
            Element s;
            try {
                s = unjavaifyJSONValue(f.get(t));
            } catch (IllegalAccessException ex) {
                throw new JSONException(ex.getMessage());
            }
            SetStringElementMap(e.object, i, f.getName().toCharArray(), s);
            i++;
        }

        return e;
    }

    public static Element unjavaifyJSONArrayList(Object o) throws JSONException {
        List<Object> l = (List<Object>)o;
        Element e = CreateArrayElement(l.size());
        int i = 0;

        for(Object p : l){
            Element s = unjavaifyJSONValue(p);
            e.array[i] = s;
            i++;
        }

        return e;
    }

    public static Element unjavaifyJSONArrayArray(Object o) throws JSONException {
        Object a[] = (Object[])o;
        Element e = CreateArrayElement(a.length);
        int i = 0;

        for(Object p : a){
            Element s = unjavaifyJSONValue(p);
            e.array[i] = s;
            i++;
        }

        return e;
    }
}
