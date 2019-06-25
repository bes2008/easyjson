package JSON.StringElementMaps;


import JSON.structures.Element;
import references.references.BooleanReference;
import references.references.StringArrayReference;

import static JSON.ElementLists.ElementLists.AddElementRef;
import static arrays.arrays.arrays.StringsEqual;
import static lists.StringList.StringList.AddStringRef;
import static references.references.references.CreateStringReference;

public class StringElementMaps {
    public static StringArrayReference GetStringElementMapKeySet(StringElementMap stringElementMap) {
        return stringElementMap.stringListRef;
    }

    public static Element GetObjectValue(StringElementMap stringElementMap, char[] key) {
        Element result;
        double i;

        result = new Element();

        for (i = 0; i < GetStringElementMapNumberOfKeys(stringElementMap); i = i + 1d) {
            if (StringsEqual(stringElementMap.stringListRef.stringArray[(int) i].string, key)) {
                result = stringElementMap.elementListRef.array[(int) i];
            }
        }

        return result;
    }

    public static Element GetObjectValueWithCheck(StringElementMap stringElementMap, char[] key, BooleanReference foundReference) {
        Element result;
        double i;

        result = new Element();

        foundReference.booleanValue = false;
        for (i = 0; i < GetStringElementMapNumberOfKeys(stringElementMap); i = i + 1d) {
            if (StringsEqual(stringElementMap.stringListRef.stringArray[(int) i].string, key)) {
                result = stringElementMap.elementListRef.array[(int) i];
                foundReference.booleanValue = true;
            }
        }

        return result;
    }

    public static void PutStringElementMap(StringElementMap stringElementMap, char[] keystring, Element value) {
        AddStringRef(stringElementMap.stringListRef, CreateStringReference(keystring));
        AddElementRef(stringElementMap.elementListRef, value);
    }

    public static void SetStringElementMap(StringElementMap stringElementMap, double index, char[] keystring, Element value) {
        stringElementMap.stringListRef.stringArray[(int) index].string = keystring;
        stringElementMap.elementListRef.array[(int) index] = value;
    }

    public static double GetStringElementMapNumberOfKeys(StringElementMap stringElementMap) {
        return stringElementMap.stringListRef.stringArray.length;
    }
}