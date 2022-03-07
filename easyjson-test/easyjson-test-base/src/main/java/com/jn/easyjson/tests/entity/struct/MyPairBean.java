package com.jn.easyjson.tests.entity.struct;

import com.jn.langx.util.struct.pair.NameValuePair;

import java.util.Set;

public class MyPairBean {
    private Set<NameValuePair> pairSet;
    private String id;

    public MyPairBean(){

    }

    public Set<NameValuePair> getPairSet() {
        return pairSet;
    }

    public void setPairSet(Set<NameValuePair> pairSet) {
        this.pairSet = pairSet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
