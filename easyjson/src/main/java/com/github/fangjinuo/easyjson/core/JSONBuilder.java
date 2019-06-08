package com.github.fangjinuo.easyjson.core;

public abstract class JSONBuilder {
    // null
    protected boolean serializeNulls = false;

    // enum priority: ordinal() > toString() > field > name()
    protected boolean serializeEnumUsingToString = false; // default using name()
    protected boolean serializeEnumUsingValue = false;
    protected String serializeEnumUsingField = null;

    // date priority: pattern > toString() > timestamp []
    protected String serializeDateUsingPattern = null;// default : using timestamp
    protected boolean serializeDateUsingToString=false;

    // number priority: longAsString > numberAsString > number
    protected boolean serializeLongAsString = false;
    protected boolean serializeNumberAsString = false;

    // print format
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
    public JSONBuilder serializeEnumUsingField(String field){
        if(field!=null && !field.trim().isEmpty()) {
            this.serializeEnumUsingField = field;
        }
        return this;
    }

    public JSONBuilder serializeLongAsString() {
        this.serializeLongAsString = true;
        return this;
    }

    public JSONBuilder serializeNumberAsString(){
        this.serializeNumberAsString = true;
        return this;
    }

    public JSONBuilder serializeDateUsingPattern(String datePattern) {
        this.serializeDateUsingPattern = datePattern;
        return this;
    }

    public JSONBuilder serializeDateUsingToString() {
        this.serializeDateUsingToString = true;
        return this;
    }

    public abstract JSON build();
}
