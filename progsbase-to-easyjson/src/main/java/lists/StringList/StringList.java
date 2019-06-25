package lists.StringList;

import references.references.StringArrayReference;
import references.references.StringReference;

public class StringList{
    public static StringReference [] AddString(StringReference [] list, StringReference a){
        StringReference [] newlist;
        double i;

        newlist = new StringReference [(int)(list.length + 1d)];

        for(i = 0d; i < list.length; i = i + 1d){
            newlist[(int)(i)] = list[(int)(i)];
        }
        newlist[(int)(list.length)] = a;

        delete(list);

        return newlist;
    }

    public static void AddStringRef(StringArrayReference list, StringReference i){

        list.stringArray = AddString(list.stringArray, i);
    }

    public static StringReference [] RemoveString(StringReference [] list, double n){
        StringReference [] newlist;
        double i;

        newlist = new StringReference [(int)(list.length - 1d)];

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

    public static StringReference GetStringRef(StringArrayReference list, double i){

        return list.stringArray[(int)(i)];
    }

    public static void RemoveStringRef(StringArrayReference list, double i){

        list.stringArray = RemoveString(list.stringArray, i);
    }

    public static void delete(Object object){
        // Java has garbage collection.
    }
}
