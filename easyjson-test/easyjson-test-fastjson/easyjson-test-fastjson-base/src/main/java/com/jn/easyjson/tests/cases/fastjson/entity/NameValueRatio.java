package com.jn.easyjson.tests.cases.fastjson.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.jn.langx.annotation.Order;

public class NameValueRatio {
    private String name;
    private String value;
    @JSONField(name = "healthRatio")
    private boolean isNeedHealthRatio = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isNeedHealthRatio() {
        return isNeedHealthRatio;
    }

    @Order(3)
    public void setIsNeedHealthRatio(boolean needHealthRatio) {
        this.isNeedHealthRatio = needHealthRatio;
    }

    @Order(5)
    public void setNeedHealthRatio(boolean needHealthRatio) {
        this.isNeedHealthRatio = needHealthRatio;
    }
}
