package nnumbers.StringToNumber;

import references.references.BooleanReference;
import references.references.NumberArrayReference;
import references.references.NumberReference;
import references.references.StringReference;

import static java.lang.Math.pow;
import static nnumbers.NumberToString.NumberToString.nGetDigitCharacterTable;
import static references.references.references.*;
import static strstrings.strings.strings.strSplitByString;
import static strstrings.strings.strings.strTrim;

public class StringToNumber {
    public static boolean nCreateNumberFromDecimalStringWithCheck(char[] string, NumberReference decimalReference, StringReference errorMessage) {

        return nCreateNumberFromStringWithCheck(string, 10d, decimalReference, errorMessage);
    }

    public static double nCreateNumberFromDecimalString(char[] string) {
        NumberReference doubleReference;
        StringReference stringReference;
        double number;

        doubleReference = CreateNumberReference(0d);
        stringReference = CreateStringReference("".toCharArray());
        nCreateNumberFromStringWithCheck(string, 10d, doubleReference, stringReference);
        number = doubleReference.numberValue;

        delete(doubleReference);
        delete(stringReference);

        return number;
    }

    public static boolean nCreateNumberFromStringWithCheck(char[] string, double base, NumberReference numberReference, StringReference errorMessage) {
        boolean success;
        BooleanReference numberIsPositive, exponentIsPositive;
        NumberArrayReference beforePoint, afterPoint, exponent;

        numberIsPositive = CreateBooleanReference(true);
        exponentIsPositive = CreateBooleanReference(true);
        beforePoint = new NumberArrayReference();
        afterPoint = new NumberArrayReference();
        exponent = new NumberArrayReference();

        if (base >= 2d && base <= 36d) {
            success = nExtractPartsFromNumberString(string, base, numberIsPositive, beforePoint, afterPoint, exponentIsPositive, exponent, errorMessage);
            if (success) {
                numberReference.numberValue = nCreateNumberFromParts(base, numberIsPositive.booleanValue, beforePoint.numberArray, afterPoint.numberArray, exponentIsPositive.booleanValue, exponent.numberArray);
            }
        } else {
            success = false;
            errorMessage.string = "Base must be from 2 to 36.".toCharArray();
        }

        return success;
    }

    public static double nCreateNumberFromParts(double base, boolean numberIsPositive, double[] beforePoint, double[] afterPoint, boolean exponentIsPositive, double[] exponent) {
        double n, i, p, e;

        n = 0d;

        for (i = 0d; i < beforePoint.length; i = i + 1d) {
            p = beforePoint[(int) (beforePoint.length - i - 1d)];
            n = n + p * pow(base, i);
        }

        for (i = 0d; i < afterPoint.length; i = i + 1d) {
            p = afterPoint[(int) (i)];
            n = n + p * pow(base, -(i + 1d));
        }

        if (exponent.length > 0d) {
            e = 0d;
            for (i = 0d; i < exponent.length; i = i + 1d) {
                p = exponent[(int) (exponent.length - i - 1d)];
                e = e + p * pow(base, i);
            }
            if (!exponentIsPositive) {
                e = -e;
            }
            n = n * pow(base, e);
        }

        if (!numberIsPositive) {
            n = -n;
        }

        return n;
    }

    public static boolean nExtractPartsFromNumberString(char[] n, double base, BooleanReference numberIsPositive, NumberArrayReference beforePoint, NumberArrayReference afterPoint, BooleanReference exponentIsPositive, NumberArrayReference exponent, StringReference errorMessages) {
        double i;
        boolean success;

        i = 0d;

        if (i < n.length) {
            if (n[(int) (i)] == '-') {
                numberIsPositive.booleanValue = false;
                i = i + 1d;
            } else if (n[(int) (i)] == '+') {
                numberIsPositive.booleanValue = true;
                i = i + 1d;
            }

            success = nExtractPartsFromNumberStringFromSign(n, base, i, beforePoint, afterPoint, exponentIsPositive, exponent, errorMessages);
        } else {
            success = false;
            errorMessages.string = "Number cannot have length zero.".toCharArray();
        }

        return success;
    }

