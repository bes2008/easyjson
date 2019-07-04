package net.sf.json;

import com.github.fangjinuo.easyjson.core.util.type.Primitives;
import net.sf.json.util.JSONTokener;

import java.util.Collection;

public class JsonMapper {
    public static JSON fromJavaCollection(Collection collection){
        JSONArray jsonArray = new JSONArray();
        if(collection==null){
            return jsonArray;
        }
        for(Object item : collection){
            if(item==null){
                jsonArray.element(JSONNull.getInstance());
                continue;
            }
            if(Primitives.isPrimitive(item.getClass()) || item instanceof String){
                jsonArray.element(item);
                continue;
            }

        }
    }
}
