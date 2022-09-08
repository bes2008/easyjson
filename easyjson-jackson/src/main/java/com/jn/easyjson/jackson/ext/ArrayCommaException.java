package com.jn.easyjson.jackson.ext;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;

/**
 * 为处理数组中的 , 后面没有值的情况加的
 */
public class ArrayCommaException extends JsonParseException {
    public ArrayCommaException(String msg, JsonLocation loc) {
        super(msg, loc);
    }

    public ArrayCommaException(String msg, JsonLocation loc, Throwable root) {
        super(msg, loc, root);
    }
}
