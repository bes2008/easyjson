package com.jn.easyjson.jackson.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.JsonTreeNodes;

public class JacksonJsonMapper {

    public static JsonTreeNode toJsonTreeNode(Object obj) {
        return JsonTreeNodes.toJsonTreeNode(obj, new JacksonToTreeNodeMapper());
    }

    public static JsonNode fromJsonTreeNode(JsonTreeNode treeNode) {
        return (JsonNode) JsonTreeNodes.fromJsonTreeNode(treeNode, new JacksonToJsonMapper());
    }

}