    public static boolean nExtractPartsFromNumberStringFromSign(char[] n, double base, double i, NumberArrayReference beforePoint, NumberArrayReference afterPoint, BooleanReference exponentIsPositive, NumberArrayReference exponent, StringReference errorMessages) {
        boolean success, done;
        double count, j;

        done = false;
        count = 0d;
        for (; i + count < n.length && !done; ) {
            if (nCharacterIsNumberCharacterInBase(n[(int) (i + count)], base)) {
                count = count + 1d;
            } else {
                done = true;
            }
        }

        if (count >= 1d) {
            beforePoint.numberArray = new double[(int) (count)];
            for (j = 0d; j < count; j = j + 1d) {
                beforePoint.numberArray[(int) (j)] = nGetNumberFromNumberCharacterForBase(n[(int) (i + j)], base);
            }
            i = i + count;
            if (i < n.length) {
                success = nExtractPartsFromNumberStringFromPointOrExponent(n, base, i, afterPoint, exponentIsPositive, exponent, errorMessages);
            } else {
                afterPoint.numberArray = new double[0];
                exponent.numberArray = new double[0];
                success = true;
            }
        } else {
            success = false;
            errorMessages.string = "Number must have at least one number after the optional sign.".toCharArray();
        }

        return success;
    }

    public static boolean nExtractPartsFromNumberStringFromPointOrExponent(char[] n, double base, double i, NumberArrayReference afterPoint, BooleanReference exponentIsPositive, NumberArrayReference exponent, StringReference errorMessages) {
        boolean success, done;
        double count, j;

        if (n[(int) (i)] == '.') {
            i = i + 1d;
            if (i < n.length) {
                done = false;
                count = 0d;
                for (; i + count < n.length && !done; ) {
                    if (nCharacterIsNumberCharacterInBase(n[(int) (i + count)], base)) {
                        count = count + 1d;
                    } else {
                        done = true;
                    }
                }
                if (count >= 1d) {
                    afterPoint.numberArray = new double[(int) (count)];
                    for (j = 0d; j < count; j = j + 1d) {
                        afterPoint.numberArray[(int) (j)] = nGetNumberFromNumberCharacterForBase(n[(int) (i + j)], base);
                    }
                    i = i + count;
                    if (i < n.length) {
                        success = nExtractPartsFromNumberStringFromExponent(n, base, i, exponentIsPositive, exponent, errorMessages);
                    } else {
                        exponent.numberArray = new double[0];
                        success = true;
                    }
                } else {
                    success = false;
                    errorMessages.string = "There must be at least one digit after the decimal point.".toCharArray();
                }
            } else {
                success = false;
                errorMessages.string = "There must be at least one digit after the decimal point.".toCharArray();
            }
        } else if (base <= 14d && (n[(int) (i)] == 'e' || n[(int) (i)] == 'E')) {
            if (i < n.length) {
                success = nExtractPartsFromNumberStringFromExponent(n, base, i, exponentIsPositive, exponent, errorMessages);
                afterPoint.numberArray = new double[0];
            } else {
                success = false;
                errorMessages.string = "There must be at least one digit after the exponent.".toCharArray();
            }
        } else {
            success = false;
            errorMessages.string = "Expected decimal point or exponent symbol.".toCharArray();
        }


        return success;
    }

