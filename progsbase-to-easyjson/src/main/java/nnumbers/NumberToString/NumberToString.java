package nnumbers.NumberToString;

import references.references.CharacterReference;
import references.references.StringReference;

import static java.lang.Math.*;
import static math.math.math.IsInteger;
import static strstrings.strings.strings.strAppendCharacter;
import static strstrings.strings.strings.strAppendString;

public class NumberToString {
    public static char[] nCreateStringScientificNotationDecimalFromNumber(double decimal) {
        StringReference mantissaReference, exponentReference;
        double multiplier, inc;
        double exponent;
        boolean done, isPositive;
        char[] result;

        mantissaReference = new StringReference();
        exponentReference = new StringReference();
        result = new char[0];
        done = false;
        exponent = 0d;

        if (decimal < 0d) {
            isPositive = false;
            decimal = -decimal;
        } else {
            isPositive = true;
        }

        if (decimal == 0d) {
            done = true;
        }

        if (!done) {
            multiplier = 0d;
            inc = 0d;
            if (decimal < 1d) {
                multiplier = 10d;
                inc = -1d;
            } else if (decimal >= 10d) {
                multiplier = 0.1;
                inc = 1d;
            } else {
                done = true;
            }

            if (!done) {
                for (; decimal >= 10d || decimal < 1d; ) {
                    decimal = decimal * multiplier;
                    exponent = exponent + inc;
                }
            }
        }

        nCreateStringFromNumberWithCheck(decimal, 10d, mantissaReference);

        nCreateStringFromNumberWithCheck(exponent, 10d, exponentReference);

        if (!isPositive) {
            result = strAppendString(result, "-".toCharArray());
        }

        result = strAppendString(result, mantissaReference.string);
        result = strAppendString(result, "e".toCharArray());
        result = strAppendString(result, exponentReference.string);

        return result;
    }

    public static char[] nCreateStringDecimalFromNumber(double decimal) {
        StringReference stringReference;

        stringReference = new StringReference();

        /* This will succeed because base = 10.*/
        nCreateStringFromNumberWithCheck(decimal, 10d, stringReference);

        return stringReference.string;
    }

    public static boolean nCreateStringFromNumberWithCheck(double decimal, double base, StringReference stringReference) {
        char[] string;
        double maximumDigits;
        double digitPosition;
        boolean hasPrintedPoint, isPositive;
        double i, d;
        boolean success;
        CharacterReference characterReference;
        char c;

        isPositive = true;

        if (decimal < 0d) {
            isPositive = false;
            decimal = -decimal;
        }

        if (decimal == 0d) {
            stringReference.string = "0".toCharArray();
            success = true;
        } else {
            characterReference = new CharacterReference();
            if (IsInteger(base)) {
                success = true;
                string = new char[0];
                maximumDigits = nGetMaximumDigitsForBase(base);
                digitPosition = nGetFirstDigitPosition(decimal, base);
                decimal = (double) round(decimal * pow(base, maximumDigits - digitPosition - 1d));
                hasPrintedPoint = false;
                if (!isPositive) {
                    string = strAppendCharacter(string, '-');
                }
                if (digitPosition < 0d) {
                    string = strAppendCharacter(string, '0');
                    string = strAppendCharacter(string, '.');
                    hasPrintedPoint = true;
                    for (i = 0d; i < -digitPosition - 1d; i = i + 1d) {
                        string = strAppendCharacter(string, '0');
                    }
                }
                for (i = 0d; i < maximumDigits && success; i = i + 1d) {
                    d = floor(decimal / pow(base, maximumDigits - i - 1d));
                    if (d >= base) {
                        d = base - 1d;
                    }
                    if (!hasPrintedPoint && digitPosition - i + 1d == 0d) {
                        if (decimal != 0d) {
                            string = strAppendCharacter(string, '.');
                        }
                        hasPrintedPoint = true;
                    }
                    if (decimal == 0d && hasPrintedPoint) {
                    } else {
                        success = nGetSingleDigitCharacterFromNumberWithCheck(d, base, characterReference);
                        if (success) {
                            c = characterReference.characterValue;
                            string = strAppendCharacter(string, c);
                        }
                    }
                    if (success) {
                        decimal = decimal - d * pow(base, maximumDigits - i - 1d);
                    }
                }
                if (success) {
                    for (i = 0d; i < digitPosition - maximumDigits + 1d; i = i + 1d) {
                        string = strAppendCharacter(string, '0');
                    }
                    stringReference.string = string;
                }
            } else {
                success = false;
            }
        }

        /* Done*/
        return success;
    }

    public static double nGetMaximumDigitsForBase(double base) {
        double t;

        t = pow(10d, 15d);
        return floor(log10(t) / log10(base));
    }

    public static double nGetFirstDigitPosition(double decimal, double base) {
        double power;
        double t;

        power = ceil(log10(decimal) / log10(base));

        t = decimal * pow(base, -power);
        if (t < base && t >= 1d) {
        } else if (t >= base) {
            power = power + 1d;
        } else if (t < 1d) {
            power = power - 1d;
        }


        return power;
    }

    public static boolean nGetSingleDigitCharacterFromNumberWithCheck(double c, double base, CharacterReference characterReference) {
        char[] numberTable;
        boolean success;

        numberTable = nGetDigitCharacterTable();

        if (c < base || c < numberTable.length) {
            success = true;
            characterReference.characterValue = numberTable[(int) (c)];
        } else {
            success = false;
        }

        return success;
    }

    public static char[] nGetDigitCharacterTable() {
        char[] numberTable;

        numberTable = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        return numberTable;
    }

    public static void delete(Object object) {
        // Java has garbage collection.
    }
}
