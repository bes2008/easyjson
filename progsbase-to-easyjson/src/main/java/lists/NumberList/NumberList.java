package lists.NumberList;

import static java.lang.Math.*;

import static arrays.arrays.arrays.*;

import references.references.*;
import static references.references.references.*;


import static lists.StringList.StringList.*;

import static lists.BooleanList.BooleanList.*;

import static lists.CharacterList.CharacterList.*;

public class NumberList{
    public static double [] AddNumber(double [] list, double a){
        double [] newlist;
        double i;

        newlist = new double [(int)(list.length + 1d)];
        for(i = 0d; i < list.length; i = i + 1d){
            newlist[(int)(i)] = list[(int)(i)];
        }
        newlist[(int)(list.length)] = a;

        delete(list);

        return newlist;
    }

    public static void AddNumberRef(NumberArrayReference list, double i){

        list.numberArray = AddNumber(list.numberArray, i);
    }

    public static double [] RemoveNumber(double [] list, double n){
        double [] newlist;
        double i;

        newlist = new double [(int)(list.length - 1d)];

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

    public static double GetNumberRef(NumberArrayReference list, double i){

        return list.numberArray[(int)(i)];
    }

    public static void RemoveNumberRef(NumberArrayReference list, double i){

        list.numberArray = RemoveNumber(list.numberArray, i);
    }

    public static void delete(Object object){
        // Java has garbage collection.
    }
}
