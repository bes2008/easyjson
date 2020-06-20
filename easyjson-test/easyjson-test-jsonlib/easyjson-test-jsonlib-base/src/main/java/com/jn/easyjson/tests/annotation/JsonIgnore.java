package com.jn.easyjson.tests.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** 忽略字段的注解 **/
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonIgnore {
}
