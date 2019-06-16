package com.github.fangjinuo.easyjson.api.tree;

/**
 * This exception is raised when Gson attempts to read (or write) a malformed
 * JSON element.
 *
 */
public final class JsonSyntaxException extends JsonParseException {

    private static final long serialVersionUID = 1L;

    public JsonSyntaxException(String msg) {
        super(msg);
    }

    public JsonSyntaxException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Creates exception with the specified cause. Consider using
     * {@link #JsonSyntaxException(String, Throwable)} instead if you can
     * describe what actually happened.
     *
     * @param cause root exception that caused this exception to be thrown.
     */
    public JsonSyntaxException(Throwable cause) {
        super(cause);
    }
}
