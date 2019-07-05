package com.progsbase.libraries.JSON;


import JSON.StringElementMaps.StringElementMap;
import JSON.structures.Element;
import com.github.fangjinuo.easyjson.core.JSONBuilderProvider;
import com.github.fangjinuo.easyjson.core.node.JsonTreeNodes;
import references.references.StringArrayReference;
import references.references.StringReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static JSON.StringElementMaps.StringElementMaps.GetStringElementMapNumberOfKeys;

/*
 This class reads JSON into a Java Object with the following actual classes:
  - {} -> Map<String, Object>
  - [] -> List<Object>
  - number -> Double
  - "" -> String
  - null -> null
  - boolean -> Boolean
 */
public class JSONObjectReader {
    public static Object readJSON(String json) {
        return JsonTreeNodes.toJavaObject(JSONBuilderProvider.create().build().fromJson(json));
    }

    public static Object readJSONExceptionOnFailure(String json) throws JSONException {
        return readJSON(json);
    }

    public static String joinErrorMessages(StringArrayReference errorMessages) {
        StringBuilder errorMessage = new StringBuilder();

        for (int i = 0; i < errorMessages.stringArray.length; i++) {
            errorMessage.append(i + 1);
            errorMessage.append(". ");
            errorMessage.append(new String(errorMessages.stringArray[i].string));
            errorMessage.append(" ");
        }

        return errorMessage.toString();
    }

    private static StringArrayReference throwableToStringArrayReference(Throwable ex) {
        List<StringReference> stringReferences = new ArrayList<StringReference>();

        StringReference stringReference = new StringReference();
        stringReference.string = ex.getMessage().toCharArray();
        stringReferences.add(stringReference);

        Throwable current = ex;
        Throwable cause = ex.getCause();
        while (cause != null && cause != current) {
            stringReference = new StringReference();
            stringReference.string = cause.getMessage().toCharArray();
            stringReferences.add(stringReference);
            current = cause;
            cause = current.getCause();
        }

        StringArrayReference arrayReference = new StringArrayReference();
        arrayReference.stringArray = stringReferences.toArray(new StringReference[stringReferences.size()]);
        return arrayReference;
    }

    public static JSONReturn readJSONWithCheck(String json) {
        JSONReturn jsonReturn = new JSONReturn();
        boolean success = true;
        Object object = null;
        try {
            object = readJSON(json);
        } catch (Throwable ex) {
            success = false;
            jsonReturn.errorMessage = joinErrorMessages(throwableToStringArrayReference(ex));
        }

        jsonReturn.object = object;
        jsonReturn.success = success;

        return jsonReturn;
    }

    public static Object javaifyJSONValue(Element element) {
        Object o;

        o = null;

        String type = new String(element.type.name);

        if (type.equals("object")) {
            o = javaifyJSONObject(element.object);
        } else if (type.equals("array")) {
            o = javaifyJSONArray(element.array);
        } else if (type.equals("string")) {
            o = new String(element.string);
        } else if (type.equals("number")) {
            o = element.number;
        } else if (type.equals("booleanValue")) {
            o = element.booleanValue;
        } else if (type.equals("nullValue")) {
            o = null;
        }

        return o;
    }

    public static Object javaifyJSONObject(StringElementMap object) {
        Map<String, Object> resultObject = new HashMap<String, Object>();

        for (int i = 0; i < GetStringElementMapNumberOfKeys(object); i++) {
            resultObject.put(new String(object.stringListRef.stringArray[i].string), javaifyJSONValue(object.elementListRef.array[i]));
        }

        return resultObject;
    }

    public static Object javaifyJSONArray(Element[] array) {
        List<Object> resultArray = new ArrayList<Object>();

        for (int i = 0; i < array.length; i++) {
            resultArray.add(javaifyJSONValue(array[i]));
        }

        return resultArray;
    }
}