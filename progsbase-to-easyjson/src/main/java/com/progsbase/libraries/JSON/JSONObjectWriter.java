package com.progsbase.libraries.JSON;


import JSON.structures.Element;

import java.util.List;
import java.util.Map;

import static JSON.StringElementMaps.StringElementMaps.SetStringElementMap;
import static JSON.json.json.*;
import static JSON.writer.writer.WriteJSON;

/*
 This class writes a Java Object into a string with the following specs:
  - Map<String, Object> -> {}
  - List<Object> -> []
  - Object [] -> []
  - Double | Float | Integer | Long | Short | Byte -> number
  - String -> ""
  - null -> null
  - Boolean -> boolean
 */
public class JSONObjectWriter {
    public static String writeJSON(Object object){
        char[] value;

        try {
            Element e = unjavaifyJSONValue(object);
            value = WriteJSON(e);
        } catch (JSONException ex) {
            value = "".toCharArray();
        }

        return new String(value);
    }

    public static String writeJSONExceptionOnFailure(Object o) throws JSONException {
        Element e = unjavaifyJSONValue(o);
        char[] value = WriteJSON(e);

        return new String(value);
    }

    public static StringReturn writeJSONWithCheck(Object o) {
        StringReturn stringReturn = new StringReturn();

        try {
            Element e = unjavaifyJSONValue(o);
            stringReturn.success = true;
            stringReturn.errorMessage = "";
            stringReturn.str = new String(WriteJSON(e));
        } catch (JSONException ex) {
            stringReturn.success = false;
            stringReturn.errorMessage = ex.getMessage();
            stringReturn.str = "";
        }

        return stringReturn;
    }

    public static Element unjavaifyJSONValue(Object o) throws JSONException {
        Element e;

        if(o == null) {
            e = CreateNullElement();
        }else if(o instanceof Map) {
            e = unjavaifyJSONObject(o);
        }else if(o.getClass().isArray()) {
            e = unjavaifyJSONArrayArray(o);
        }else if(o instanceof List) {
            e = unjavaifyJSONArrayList(o);
        }else if(o instanceof String) {
            String s = (String)o;
            e = CreateStringElement(s.toCharArray());
        }else if(o instanceof Double) {
            Double n = (Double)o;
            e = CreateNumberElement(n);
        }else if(o instanceof Float) {
            Float n = (Float)o;
            e = CreateNumberElement(n);
        }else if(o instanceof Integer) {
            Integer n = (Integer)o;
            e = CreateNumberElement(n);
        }else if(o instanceof Long) {
            Long n = (Long)o;
            e = CreateNumberElement(n);
        }else if(o instanceof Short) {
            Short n = (Short)o;
            e = CreateNumberElement(n);
        }else if(o instanceof Byte) {
            Byte n = (Byte)o;
            e = CreateNumberElement(n);
        }else if(o instanceof Boolean) {
            Boolean b = (Boolean)o;
            e = CreateBooleanElement(b);
        }else{
            throw new JSONException("Cannot be converted to JSON structure: " + o.getClass());
        }

        return e;
    }

    public static Element unjavaifyJSONObject(Object o) throws JSONException {
        Map<String, Object> m = (Map)o;
        Element e = CreateObjectElement(m.size());
        int i = 0;

        for(Map.Entry<String, Object> p : m.entrySet()){
            Element s = unjavaifyJSONValue(p.getValue());
            SetStringElementMap(e.object, i, p.getKey().toCharArray(), s);
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