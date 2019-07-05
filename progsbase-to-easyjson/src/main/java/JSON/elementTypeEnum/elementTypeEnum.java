package JSON.elementTypeEnum;


import JSON.structures.ElementType;
import references.references.BooleanReference;

import static arrays.arrays.arrays.StringsEqual;

public class elementTypeEnum {
    public static ElementType GetElementType(char[] elementTypeName) {
        ElementType et;

        et = new ElementType();
        et.name = elementTypeName;

        return et;
    }

    public static ElementType GetAndCheckElementType(char[] elementTypeName, BooleanReference found) {
        ElementType[] elementTypes;
        ElementType elementType;
        double i, antall;

        antall = 6;

        elementTypes = new ElementType[(int) antall];

        for (i = 0; i < antall; i = i + 1d) {
            elementTypes[(int) i] = new ElementType();
        }

        elementTypes[0].name = "object".toCharArray();
        elementTypes[1].name = "array".toCharArray();
        elementTypes[2].name = "string".toCharArray();
        elementTypes[3].name = "number".toCharArray();
        elementTypes[4].name = "booleanValue".toCharArray();
        elementTypes[5].name = "nullValue".toCharArray();

        found.booleanValue = false;
        elementType = new ElementType();
        for (i = 0; i < antall && !found.booleanValue; i = i + 1d) {
            elementType = elementTypes[(int) i];
            if (StringsEqual(elementType.name, elementTypeName)) {
                found.booleanValue = true;
            }
        }

        return elementType;
    }

    public static boolean ElementTypeStructureEquals(ElementType a, ElementType b) {
        return StringsEqual(a.name, b.name);
    }

    public static boolean ElementTypeEnumEquals(char[] a, char[] b) {
        boolean equals;
        ElementType eta, etb;
        BooleanReference founda, foundb;

        founda = new BooleanReference();
        foundb = new BooleanReference();

        eta = GetAndCheckElementType(a, founda);
        etb = GetAndCheckElementType(b, foundb);

        if (founda.booleanValue && foundb.booleanValue) {
            equals = ElementTypeStructureEquals(eta, etb);
        } else {
            equals = false;
        }

        return equals;
    }
}
