package JSON.structures;

import JSON.StringElementMaps.StringElementMap;

public class Element {
    public ElementType type;

    public StringElementMap object;
    public Element [] array;
    public char [] string;
    public double number;
    public boolean booleanValue;
}
