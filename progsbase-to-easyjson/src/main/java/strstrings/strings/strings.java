package strstrings.strings;

import references.references.BooleanReference;
import references.references.NumberReference;
import references.references.StringReference;

import static charCharacters.Characters.Characters.*;
import static java.lang.Math.max;
import static lists.StringList.StringList.AddString;


public class strings{
    public static boolean strSubstringWithCheck(char [] string, double from, double to, StringReference stringReference){
        boolean success;

        if(from < string.length && to < string.length && from <= to && from >= 0d && to >= 0d){
            stringReference.string = strSubstring(string, from, to);
            success = true;
        }else{
            success = false;
        }

        return success;
    }

    public static char [] strSubstring(char [] string, double from, double to){
        char [] n;
        double i;

        n = new char [(int)(max(to - from, 0d))];

        for(i = from; i < to; i = i + 1d){
            n[(int)(i - from)] = string[(int)(i)];
        }

        return n;
    }

    public static char [] strAppendString(char [] string, char [] s){
        char [] newString;

        newString = strConcatenateString(string, s);

        delete(string);

        return newString;
    }

    public static char [] strConcatenateString(char [] string, char [] s){
        char [] newString;
        double i;

        newString = new char [(int)(string.length + s.length)];

        for(i = 0d; i < string.length; i = i + 1d){
            newString[(int)(i)] = string[(int)(i)];
        }

        for(i = 0d; i < s.length; i = i + 1d){
            newString[(int)(string.length + i)] = s[(int)(i)];
        }

        return newString;
    }

    public static char [] strAppendCharacter(char [] string, char c){
        char [] newString;

        newString = strConcatenateCharacter(string, c);

        delete(string);

        return newString;
    }

    public static char [] strConcatenateCharacter(char [] string, char c){
        char [] newString;
        double i;

        newString = new char [(int)(string.length + 1d)];

        for(i = 0d; i < string.length; i = i + 1d){
            newString[(int)(i)] = string[(int)(i)];
        }

        newString[(int)(string.length)] = c;

        return newString;
    }

    public static StringReference [] strSplitByCharacter(char [] toSplit, char splitBy){
        StringReference [] split;
        char [] stringToSplitBy;

        stringToSplitBy = new char [1];
        stringToSplitBy[0] = splitBy;

        split = strSplitByString(toSplit, stringToSplitBy);

        delete(stringToSplitBy);

        return split;
    }

    public static boolean strIndexOfCharacter(char [] string, char character, NumberReference indexReference){
        double i;
        boolean found;

        found = false;
        for(i = 0d; i < string.length && !found; i = i + 1d){
            if(string[(int)(i)] == character){
                found = true;
                indexReference.numberValue = i;
            }
        }

        return found;
    }

    public static boolean strSubstringEqualsWithCheck(char [] string, double from, char [] substring, BooleanReference equalsReference){
        boolean success;

        if(from < string.length){
            success = true;
            equalsReference.booleanValue = strSubstringEquals(string, from, substring);
        }else{
            success = false;
        }

        return success;
    }

    public static boolean strSubstringEquals(char [] string, double from, char [] substring){
        double i;
        boolean equal;

        equal = true;
        for(i = 0d; i < substring.length && equal; i = i + 1d){
            if(string[(int)(from + i)] != substring[(int)(i)]){
                equal = false;
            }
        }

        return equal;
    }

    public static boolean strIndexOfString(char [] string, char [] substring, NumberReference indexReference){
        double i;
        boolean found;

        found = false;
        for(i = 0d; i < string.length - substring.length + 1d && !found; i = i + 1d){
            if(strSubstringEquals(string, i, substring)){
                found = true;
                indexReference.numberValue = i;
            }
        }

        return found;
    }

    public static boolean strContainsCharacter(char [] string, char character){

        return strIndexOfCharacter(string, character, new NumberReference());
    }

    public static boolean strContainsString(char [] string, char [] substring){

        return strIndexOfString(string, substring, new NumberReference());
    }

    public static void strToUpperCase(char [] string){
        double i;

        for(i = 0d; i < string.length; i = i + 1d){
            string[(int)(i)] = charToUpperCase(string[(int)(i)]);
        }
    }

