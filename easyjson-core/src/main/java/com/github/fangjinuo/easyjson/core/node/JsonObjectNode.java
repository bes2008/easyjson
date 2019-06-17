/*
 *  Copyright 2019 the original author or authors.
 *
 *  Licensed under the LGPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.github.fangjinuo.easyjson.core.node;

import com.github.fangjinuo.easyjson.core.JsonTreeNode;
import com.github.fangjinuo.easyjson.core.util.LinkedTreeMap;

import java.util.Map;
import java.util.Set;

public class JsonObjectNode extends JsonTreeNode {
    private final LinkedTreeMap<String, JsonTreeNode> members =
            new LinkedTreeMap<String, JsonTreeNode>();

    /**
     * Creates a deep copy of this element and all its children
     * @since 2.8.2
     */
    @Override
    public JsonObjectNode deepCopy() {
        JsonObjectNode result = new JsonObjectNode();
        for (Map.Entry<String, JsonTreeNode> entry : members.entrySet()) {
            result.addProperty(entry.getKey(), entry.getValue().deepCopy());
        }
        return result;
    }

    /**
     * Adds a member, which is a name-value pair, to self. The name must be a String, but the value
     * can be an arbitrary JsonTreeNode, thereby allowing you to build a full tree of JsonTreeNodes
     * rooted at this node.
     *
     * @param property name of the member.
     * @param value the member object.
     */
    public void addProperty(String property, JsonTreeNode value) {
        if (value == null) {
            value = JsonNullNode.INSTANCE;
        }
        members.put(property, value);
    }

    /**
     * Removes the {@code property} from this {@link JsonObjectNode}.
     *
     * @param property name of the member that should be removed.
     * @return the {@link JsonTreeNode} object that is being removed.
     * @since 1.3
     */
    public JsonTreeNode remove(String property) {
        return members.remove(property);
    }

    /**
     * Convenience method to addProperty a primitive member. The specified value is converted to a
     * JsonPrimitiveNode of String.
     *
     * @param property name of the member.
     * @param value the string value associated with the member.
     */
    public void addProperty(String property, String value) {
        addProperty(property, createJsonTreeNode(value));
    }

    /**
     * Convenience method to addProperty a primitive member. The specified value is converted to a
     * JsonPrimitiveNode of Number.
     *
     * @param property name of the member.
     * @param value the number value associated with the member.
     */
    public void addProperty(String property, Number value) {
        addProperty(property, createJsonTreeNode(value));
    }

    /**
     * Convenience method to addProperty a boolean member. The specified value is converted to a
     * JsonPrimitiveNode of Boolean.
     *
     * @param property name of the member.
     * @param value the number value associated with the member.
     */
    public void addProperty(String property, Boolean value) {
        addProperty(property, createJsonTreeNode(value));
    }

    /**
     * Convenience method to addProperty a char member. The specified value is converted to a
     * JsonPrimitiveNode of Character.
     *
     * @param property name of the member.
     * @param value the number value associated with the member.
     */
    public void addProperty(String property, Character value) {
        addProperty(property, createJsonTreeNode(value));
    }

    /**
     * Creates the proper {@link JsonTreeNode} object from the given {@code value} object.
     *
     * @param value the object to generate the {@link JsonTreeNode} for
     * @return a {@link JsonPrimitiveNode} if the {@code value} is not null, otherwise a {@link JsonNullNode}
     */
    private JsonTreeNode createJsonTreeNode(Object value) {
        return value == null ? JsonNullNode.INSTANCE : new JsonPrimitiveNode(value);
    }

    /**
     * Returns a set of members of this object. The set is ordered, and the order is in which the
     * elements were added.
     *
     * @return a set of members of this object.
     */
    public Set<Map.Entry<String, JsonTreeNode>> propertySet() {
        return members.entrySet();
    }

    /**
     * Returns a set of members key values.
     *
     * @return a set of member keys as Strings
     * @since 2.8.1
     */
    public Set<String> propertyNames() {
        return members.keySet();
    }

    /**
     * Returns the number of key/value pairs in the object.
     *
     * @return the number of key/value pairs in the object.
     */
    public int size() {
        return members.size();
    }

    /**
     * Convenience method to check if a member with the specified name is present in this object.
     *
     * @param memberName name of the member that is being checked for presence.
     * @return true if there is a member with the specified name, false otherwise.
     */
    public boolean hasProperty(String memberName) {
        return members.containsKey(memberName);
    }

    /**
     * Returns the member with the specified name.
     *
     * @param memberName name of the member that is being requested.
     * @return the member matching the name. Null if no such member exists.
     */
    public JsonTreeNode getProperty(String memberName) {
        return members.get(memberName);
    }

    /**
     * Convenience method to get the specified member as a JsonPrimitiveNode element.
     *
     * @param memberName name of the member being requested.
     * @return the JsonPrimitiveNode corresponding to the specified member.
     */
    public JsonPrimitiveNode getPropertyAsJsonPrimitiveNode(String memberName) {
        return (JsonPrimitiveNode) members.get(memberName);
    }

    /**
     * Convenience method to get the specified member as a JsonArrayNode.
     *
     * @param memberName name of the member being requested.
     * @return the JsonArrayNode corresponding to the specified member.
     */
    public JsonArrayNode getPropertyAsJsonArrayNode(String memberName) {
        return (JsonArrayNode) members.get(memberName);
    }

    /**
     * Convenience method to get the specified member as a JsonObjectNode.
     *
     * @param memberName name of the member being requested.
     * @return the JsonObjectNode corresponding to the specified member.
     */
    public JsonObjectNode getPropertyAsJsonObjectNode(String memberName) {
        return (JsonObjectNode) members.get(memberName);
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof JsonObjectNode
                && ((JsonObjectNode) o).members.equals(members));
    }

    @Override
    public int hashCode() {
        return members.hashCode();
    }
}
