package arrays.arrays;


import com.jn.langx.util.collection.PrimitiveArrays;
import references.references.BooleanArrayReference;
import references.references.NumberArrayReference;
import references.references.StringReference;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class arrays {
    public static double[] StringToNumberArray(char[] string) {
        double[] array = new double[(int) (string.length)];
        for (int i = 0; i < string.length; i = i + 1) {
            array[(int) (i)] = string[(int) (i)];
        }
        return array;
    }

    public static char[] NumberArrayToString(double[] array) {
        double i;
        char[] string;

        string = new char[(int) (array.length)];

        for (i = 0d; i < array.length; i = i + 1d) {
            string[(int) (i)] = (char) (array[(int) (i)]);
        }
        return string;
    }

    public static boolean NumberArraysEqual(double[] a, double[] b) {
        return primitiveEquels(PrimitiveArrays.wrap(a, false), PrimitiveArrays.wrap(b, false));
    }

    public static boolean BooleanArraysEqual(boolean[] a, boolean[] b) {
        return primitiveEquels(PrimitiveArrays.wrap(a, false), PrimitiveArrays.wrap(b, false));
    }

    private static <T> boolean primitiveEquels(T[] a, T[] b) {
        boolean equal = true;

        if (a.length == b.length) {
            for (int i = 0; i < a.length && equal; i = i + 1) {
                if (a[i] != b[i]) {
                    equal = false;
                }
            }
        } else {
            equal = false;
        }

        return equal;
    }

    public static boolean StringsEqual(char[] a, char[] b) {
        return new String(a).equals(new String(b));
    }

    public static void FillNumberArray(double[] a, double value) {
        double i;

        for (i = 0d; i < a.length; i = i + 1d) {
            a[(int) (i)] = value;
        }
    }

    public static void FillString(char[] a, char value) {
        double i;

        for (i = 0d; i < a.length; i = i + 1d) {
            a[(int) (i)] = value;
        }
    }

    public static void FillBooleanArray(boolean[] a, boolean value) {
        double i;

        for (i = 0d; i < a.length; i = i + 1d) {
            a[(int) (i)] = value;
        }
    }

    public static boolean FillNumberArrayInterval(double[] a, double value, double from, double to) {
        double i;
        boolean success;

        if (from >= 0d && from < a.length && to >= 0d && to < a.length) {
            for (i = from; i < to; i = i + 1d) {
                a[(int) (i)] = value;
            }
            success = true;
        } else {
            success = false;
        }

        return success;
    }

    public static boolean FillBooleanArrayInterval(boolean[] a, boolean value, double from, double to) {
        double i;
        boolean success;

        if (from >= 0d && from < a.length && to >= 0d && to < a.length) {
            for (i = max(from, 0d); i < min(to, a.length); i = i + 1d) {
                a[(int) (i)] = value;
            }
            success = true;
        } else {
            success = false;
        }

        return success;
    }

    public static boolean FillStringInterval(char[] a, char value, double from, double to) {
        double i;
        boolean success;

        if (from >= 0d && from < a.length && to >= 0d && to < a.length) {
            for (i = max(from, 0d); i < min(to, a.length); i = i + 1d) {
                a[(int) (i)] = value;
            }
            success = true;
        } else {
            success = false;
        }

        return success;
    }

    public static double[] CopyNumberArray(double[] a) {
        double i;
        double[] n;

        n = new double[(int) (a.length)];

        for (i = 0d; i < a.length; i = i + 1d) {
            n[(int) (i)] = a[(int) (i)];
        }

        return n;
    }

    public static boolean[] CopyBooleanArray(boolean[] a) {
        double i;
        boolean[] n;

        n = new boolean[(int) (a.length)];

        for (i = 0d; i < a.length; i = i + 1d) {
            n[(int) (i)] = a[(int) (i)];
        }

        return n;
    }

    public static char[] CopyString(char[] a) {
        double i;
        char[] n;

        n = new char[(int) (a.length)];

        for (i = 0d; i < a.length; i = i + 1d) {
            n[(int) (i)] = a[(int) (i)];
        }

        return n;
    }

    public static boolean CopyNumberArrayRange(double[] a, double from, double to, NumberArrayReference copyReference) {
        double i, length;
        double[] n;
        boolean success;

        if (from >= 0d && from < a.length && to >= 0d && to < a.length && from <= to) {
            length = to - from;
            n = new double[(int) (length)];
            for (i = 0d; i < length; i = i + 1d) {
                n[(int) (i)] = a[(int) (from + i)];
            }
            copyReference.numberArray = n;
            success = true;
        } else {
            success = false;
        }

        return success;
    }

    public static boolean CopyBooleanArrayRange(boolean[] a, double from, double to, BooleanArrayReference copyReference) {
        double i, length;
        boolean[] n;
        boolean success;

        if (from >= 0d && from < a.length && to >= 0d && to < a.length && from <= to) {
            length = to - from;
            n = new boolean[(int) (length)];
            for (i = 0d; i < length; i = i + 1d) {
                n[(int) (i)] = a[(int) (from + i)];
            }
            copyReference.booleanArray = n;
            success = true;
        } else {
            success = false;
        }

        return success;
    }

    public static boolean CopyStringRange(char[] a, double from, double to, StringReference copyReference) {
        double i, length;
        char[] n;
        boolean success;

        if (from >= 0d && from < a.length && to >= 0d && to < a.length && from <= to) {
            length = to - from;
            n = new char[(int) (length)];
            for (i = 0d; i < length; i = i + 1d) {
                n[(int) (i)] = a[(int) (from + i)];
            }
            copyReference.string = n;
            success = true;
        } else {
            success = false;
        }

        return success;
    }

    public static boolean IsLastElement(double length, double index) {
        return index + 1d == length;
    }

    public static double[] CreateNumberArray(double length, double value) {
        double[] array;

        array = new double[(int) (length)];
        FillNumberArray(array, value);

        return array;
    }

    public static boolean[] CreateBooleanArray(double length, boolean value) {
        boolean[] array;

        array = new boolean[(int) (length)];
        FillBooleanArray(array, value);

        return array;
    }

    public static char[] CreateString(double length, char value) {
        char[] array;

        array = new char[(int) (length)];
        FillString(array, value);

        return array;
    }

    public static void delete(Object object) {
        // Java has garbage collection.
    }
}
