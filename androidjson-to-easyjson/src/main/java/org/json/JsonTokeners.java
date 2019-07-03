package org.json;

public class JsonTokeners {
    public static String readToString(JSONTokener jsonTokener){
        if(jsonTokener==null){
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(255);
        while (jsonTokener.more()){
            stringBuilder.append(jsonTokener.next());
        }
        return stringBuilder.toString();
    }
}
