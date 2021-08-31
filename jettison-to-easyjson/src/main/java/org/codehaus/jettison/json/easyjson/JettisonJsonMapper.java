package org.codehaus.jettison.json.easyjson;

import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.*;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONString;

import java.util.Iterator;

public class JettisonJsonMapper {

    private static class JettisonToJsonTreeNodeMapper implements ToJsonTreeNodeMapper {
        @Override
        public JsonTreeNode mapping(Object object) {
            if (object instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) object;
                JsonArrayNode jsonArrayNode = new JsonArrayNode(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    Object element = jsonArray.opt(i);
                    JsonTreeNode elementNode = JsonTreeNodes.toJsonTreeNode(element, this);
                    jsonArrayNode.add(elementNode);
                }
                return jsonArrayNode;
            } else if (object instanceof JSONObject) {
                final JSONObject jsonObject = (JSONObject) object;
                Iterator<String> keys = jsonObject.keys();

                final JsonObjectNode objectNode = new JsonObjectNode();
                final ToJsonTreeNodeMapper mapper = this;
                Collects.forEach(keys, new Consumer2<Integer, String>() {
                    @Override
                    public void accept(Integer index, String key) {
                        Object value = jsonObject.opt(key);
                        JsonTreeNode valueNode = JsonTreeNodes.toJsonTreeNode(value, mapper);
                        objectNode.addProperty(key, valueNode);
                    }
                });
                return objectNode;
            } else if (object instanceof JSONString) {
                JSONString jsonString = (JSONString) object;
                String json = jsonString.toJSONString();
                JsonTreeNode jsonTreeNode = JSONBuilderProvider.simplest().fromJson(json);
                return jsonTreeNode;
            }
            return null;
        }

        @Override
        public boolean isAcceptable(Object object) {
            return object instanceof JSONArray || object instanceof JSONObject || object instanceof JSONString;
        }
    }

    public static JsonTreeNode toJsonTreeNode(Object object) {
        return JsonTreeNodes.toJsonTreeNode(object, new JettisonToJsonTreeNodeMapper());
    }

}
