package com.github.fangjinuo.easyjson.api.node;

import com.github.fangjinuo.easyjson.api.JsonTreeNode;

public class JsonNullNode extends JsonTreeNode {
    /**
     * singleton for JsonNullNode
     *
     * @since 1.8
     */
    public static final JsonNullNode INSTANCE = new JsonNullNode();

    /**
     * Creates a new JsonNullNode object.
     * Deprecated since Gson version 1.8. Use {@link #INSTANCE} instead
     */
    @Deprecated
    public JsonNullNode() {
        // Do nothing
    }

    /**
     * Returns the same instance since it is an immutable value
     * @since 2.8.2
     */
    @Override
    public JsonNullNode deepCopy() {
        return INSTANCE;
    }

    /**
     * All instances of JsonNullNode have the same hash code since they are indistinguishable
     */
    @Override
    public int hashCode() {
        return JsonNullNode.class.hashCode();
    }

    /**
     * All instances of JsonNullNode are the same
     */
    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof JsonNullNode;
    }
}