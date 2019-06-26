package lists.CharacterList;

import static java.lang.Math.*;

import static arrays.arrays.arrays.*;

import references.references.*;
import static references.references.references.*;


import static lists.NumberList.NumberList.*;

import static lists.StringList.StringList.*;

import static lists.BooleanList.BooleanList.*;

public class CharacterList{
    public static char [] AddCharacter(char [] list, char a){
        char [] newlist;
        double i;

        newlist = new char [(int)(list.length + 1d)];
        for(i = 0d; i < list.length; i = i + 1d){
            newlist[(int)(i)] = list[(int)(i)];
        }
        newlist[(int)(list.length)] = a;

        delete(list);

        return newlist;
    }

    public static void AddCharacterRef(StringReference list, char i){

        list.string = AddCharacter(list.string, i);
    }

    public static char [] RemoveCharacter(char [] list, double n){
        char [] newlist;
        double i;

        newlist = new char [(int)(list.length - 1d)];

        for(i = 0d; i < list.length; i = i + 1d){
            if(i < n){
                newlist[(int)(i)] = list[(int)(i)];
            }
            if(i > n){
                newlist[(int)(i - 1d)] = list[(int)(i)];
            }
        }

        delete(list);

        return newlist;
    }

    public static char GetCharacterRef(StringReference list, double i){

        return list.string[(int)(i)];
    }

    public static void RemoveCharacterRef(StringReference list, double i){

        list.string = RemoveCharacter(list.string, i);
    }

    public static void delete(Object object){
        // Java has garbage collection.
    }
}
