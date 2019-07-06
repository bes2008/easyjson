package com.github.fangjinuo.easyjson.core.node;

import com.github.fangjinuo.easyjson.core.JsonTreeNode;

public interface ToJsonTreeNodeMapper {
    JsonTreeNode mapping(Object object);
}
