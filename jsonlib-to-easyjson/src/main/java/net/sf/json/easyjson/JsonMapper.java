package net.sf.json.easyjson;

import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.*;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;
import net.sf.json.*;
import net.sf.json.util.JSONTokener;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonMapper {

    /**
     * from java object to JSON-lib JSONObject, JSONArray, JSONNull, primitive
     */
    public static Object fromJavaObject(Object object) {
        return fromJavaObject(object, null);
    }

    public static Object fromJavaObject(Object object, JsonConfig jsonConfig) {
        return fromJsonTreeNode(toJsonTreeNode(object, jsonConfig));
    }

    public static com.jn.easyjson.core.JSON buildJSON(JsonConfig config) {
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
     */
    public static Object fromJsonTreeNode(JsonTreeNode treeNode) {
        return JsonTreeNodes.fromJsonTreeNode(treeNode, new ToJSONMapper<JSONObject, JSONArray, Object, JSONNull>() {
            @Override
            public JSONNull mappingNull(JsonNullNode node) {
                return JSONNull.getInstance();
            }

            @Override
            public Object mappingPrimitive(JsonPrimitiveNode node) {
                if (node.isBoolean()) {
                    return node.getAsBoolean();
                }
                if (node.isString()) {
                    return node.getAsString();
                }
                if (node.isNumber()) {
                    return node.getAsNumber();
                }
                return node.getValue();
            }

            @Override
            public JSONArray mappingArray(JsonArrayNode arrayNode) {
                JSONArray jsonArray = new JSONArray();
                for (JsonTreeNode jsonTreeNode : arrayNode) {
                    jsonArray.element(JsonTreeNodes.fromJsonTreeNode(jsonTreeNode, this));
                }
                return jsonArray;
            }

            @Override
            public JSONObject mappingObject(JsonObjectNode objectNode) {
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, JsonTreeNode> entry : objectNode.propertySet()) {
                    jsonObject.put(entry.getKey(), JsonTreeNodes.fromJsonTreeNode(entry.getValue(), this));
                }
                return jsonObject;
            }
        });

    }

    /**
     * from JSONObject, JSONArray, JSONNull, primitive to Java Object
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

    private static class JsonTreeNodeMapper implements ToJsonTreeNodeMapper {
        private JsonConfig jsonConfig;

        private JsonTreeNodeMapper(JsonConfig jsonConfig) {
            this.jsonConfig = jsonConfig;
        }
        private static final List<Class> supportedTypes = Collects.<Class>newArrayList(
                JSONNull.class,
                JSONArray.class,
                JSONObject.class,
                JSONString.class,
                JSON.class,
                JSONTokener.class
        );
        @Override
        public boolean isAcceptable(final Object object) {
            return Collects.anyMatch(supportedTypes, new Predicate<Class>() {
                @Override
                public boolean test(Class cls) {
                    return Reflects.isSubClassOrEquals(cls, object.getClass());
                }
            });
        }

        @Override
        public JsonTreeNode mapping(Object jsonObj) {
            if (jsonObj == JSONNull.getInstance() || jsonObj instanceof JSONNull) {
                return JsonNullNode.INSTANCE;
            }

            if (jsonObj instanceof JSONArray) {
                JsonArrayNode arrayNode = new JsonArrayNode();
                JSONArray jsonArray = (JSONArray) jsonObj;
                for (Object item : jsonArray) {
                    arrayNode.add(JsonTreeNodes.toJsonTreeNode(item, this));
                }
                return arrayNode;
            }

            if (jsonObj instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) jsonObj;
                JsonObjectNode objectNode = new JsonObjectNode();
                Iterator<Map.Entry<Object, Object>> iter = jsonObject.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<Object, Object> entry = iter.next();
                    objectNode.addProperty(entry.getKey().toString(), JsonTreeNodes.toJsonTreeNode(entry.getValue(), this));
                }
                return objectNode;
            }


            com.jn.easyjson.core.JSON json = buildJSON(jsonConfig);

            if (jsonObj instanceof JSONString) {
                String jsonString = ((JSONString) jsonObj).toJSONString();
                return json.fromJson(jsonString);
            }

            if (jsonObj instanceof JSON) {
                String jsonString = ((JSON) jsonObj).toString(0);
                return json.fromJson(jsonString);
            }

            if (jsonObj instanceof JSONTokener) {
                JSONTokener tokener = (JSONTokener) jsonObj;
                return json.fromJson(JsonTokeners.readToString(tokener));
            }

            return null;

        }
    }

    public static JsonTreeNode toJsonTreeNode(Object object, JsonConfig jsonConfig) {
        return JsonTreeNodes.toJsonTreeNode(object, new JsonTreeNodeMapper(jsonConfig));
    }

}
