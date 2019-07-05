package JSON.ElementLists;


import JSON.structures.Element;

public class ElementLists {
    public static Element[] AddElement(Element[] list, Element a) {
        Element newlist[];
        double i;

        newlist = new Element[(int) (list.length + 1d)];

        for (i = 0; i < list.length; i = i + 1d) {
            newlist[(int) i] = list[(int) i];
        }
        newlist[list.length] = a;

        delete(list);

        return newlist;
    }

    public static void AddElementRef(ElementArrayReference list, Element i) {
        list.array = AddElement(list.array, i);
    }

    public static Element[] RemoveElement(Element[] list, double n) {
        Element newlist[];
        double i;

        newlist = new Element[(int) (list.length - 1d)];

        for (i = 0; i < list.length; i = i + 1d) {
            if (i < n) {
                newlist[(int) i] = list[(int) i];
            }
            if (i > n) {
                newlist[(int) (i - 1d)] = list[(int) i];
            }
        }

        delete(list);

        return newlist;
    }

    public static Element GetElementRef(ElementArrayReference list, double i) {
        return list.array[(int) i];
    }

    public static void RemoveElementRef(ElementArrayReference list, double i) {
        list.array = RemoveElement(list.array, i);
    }

    public static void delete(Object object) {
        // Java has garbage collection.
    }
}