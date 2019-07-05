package references.references;


public class references {
    public static BooleanReference CreateBooleanReference(boolean value) {
        BooleanReference ref;

        ref = new BooleanReference();
        ref.booleanValue = value;

        return ref;
    }

    public static BooleanArrayReference CreateBooleanArrayReference(boolean[] value) {
        BooleanArrayReference ref;

        ref = new BooleanArrayReference();
        ref.booleanArray = value;

        return ref;
    }

    public static BooleanArrayReference CreateBooleanArrayReferenceLengthValue(double length, boolean value) {
        BooleanArrayReference ref;
        double i;

        ref = new BooleanArrayReference();
        ref.booleanArray = new boolean[(int) (length)];

        for (i = 0d; i < length; i = i + 1d) {
            ref.booleanArray[(int) (i)] = value;
        }

        return ref;
    }

    public static void FreeBooleanArrayReference(BooleanArrayReference booleanArrayReference) {

        delete(booleanArrayReference.booleanArray);
        delete(booleanArrayReference);
    }

    public static CharacterReference CreateCharacterReference(char value) {
        CharacterReference ref;

        ref = new CharacterReference();
        ref.characterValue = value;

        return ref;
    }

    public static NumberReference CreateNumberReference(double value) {
        NumberReference ref;

        ref = new NumberReference();
        ref.numberValue = value;

        return ref;
    }

    public static NumberArrayReference CreateNumberArrayReference(double[] value) {
        NumberArrayReference ref;

        ref = new NumberArrayReference();
        ref.numberArray = value;

        return ref;
    }

    public static NumberArrayReference CreateNumberArrayReferenceLengthValue(double length, double value) {
        NumberArrayReference ref;
        double i;

        ref = new NumberArrayReference();
        ref.numberArray = new double[(int) (length)];

        for (i = 0d; i < length; i = i + 1d) {
            ref.numberArray[(int) (i)] = value;
        }

        return ref;
    }

    public static void FreeNumberArrayReference(NumberArrayReference numberArrayReference) {

        delete(numberArrayReference.numberArray);
        delete(numberArrayReference);
    }

    public static StringReference CreateStringReference(char[] value) {
        StringReference ref;

        ref = new StringReference();
        ref.string = value;

        return ref;
    }

    public static StringReference CreateStringReferenceLengthValue(double length, char value) {
        StringReference ref;
        double i;

        ref = new StringReference();
        ref.string = new char[(int) (length)];

        for (i = 0d; i < length; i = i + 1d) {
            ref.string[(int) (i)] = value;
        }

        return ref;
    }

    public static void FreeStringReference(StringReference stringReference) {

        delete(stringReference.string);
        delete(stringReference);
    }

    public static StringArrayReference CreateStringArrayReference(StringReference[] strings) {
        StringArrayReference ref;

        ref = new StringArrayReference();
        ref.stringArray = strings;

        return ref;
    }

    public static StringArrayReference CreateStringArrayReferenceLengthValue(double length, char[] value) {
        StringArrayReference ref;
        double i;

        ref = new StringArrayReference();
        ref.stringArray = new StringReference[(int) (length)];

        for (i = 0d; i < length; i = i + 1d) {
            ref.stringArray[(int) (i)] = CreateStringReference(value);
        }

        return ref;
    }

    public static void FreeStringArrayReference(StringArrayReference stringArrayReference) {
        double i;

        for (i = 0d; i < stringArrayReference.stringArray.length; i = i + 1d) {
            delete(stringArrayReference.stringArray[(int) (i)]);
        }
        delete(stringArrayReference.stringArray);
        delete(stringArrayReference);
    }

    public static void delete(Object object) {
        // Java has garbage collection.
    }
}