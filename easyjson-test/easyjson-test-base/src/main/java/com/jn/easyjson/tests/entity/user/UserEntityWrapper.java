package com.jn.easyjson.tests.entity.user;

import java.util.List;

public class UserEntityWrapper {
    private List<UserEntity> data;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserEntity> getData() {
        return data;
    }

    public void setData(List<UserEntity> data) {
        this.data = data;
    }
}
