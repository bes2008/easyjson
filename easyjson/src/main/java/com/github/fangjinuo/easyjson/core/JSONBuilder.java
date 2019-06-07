package com.github.fangjinuo.easyjson.core;

public abstract class JSONBuilder {
    protected boolean serializeNulls = false;
    // enum priority: value() > toString() > name()
    protected boolean serializeEnumUsingToString = false; // default using name()
    protected boolean serializeEnumUsingValue = false;
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

    public JSONBuilder serializeEnumUsingValue() {
        this.serializeEnumUsingValue = true;
        return this;
    }

    public JSONBuilder serializeLongAsString() {
        this.serializeLongAsString = true;
        return this;
    }

    public JSONBuilder serializeDatePattern(String datePattern) {
        this.serializeDatePattern = datePattern;
        return this;
    }

    public abstract JSON build();
}
