package com.jn.easyjson.jackson.ext;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.io.IOContext;

import java.io.IOException;
import java.io.Reader;

/**
 * 为处理数组中的 , 后面没有值的情况加的
 */
public class EasyjsonJsonFactory extends JsonFactory {

    @Override
    protected JsonParser _createParser(Reader r, IOContext ctxt) throws IOException {
        return new EasyjsonJsonParser(new EasyjsonReaderBasedJsonParser(ctxt, _parserFeatures, r, _objectCodec,
                _rootCharSymbols.makeChild(_factoryFeatures)));
    }

    @Override
    protected JsonParser _createParser(char[] data, int offset, int len, IOContext ctxt, boolean recyclable) throws IOException {
        return new EasyjsonJsonParser(new EasyjsonReaderBasedJsonParser(ctxt, _parserFeatures, null, _objectCodec,
                _rootCharSymbols.makeChild(_factoryFeatures),
                data, offset, offset+len, recyclable));
    }

}
