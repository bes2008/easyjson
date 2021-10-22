package com.jn.easyjson.jackson.exception;

public class UnsupportedKeyTypeException extends RuntimeException {

    public UnsupportedKeyTypeException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UnsupportedKeyTypeException(String message) {
        super(message);
    }

    public UnsupportedKeyTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedKeyTypeException(Throwable cause) {
        super(cause);
    }

}