    public static void strToLowerCase(char [] string){
        double i;

        for(i = 0d; i < string.length; i = i + 1d){
            string[(int)(i)] = charToLowerCase(string[(int)(i)]);
        }
    }

    public static boolean strEqualsIgnoreCase(char [] a, char [] b){
        boolean equal;
        double i;

        if(a.length == b.length){
            equal = true;
            for(i = 0d; i < a.length && equal; i = i + 1d){
                if(charToLowerCase(a[(int)(i)]) != charToLowerCase(b[(int)(i)])){
                    equal = false;
                }
            }
        }else{
            equal = false;
        }

        return equal;
    }

    public static char [] strReplacesString(char [] string, char [] toReplace, char [] replaceWith){
        char [] result;
        double i;
        BooleanReference equalsReference;
        boolean success;

        equalsReference = new BooleanReference();
        result = new char [0];

        for(i = 0d; i < string.length; ){
            success = strSubstringEqualsWithCheck(string, i, toReplace, equalsReference);
            if(success){
                success = equalsReference.booleanValue;
            }
            if(success && toReplace.length > 0d){
                result = strConcatenateString(result, replaceWith);
                i = i + toReplace.length;
            }else{
                result = strConcatenateCharacter(result, string[(int)(i)]);
                i = i + 1d;
            }
        }

        return result;
    }

    public static char [] strReplaceCharacter(char [] string, char toReplace, char replaceWith){
        char [] result;
        double i;

        result = new char [0];

        for(i = 0d; i < string.length; i = i + 1d){
            if(string[(int)(i)] == toReplace){
                result = strConcatenateCharacter(result, replaceWith);
            }else{
                result = strConcatenateCharacter(result, string[(int)(i)]);
            }
        }

        return result;
    }

    public static char [] strTrim(char [] string){
        char [] result;
        double i, lastWhitespaceLocationStart, lastWhitespaceLocationEnd;
        boolean firstNonWhitespaceFound;

        lastWhitespaceLocationStart = -1d ;
        firstNonWhitespaceFound = false;
        for(i = 0d; i < string.length && !firstNonWhitespaceFound; i = i + 1d){
            if(charIsWhiteSpace(string[(int)(i)])){
                lastWhitespaceLocationStart = i;
            }else{
                firstNonWhitespaceFound = true;
            }
        }

        /* Find whitepaces at the end.*/
        lastWhitespaceLocationEnd = string.length;
        firstNonWhitespaceFound = false;
        for(i = string.length - 1d; i >= 0d && !firstNonWhitespaceFound; i = i - 1d){
            if(charIsWhiteSpace(string[(int)(i)])){
                lastWhitespaceLocationEnd = i;
            }else{
                firstNonWhitespaceFound = true;
            }
        }

        if(lastWhitespaceLocationStart < lastWhitespaceLocationEnd){
            result = strSubstring(string, lastWhitespaceLocationStart + 1d, lastWhitespaceLocationEnd);
        }else{
            result = new char [0];
        }

        return result;
    }

    public static boolean strStartsWith(char [] string, char [] start){
        boolean startsWithString;

        startsWithString = false;
        if(string.length >= start.length){
            startsWithString = strSubstringEquals(string, 0d, start);
        }

        return startsWithString;
    }

    public static boolean strEndsWith(char [] string, char [] end){
        boolean endsWithString;

        endsWithString = false;
        if(string.length >= end.length){
            endsWithString = strSubstringEquals(string, string.length - end.length, end);
        }

        return endsWithString;
    }

    public static StringReference [] strSplitByString(char [] toSplit, char [] splitBy){
        StringReference [] split;
        char [] next;
        double i;
        char c;
        StringReference n;

        split = new StringReference [0];

        next = new char [0];
        for(i = 0d; i < toSplit.length; ){
            c = toSplit[(int)(i)];
            if(strSubstringEquals(toSplit, i, splitBy)){
                if(split.length != 0d || i != 0d){
                    n = new StringReference();
                    n.string = next;
                    split = AddString(split, n);
                    next = new char [0];
                    i = i + splitBy.length;
                }
            }else{
                next = strAppendCharacter(next, c);
                i = i + 1d;
            }
        }

        if(next.length > 0d){
            n = new StringReference();
            n.string = next;
            split = AddString(split, n);
        }

        return split;
    }

    public static void delete(Object object){
        // Java has garbage collection.
    }
}
