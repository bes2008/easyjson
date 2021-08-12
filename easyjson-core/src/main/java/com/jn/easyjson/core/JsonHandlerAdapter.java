package com.jn.easyjson.core;

/**
 * @since 3.1.8
 */
public abstract class JsonHandlerAdapter<Delegate> implements JsonHandler {
    private Delegate delegate;

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public Delegate getDelegate() {
        return delegate;
    }
}
