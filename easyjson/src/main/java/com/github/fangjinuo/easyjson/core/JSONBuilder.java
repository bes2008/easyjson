package com.github.fangjinuo.easyjson.core;

public abstract class JSONBuilder {
    protected boolean serializeNulls = false;
    protected boolean serializeEnumUsingToString = false; // default using name()
    protected String serializeDatePattern = null;// default : using timestamp
    protected boolean serializeLongAsString = false;
    protected boolean prettyFormat = false;

    public JSONBuilder() {
    }

    public JSONBuilder serializeNulls() {
        this.serializeNulls = true;
        return this;
    }

    public JSONBuilder prettyFormat() {
        this.prettyFormat = true;
        return this;
    }

    public JSONBuilder serializeEnumUsingToString() {
        this.serializeEnumUsingToString = true;
        return this;
    }

    public JSONBuilder serializeLongAsString(){
        this.serializeLongAsString = true;
        return this;
    }

    public JSONBuilder serializeDatePattern(String datePattern){
        this.serializeDatePattern = datePattern;
        return this;
    }

    public abstract JSON build();
}
