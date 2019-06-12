package com.github.fangjinuo.easyjson.fastjson.codec;

import java.lang.reflect.Type;
import java.util.List;

public interface Typed {
    List<Type> applyTo();
}
