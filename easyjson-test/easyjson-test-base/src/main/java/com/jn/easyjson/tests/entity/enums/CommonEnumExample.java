package com.jn.easyjson.tests.entity.enums;

import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

public enum CommonEnumExample implements CommonEnum {
    EXAMPLE_1(1, "example1", "001"),
    EXAMPLE_2(2, "example2", "002"),
    EXAMPLE_3(3, "example3", "003"),
    EXAMPLE_4(4, "example4", "004")
    ;
    private EnumDelegate delegate;

    CommonEnumExample(int code, String name, String displayText) {
        this.delegate = new EnumDelegate(code, name, displayText);
    }

    @Override
    public int getCode() {
        return this.delegate.getCode();
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public String getDisplayText() {
        return this.delegate.getDisplayText();
    }
}
