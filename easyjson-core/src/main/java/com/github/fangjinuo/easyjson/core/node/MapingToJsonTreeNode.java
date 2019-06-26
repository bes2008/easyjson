package com.github.fangjinuo.easyjson.core.node;

import com.github.fangjinuo.easyjson.core.JsonTreeNode;

public interface MapingToJsonTreeNode {
    JsonTreeNode mapping(Object object);
}
