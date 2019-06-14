package com.github.fangjinuo.easyjson.api.node;

import com.github.fangjinuo.easyjson.api.JsonTreeNode;

public interface JsonTreeNodeFactory<JsonNode> {
    JsonTreeNode create(JsonNode node);
}
