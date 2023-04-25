package com.jn.easyjson.supports.jsonpath;

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.spi.json.AbstractJsonProvider;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.JsonArrayNode;
import com.jn.easyjson.core.node.JsonObjectNode;
import com.jn.easyjson.core.util.JSONs;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

public class EasyjsonProvider extends AbstractJsonProvider {
    @Override
    public boolean isArray(Object obj) {
        return super.isArray(obj) || obj instanceof JsonArrayNode;
    }

    @Override
    public boolean isMap(Object obj) {
        return super.isMap(obj) || obj instanceof JsonObjectNode;
    }

    @Override
    public Object getArrayIndex(Object array, int idx) {
        if (array instanceof JsonArrayNode) {
            return ((JsonArrayNode) array).get(idx);
        } else {
            return super.getArrayIndex(array, idx);
        }
    }

    @Override
    public void setArrayIndex(Object array, int index, Object newValue) {
        if (array instanceof JsonArrayNode) {
            JsonTreeNode newTreeNode = JSONs.toJsonTreeNode(newValue);
            ((JsonArrayNode) array).set(index, newTreeNode);
        } else {
            super.setArrayIndex(array, index, newValue);
        }
    }

    @Override
    public Collection<String> getPropertyKeys(Object obj) {
        if (obj instanceof JsonObjectNode) {
            return ((JsonObjectNode) obj).propertyNames();
        } else {
            return super.getPropertyKeys(obj);
        }
    }

    @Override
    public Object getMapValue(Object obj, String key) {
        if (obj instanceof JsonObjectNode) {
            return ((JsonObjectNode) obj).getProperty(key);
        } else {
            return super.getMapValue(obj, key);
        }
    }

    @Override
    public void setProperty(Object obj, Object key, Object value) {
        if (obj instanceof JsonObjectNode) {
            JsonTreeNode newTreeNode = JSONs.toJsonTreeNode(value);
            ((JsonObjectNode) obj).addProperty(key.toString(), newTreeNode);
        } else {
            super.setProperty(obj, key, value);
        }
    }

    @Override
    public void removeProperty(Object obj, Object key) {
        if (obj instanceof JsonObjectNode) {
            ((JsonObjectNode) obj).remove(key.toString());
        } else if (obj instanceof JsonArrayNode && key instanceof Integer) {
            JsonArrayNode arrayNode = (JsonArrayNode) obj;
            int index = Integer.parseInt(key.toString());
            if (index >= 0 && index < arrayNode.size()) {
                arrayNode.remove(index);
            }
        }
    }

    @Override
    public int length(Object obj) {
        if (obj instanceof JsonObjectNode) {
            return ((JsonObjectNode) obj).size();
        }
        if (obj instanceof JsonArrayNode) {
            return ((JsonArrayNode) obj).size();
        }
        if (obj instanceof String) {
            return ((String) obj).length();
        }
        throw new JsonPathException("length operation cannot be applied to " + (obj != null ? obj.getClass().getName() : "null"));
    }

    @Override
    public Object parse(String json) throws InvalidJsonException {
        return JSONs.parse(json);
    }

    @Override
    public Object parse(InputStream jsonStream, String charset) throws InvalidJsonException {
        try {
            return JSONs.parse(new InputStreamReader(jsonStream, charset));
        } catch (IOException e) {
            throw new InvalidJsonException(e);
        }
    }


    @Override
    public String toJson(Object obj) {
        return JSONs.toJson(obj);
    }

    @Override
    public Object createArray() {
        return new JsonArrayNode();
    }

    @Override
    public Object createMap() {
        return new JsonObjectNode();
    }

    @Override
    public Object unwrap(Object obj) {
        if(obj instanceof JsonTreeNode){
            return JSONs.toJavaObject((JsonTreeNode) obj);
        }
        return super.unwrap(obj);
    }
}
