package com.jn.easyjson.jackson.ext;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;
import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;

import java.io.Reader;
import java.util.Collection;

/**
 * 为处理数组中的 , 后面没有值的情况加的
 */
public class EasyjsonReaderBasedJsonParser extends ReaderBasedJsonParser {
    public EasyjsonReaderBasedJsonParser(IOContext ctxt, int features, Reader r,
                                         ObjectCodec codec, CharsToNameCanonicalizer st,
                                         char[] inputBuffer, int start, int end,
                                         boolean bufferRecyclable) {
        super(ctxt, features, r, codec, st, inputBuffer, start, end, bufferRecyclable);
    }

    public EasyjsonReaderBasedJsonParser(IOContext ctxt, int features, Reader r,
                                         ObjectCodec codec, CharsToNameCanonicalizer st) {
        super(ctxt, features, r, codec, st);
    }

    @Override
    protected void _reportUnexpectedChar(int ch, String comment) throws JsonParseException {
        // 如果是数组
        if ((ch == ']' || ch == ',') && this._parsingContext.getCurrentValue() instanceof Collection) {
            String msg = "Unexpected character (" + _getCharDesc(ch) + ")";
            if (comment != null) {
                msg += ": " + comment;
            }
            JsonLocation location = getCurrentLocation();
            _inputPtr = _inputPtr+1;
            throw new ArrayCommaException(msg, location);
            // ignore it
        } else {
            super._reportUnexpectedChar(ch, comment);
        }
    }
}
