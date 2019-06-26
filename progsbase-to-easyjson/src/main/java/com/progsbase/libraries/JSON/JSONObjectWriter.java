package com.progsbase.libraries.JSON;


import JSON.structures.Element;
import com.github.fangjinuo.easyjson.core.JSONBuilderProvider;
import com.github.fangjinuo.easyjson.core.JsonTreeNode;
import com.github.fangjinuo.easyjson.core.node.*;
import references.references.NumberReference;
import references.references.StringArrayReference;
import references.references.StringReference;

import java.util.List;
import java.util.Map;

import static JSON.StringElementMaps.StringElementMaps.GetObjectValue;
import static JSON.StringElementMaps.StringElementMaps.GetStringElementMapKeySet;
import static JSON.StringElementMaps.StringElementMaps.SetStringElementMap;
import static JSON.elementTypeEnum.elementTypeEnum.ElementTypeEnumEquals;
import static JSON.json.json.*;
import static JSON.json.json.CreateArrayElement;
import static JSON.json.json.CreateObjectElement;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static nnumbers.NumberToString.NumberToString.*;
import static references.references.references.CreateNumberReference;

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
    public static String writeJSON(Object object) {
        char[] value;

            JsonTreeNode e = toJsonTreeNode(object);
            return JSONBuilderProvider.create().build().toJson(e);
    }

    public static JsonTreeNode toJsonTreeNode(Object object) {
        return JsonTreeNodes.fromJavaObject(object, new MapingToJsonTreeNode() {
            @Override
            public JsonTreeNode mapping(Object object) {
                if (object instanceof Element) {
                    Element element = (Element) object;
                    char[] elementType = element.type.name;
                    if (ElementTypeEnumEquals(elementType, "object".toCharArray())) {
                        char[] key;
                        int i;
                        StringArrayReference keys;
                        Element objectElement;
                        JsonObjectNode jsonObjectNode = new JsonObjectNode();

                        keys = GetStringElementMapKeySet(element.object);
                        for (i = 0; i < keys.stringArray.length; i = i + 1) {
                            key = keys.stringArray[(int) i].string;
                            key = JSONEscapeString(key);
                            objectElement = GetObjectValue(element.object, key);
                            JsonTreeNode valueNode = toJsonTreeNode(objectElement);
                            jsonObjectNode.addProperty(new String(key), valueNode);
                        }
                        return jsonObjectNode;
                    } else if (ElementTypeEnumEquals(elementType, "string".toCharArray())) {
                        String str = new String(JSONEscapeString(element.string));
                        return toJsonTreeNode(str);
                    } else if (ElementTypeEnumEquals(elementType, "array".toCharArray())) {
                        JsonArrayNode jsonArrayNode = new JsonArrayNode();
                        Element arrayElement;
                        int i;
                        for (i = 0; i < element.array.length; i = i + 1) {
                            arrayElement = element.array[(int) i];
                            jsonArrayNode.add(toJsonTreeNode(arrayElement));
                        }
                        return jsonArrayNode;
                    } else if (ElementTypeEnumEquals(elementType, "number".toCharArray())) {
                        char[] numberString;

                        if (abs(element.number) >= pow(10d, 15d) || abs(element.number) <= pow(10d, -15d)) {
                            numberString = nCreateStringScientificNotationDecimalFromNumber(element.number);
                        } else {
                            numberString = nCreateStringDecimalFromNumber(element.number);
                        }
                        return new JsonPrimitiveNode(Double.parseDouble(new String(numberString)));
                    } else if (ElementTypeEnumEquals(elementType, "nullValue".toCharArray())) {
                        return JsonNullNode.INSTANCE;
                    } else if (ElementTypeEnumEquals(elementType, "booleanValue".toCharArray())) {
                        return new JsonPrimitiveNode(element.booleanValue);
                    }
                }
                return null;
            }
        });
    }

    public static double JSONEscapedStringLength(char[] string) {
        NumberReference lettersReference;
        double i, length;

        lettersReference = CreateNumberReference(0d);
        length = 0d;

        for (i = 0d; i < string.length; i = i + 1d) {
            if (JSONMustBeEscaped(string[(int) i], lettersReference)) {
                length = length + lettersReference.numberValue;
            } else {
                length = length + 1d;
            }
        }
        return length;
    }

    public static char[] JSONEscapeString(char[] string) {
        double i, length;
        NumberReference index, lettersReference;
        char ns[], escaped[];

        length = JSONEscapedStringLength(string);

        ns = new char[(int) length];
        index = CreateNumberReference(0d);
        lettersReference = CreateNumberReference(0d);

        for (i = 0d; i < string.length; i = i + 1d) {
            if (JSONMustBeEscaped(string[(int) i], lettersReference)) {
                escaped = JSONEscapeCharacter(string[(int) i]);
                WriteStringToStingStream(ns, index, escaped);
            } else {
                WriteCharacterToStingStream(ns, index, string[(int) i]);
            }
        }

        return ns;
    }

    public static char[] JSONEscapeCharacter(char c) {
        double code;
        char escaped[];
        references.references.StringReference hexNumber;
        double q, rs, s, b, f, n, r, t;

        code = c;

        q = 34d;
        rs = 92d;
        s = 47d;
        b = 8d;
        f = 12d;
        n = 10d;
        r = 13d;
        t = 9d;

        hexNumber = new StringReference();

        if (code == q) {
            escaped = new char[2];
            escaped[0] = '\\';
            escaped[1] = '\"';
        } else if (code == rs) {
            escaped = new char[2];
            escaped[0] = '\\';
            escaped[1] = '\\';
        } else if (code == s) {
            escaped = new char[2];
            escaped[0] = '\\';
            escaped[1] = '/';
        } else if (code == b) {
            escaped = new char[2];
            escaped[0] = '\\';
            escaped[1] = 'b';
        } else if (code == f) {
            escaped = new char[2];
            escaped[0] = '\\';
            escaped[1] = 'f';
        } else if (code == n) {
            escaped = new char[2];
            escaped[0] = '\\';
            escaped[1] = 'n';
        } else if (code == r) {
            escaped = new char[2];
            escaped[0] = '\\';
            escaped[1] = 'r';
        } else if (code == t) {
            escaped = new char[2];
            escaped[0] = '\\';
            escaped[1] = 't';
        } else if (code >= 0d && code <= 31d) {
            escaped = new char[6];
            escaped[0] = '\\';
            escaped[1] = 'u';
            escaped[2] = '0';
            escaped[3] = '0';

            nCreateStringFromNumberWithCheck(code, 16d, hexNumber);

            if (hexNumber.string.length == 1d) {
                escaped[4] = '0';
                escaped[5] = hexNumber.string[0];
            } else if (hexNumber.string.length == 2d) {
                escaped[4] = hexNumber.string[0];
                escaped[5] = hexNumber.string[1];
            }
        } else {
            escaped = new char[1];
            escaped[0] = c;
        }

        return escaped;
    }

    public static void WriteStringToStingStream(char[] stream, NumberReference index, char[] src) {
        double i;
        for (i = 0; i < src.length; i = i + 1d) {
            stream[(int) (index.numberValue + i)] = src[(int) i];
        }
        index.numberValue = index.numberValue + src.length;
    }

    public static void WriteCharacterToStingStream(char[] stream, NumberReference index, char src) {
        stream[(int) (index.numberValue)] = src;
        index.numberValue = index.numberValue + 1d;
    }

    public static boolean JSONMustBeEscaped(char c, NumberReference letters) {
        double code;
        boolean mustBeEscaped;
        double q, rs, s, b, f, n, r, t;

        code = c;
        mustBeEscaped = false;

        q = 34d;
        rs = 92d;
        s = 47d;
        b = 8d;
        f = 12d;
        n = 10d;
        r = 13d;
        t = 9d;

        if (code == q || code == rs || code == s || code == b || code == f || code == n || code == r || code == t) {
            mustBeEscaped = true;
            letters.numberValue = 2d;
        } else if (code >= 0d && code <= 31d) {
            mustBeEscaped = true;
            letters.numberValue = 6d;
        } else if (code >= pow(2d, 16d)) {
            mustBeEscaped = true;
            letters.numberValue = 6d;
        }

        return mustBeEscaped;
    }

    public static String writeJSONExceptionOnFailure(Object o) throws JSONException {
        return writeJSON(o);
    }

    public static StringReturn writeJSONWithCheck(Object o) {
        StringReturn stringReturn = new StringReturn();

        try {
            stringReturn.success = true;
            stringReturn.errorMessage = "";
            stringReturn.str = writeJSON(o);
        } catch (Throwable ex) {
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