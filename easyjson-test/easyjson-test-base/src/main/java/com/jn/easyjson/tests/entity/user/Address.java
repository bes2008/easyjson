package com.jn.easyjson.tests.entity.user;

public class Address {

    private String name;
    private String details;
    
    public Address() {
    }
    
    public Address(String name, String details) {
        this.name = name;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
