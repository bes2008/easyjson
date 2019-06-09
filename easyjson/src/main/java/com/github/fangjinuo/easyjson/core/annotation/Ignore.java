package com.github.fangjinuo.easyjson.core.annotation;

public @interface Ignore {
    boolean write() default true;

    boolean read() default true;
}
