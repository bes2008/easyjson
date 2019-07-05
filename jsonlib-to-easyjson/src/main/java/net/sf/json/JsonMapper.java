package net.sf.json;

import com.github.fangjinuo.easyjson.core.JSONBuilder;
import com.github.fangjinuo.easyjson.core.JSONBuilderProvider;
import com.github.fangjinuo.easyjson.core.JsonTreeNode;
import com.github.fangjinuo.easyjson.core.node.*;
import com.github.fangjinuo.easyjson.core.util.type.Primitives;
import net.sf.json.util.JSONTokener;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;

public class JsonMapper {

    /**
     * from java object to JSON-lib JSONObject, JSONArray, JSONNull, primitive
     *
     * @param object
     * @return
     */
    public static Object fromJavaObject(Object object) {
        return fromJavaObject(object, null);
    }

    public static Object fromJavaObject(Object object, JsonConfig jsonConfig) {
        if (object == null) {
            return JSONNull.getInstance();
        }

        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }

        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }

        if (Primitives.isPrimitive(object.getClass()) || object instanceof String) {
            return object;
        }

        com.github.fangjinuo.easyjson.core.JSON json = buildJSON(jsonConfig);

        if (object instanceof JSONString) {
            String jsonString = ((JSONString) object).toJSONString();
            JsonTreeNode jsonTreeNode = json.fromJson(jsonString);
            return fromJsonTreeNode(jsonTreeNode);
        }

        if (object instanceof JSON) {
            String jsonString = ((JSON) object).toString(0);
            JsonTreeNode jsonTreeNode = json.fromJson(jsonString);
            return fromJsonTreeNode(jsonTreeNode);
        }

        if (object instanceof JSONTokener) {
            StringBuilder stringBuilder = new StringBuilder();
            JSONTokener tokener = (JSONTokener) object;
            while (tokener.more()) {
                stringBuilder.append(tokener.next());
            }

            JsonTreeNode jsonTreeNode = json.fromJson(stringBuilder.toString());
            return fromJavaObject(jsonTreeNode);
        }

        JsonTreeNode jsonTreeNode = json.fromJson(json.toJson(object));
        return fromJsonTreeNode(jsonTreeNode);
    }

    public static com.github.fangjinuo.easyjson.core.JSON buildJSON(JsonConfig config) {
        if (config != null) {
            JSONBuilder jsonBuilder = JSONBuilderProvider.create();

            if (config.isIgnoreTransientFields() || config.isIgnoreJPATransient()) {
                jsonBuilder.excludeFieldsWithAppendModifiers(Modifier.TRANSIENT);
            }
            return jsonBuilder.serializeNulls(true).build();
        } else {
            return JSONBuilderProvider.simplest();
        }
    }


    /**
     * from json tree node to JSON-lib JSON object
     *
     * @param treeNode
     * @return
     */
    public static Object fromJsonTreeNode(JsonTreeNode treeNode) {
        if (treeNode == null) {
            return JSONNull.getInstance();
        }

        if (treeNode.isJsonNullNode()) {
            return JSONNull.getInstance();
        }

        if (treeNode.isJsonPrimitiveNode()) {
            return treeNode.getAsJsonPrimitiveNode().getValue();
        }

        if (treeNode.isJsonArrayNode()) {
            JSONArray jsonArray = new JSONArray();
            JsonArrayNode arrayNode = treeNode.getAsJsonArrayNode();
            for (JsonTreeNode jsonTreeNode : arrayNode) {
                jsonArray.element(fromJsonTreeNode(jsonTreeNode));
            }
            return jsonArray;
        }

        if (treeNode.isJsonObjectNode()) {
            JSONObject jsonObject = new JSONObject();
            JsonObjectNode objectNode = treeNode.getAsJsonObjectNode();
            for (Map.Entry<String, JsonTreeNode> entry : objectNode.propertySet()) {
                jsonObject.put(entry.getKey(), fromJsonTreeNode(entry.getValue()));
            }
            return jsonObject;
        }
        return JSONNull.getInstance();
    }

    /**
     * from JSONObject, JSONArray, JSONNull, primitive to Java Object
     *
     * @param jsonObj
     * @return
     */
    public static Object toJavaObject(Object jsonObj) {
        return JsonTreeNodes.toJavaObject(toJsonTreeNode(jsonObj, null));
    }

    public static Object toJavaObject(Object jsonObj, JsonConfig jsonConfig) {
        return JsonTreeNodes.toJavaObject(toJsonTreeNode(jsonObj, jsonConfig));
    }

    public static JsonTreeNode toJsonTreeNode(Object jsonObj) {
        return toJsonTreeNode(jsonObj, null);
    }

    public static JsonTreeNode toJsonTreeNode(Object jsonObj, JsonConfig jsonConfig) {
        if (jsonObj == null || jsonObj == JSONNull.getInstance() || jsonObj instanceof JSONNull) {
            return JsonNullNode.INSTANCE;
        }
        if (Primitives.isPrimitive(jsonObj.getClass())) {
            return new JsonPrimitiveNode(jsonObj);
        }

        if (jsonObj instanceof JSONArray) {
            JsonArrayNode arrayNode = new JsonArrayNode();
            JSONArray jsonArray = (JSONArray) jsonObj;
            for (Object object : jsonArray) {
                arrayNode.add(toJsonTreeNode(object, jsonConfig));
            }
            return arrayNode;
        }

        if (jsonObj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) jsonObj;
            JsonObjectNode objectNode = new JsonObjectNode();
            Iterator<Map.Entry<Object, Object>> iter = jsonObject.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Object, Object> entry = iter.next();
                objectNode.addProperty(entry.getKey().toString(), toJsonTreeNode(entry.getValue(), jsonConfig));
            }
            return objectNode;
        }


        com.github.fangjinuo.easyjson.core.JSON json = buildJSON(jsonConfig);

        if (jsonObj instanceof JSONString) {
            String jsonString = ((JSONString) jsonObj).toJSONString();
            return json.fromJson(jsonString);
        }

        if (jsonObj instanceof JSON) {
            String jsonString = ((JSON) jsonObj).toString(0);
            return json.fromJson(jsonString);
        }

        if (jsonObj instanceof JSONTokener) {
            StringBuilder stringBuilder = new StringBuilder();
            JSONTokener tokener = (JSONTokener) jsonObj;
            while (tokener.more()) {
                stringBuilder.append(tokener.next());
            }

            return json.fromJson(stringBuilder.toString());
        }

        return JsonTreeNodes.fromJavaObject(jsonObj);
    }

}
