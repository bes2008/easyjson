package com.jn.easyjson.javax.json;

import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.JsonTreeNodes;
import com.jn.easyjson.core.node.ToJsonTreeNodeMapper;

public class JsonTreeNodeMapper {

    public static JsonTreeNode toJsonTreeNode(Object object) {
        return JsonTreeNodes.fromJavaObject(object, new ToJsonTreeNodeMapper() {
            @Override
            public JsonTreeNode mapping(Object object) {
                return null;
            }

            @Override
            public boolean isAcceptable(Object object) {
                return false;
            }
        });
    }

}