    public static boolean nExtractPartsFromNumberStringFromExponent(char[] n, double base, double i, BooleanReference exponentIsPositive, NumberArrayReference exponent, StringReference errorMessages) {
        boolean success, done;
        double count, j;

        if (base <= 14d && (n[(int) (i)] == 'e' || n[(int) (i)] == 'E')) {
            i = i + 1d;
            if (i < n.length) {
                if (n[(int) (i)] == '-') {
                    exponentIsPositive.booleanValue = false;
                    i = i + 1d;
                } else if (n[(int) (i)] == '+') {
                    exponentIsPositive.booleanValue = true;
                    i = i + 1d;
                }

                if (i < n.length) {
                    done = false;
                    count = 0d;
                    for (; i + count < n.length && !done; ) {
                        if (nCharacterIsNumberCharacterInBase(n[(int) (i + count)], base)) {
                            count = count + 1d;
                        } else {
                            done = true;
                        }
                    }
                    if (count >= 1d) {
                        exponent.numberArray = new double[(int) (count)];
                        for (j = 0d; j < count; j = j + 1d) {
                            exponent.numberArray[(int) (j)] = nGetNumberFromNumberCharacterForBase(n[(int) (i + j)], base);
                        }
                        i = i + count;
                        if (i == n.length) {
                            success = true;
                        } else {
                            success = false;
                            errorMessages.string = "There cannot be any characters past the exponent of the number.".toCharArray();
                        }
                    } else {
                        success = false;
                        errorMessages.string = "There must be at least one digit after the decimal point.".toCharArray();
                    }
                } else {
                    success = false;
                    errorMessages.string = "There must be at least one digit after the exponent symbol.".toCharArray();
                }
            } else {
                success = false;
                errorMessages.string = "There must be at least one digit after the exponent symbol.".toCharArray();
            }
        } else {
            success = false;
            errorMessages.string = "Expected exponent symbol.".toCharArray();
        }

        return success;
    }

    public static double nGetNumberFromNumberCharacterForBase(char c, double base) {
        char[] numberTable;
        double i;
        double position;

        numberTable = nGetDigitCharacterTable();
        position = 0d;

        for (i = 0d; i < base; i = i + 1d) {
            if (numberTable[(int) (i)] == c) {
                position = i;
            }
        }

        return position;
    }

    public static boolean nCharacterIsNumberCharacterInBase(char c, double base) {
        char[] numberTable;
        double i;
        boolean found;

        numberTable = nGetDigitCharacterTable();
        found = false;

        for (i = 0d; i < base; i = i + 1d) {
            if (numberTable[(int) (i)] == c) {
                found = true;
            }
        }

        return found;
    }

    public static double[] nStringToNumberArray(char[] str) {
        NumberArrayReference numberArrayReference;
        StringReference stringReference;
        double[] numbers;

        numberArrayReference = new NumberArrayReference();
        stringReference = new StringReference();

        nStringToNumberArrayWithCheck(str, numberArrayReference, stringReference);

        numbers = numberArrayReference.numberArray;

        delete(numberArrayReference);
        delete(stringReference);

        return numbers;
    }

    public static boolean nStringToNumberArrayWithCheck(char[] str, NumberArrayReference numberArrayReference, StringReference errorMessage) {
        StringReference[] numberStrings;
        double[] numbers;
        double i;
        char[] numberString, trimmedNumberString;
        boolean success;
        NumberReference numberReference;

        numberStrings = strSplitByString(str, ",".toCharArray());

        numbers = new double[(int) (numberStrings.length)];
        success = true;
        numberReference = new NumberReference();

        for (i = 0d; i < numberStrings.length; i = i + 1d) {
            numberString = numberStrings[(int) (i)].string;
            trimmedNumberString = strTrim(numberString);
            success = nCreateNumberFromDecimalStringWithCheck(trimmedNumberString, numberReference, errorMessage);
            numbers[(int) (i)] = numberReference.numberValue;
            FreeStringReference(numberStrings[(int) (i)]);
            delete(trimmedNumberString);
        }

        delete(numberStrings);
        delete(numberReference);

        numberArrayReference.numberArray = numbers;

        return success;
    }

    public static void delete(Object object) {
        // Java has garbage collection.
    }
}
