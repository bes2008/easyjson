package com.jn.easyjson.core.bean.propertynaming;

import java.lang.reflect.Member;

public class AnnotatedNamingPolicy implements BeanPropertyNamingPolicy {
    @Override
    public String translateName(Member member, String fieldName) {
        return null;
    }

    @Override
    public String getName() {
        return "annotated";
    }
}
