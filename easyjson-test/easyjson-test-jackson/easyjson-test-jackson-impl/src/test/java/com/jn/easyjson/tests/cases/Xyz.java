package com.jn.easyjson.tests.cases;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Xyz {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date createTime;
    private String id;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
