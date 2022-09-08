package com.jn.easyjson.jackson.ext;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserDelegate;

import java.io.IOException;

/**
 * 为处理数组中的 , 后面没有值的情况加的
 */
public class EasyjsonJsonParser extends JsonParserDelegate {
    public EasyjsonJsonParser(JsonParser d) {
        super(d);
    }

    @Override
    public JsonToken nextToken() throws IOException {
        try {
            return super.nextToken();
        }catch (ArrayCommaException e){
            JsonToken jsonToken = nextToken();
            return jsonToken;
        }
    }
}
