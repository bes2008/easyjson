package com.github.fangjinuo.easyjson.api.tree;

/**
 * This exception is raised when Gson was unable to read an input stream
 * or write to one.
 *
 */
public final class JsonIOException extends JsonParseException {
    private static final long serialVersionUID = 1L;

    public JsonIOException(String msg) {
        super(msg);
    }

    public JsonIOException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Creates exception with the specified cause. Consider using
     * {@link #JsonIOException(String, Throwable)} instead if you can describe what happened.
     *
     * @param cause root exception that caused this exception to be thrown.
     */
    public JsonIOException(Throwable cause) {
        super(cause);
    }
}
