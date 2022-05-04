package com.jn.easyjson.antlr4json;

import com.jn.easyjson.core.JsonException;
import com.jn.easyjson.core.JsonHandlerAdapter;
import com.jn.easyjson.core.JsonTreeNode;

import java.io.Reader;
import java.lang.reflect.Type;

public class Antlr4JsonHandlerAdapter extends JsonHandlerAdapter {
    @Override
    public JsonTreeNode deserialize(String json) throws JsonException {
        return Antlr4Jsons.parse(json);
    }

    @Override
    public <T> T deserialize(String json, Type typeOfT) throws JsonException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T deserialize(Reader reader, Type typeOfT) throws JsonException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String serialize(Object src, Type typeOfT) throws JsonException {
        return null;
    }
}
