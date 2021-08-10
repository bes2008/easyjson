package com.jn.easyjson.javax.json;

import com.jn.easyjson.core.JsonException;
import com.jn.easyjson.core.JsonHandlerAdapter;
import com.jn.easyjson.core.JsonTreeNode;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;

public class JavaxJsonHandler extends JsonHandlerAdapter {
    @Override
    public JsonTreeNode deserialize(String json) throws JsonException {
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonValue jsonValue = jsonReader.readValue();
        return JsonTreeNodeMapper.toJsonTreeNode(jsonValue);
    }

    @Override
    public Object deserialize(String json, Type typeOfT) throws JsonException {
        return null;
    }

    @Override
    public Object deserialize(Reader reader, Type typeOfT) throws JsonException {
        return null;
    }

    @Override
    public String serialize(Object src, Type typeOfT) throws JsonException {
        return null;
    }
}
