package lists.BooleanList;

import references.references.BooleanArrayReference;

public class BooleanList {
    public static boolean[] AddBoolean(boolean[] list, boolean a) {
        boolean[] newlist;
        double i;

        newlist = new boolean[(int) (list.length + 1d)];
        for (i = 0d; i < list.length; i = i + 1d) {
            newlist[(int) (i)] = list[(int) (i)];
        }
        newlist[(int) (list.length)] = a;

        delete(list);

        return newlist;
    }

    public static void AddBooleanRef(BooleanArrayReference list, boolean i) {

        list.booleanArray = AddBoolean(list.booleanArray, i);
    }

    public static boolean[] RemoveBoolean(boolean[] list, double n) {
        boolean[] newlist;
        double i;

        newlist = new boolean[(int) (list.length - 1d)];

        for (i = 0d; i < list.length; i = i + 1d) {
            if (i < n) {
                newlist[(int) (i)] = list[(int) (i)];
            }
            if (i > n) {
                newlist[(int) (i - 1d)] = list[(int) (i)];
            }
        }

        delete(list);

        return newlist;
    }

    public static boolean GetBooleanRef(BooleanArrayReference list, double i) {

        return list.booleanArray[(int) (i)];
    }

    public static void RemoveDecimalRef(BooleanArrayReference list, double i) {

        list.booleanArray = RemoveBoolean(list.booleanArray, i);
    }

    public static void delete(Object object) {
        // Java has garbage collection.
    }
}
