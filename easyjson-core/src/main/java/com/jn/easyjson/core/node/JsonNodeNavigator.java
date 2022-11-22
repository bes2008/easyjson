package com.jn.easyjson.core.node;

import com.jn.easyjson.core.JsonTreeNode;
import com.jn.langx.navigation.Navigator;
import com.jn.langx.navigation.Navigators;
import com.jn.langx.navigation.object.ObjectNavigator;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.util.List;

public class JsonNodeNavigator implements Navigator<JsonTreeNode> {
    private static final Logger logger = Loggers.getLogger(ObjectNavigator.class);
    private String prefix;
    private String suffix;

    public JsonNodeNavigator() {
        this("/");
    }

    public JsonNodeNavigator(String separator) {
        this(null, separator);
    }

    public JsonNodeNavigator(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = Strings.isEmpty(suffix) ? "/" : suffix;
    }

    @Override
    public JsonTreeNode get(JsonTreeNode context, String pathExpression) {
        if (context == null) {
            return null;
        }

        String[] segments = Navigators.getPathSegments(pathExpression, this.prefix, this.suffix);
        return navigate(context, Collects.asList(segments));
    }

    @Override
    public List<JsonTreeNode> getList(JsonTreeNode context, String expression) {
        return Pipeline.<JsonTreeNode>of(get(context, expression)).asList();
    }

    private JsonTreeNode navigate(JsonTreeNode context, List<String> segments) {
        JsonTreeNode node = context;
        try {
            for (int i = 0; node != null && i < segments.size(); i++) {
                String property = segments.get(i);
                if (node instanceof JsonObjectNode) {
                    JsonObjectNode objectNode = (JsonObjectNode) node;
                    node = objectNode.getProperty(property);
                } else if (node instanceof JsonArrayNode) {
                    JsonArrayNode arrayNode = (JsonArrayNode) node;
                    int index = Numbers.createInteger(property);
                    node = arrayNode.get(index);
                } else {
                    logger.warn("the node which at the path {} is not a object node or array node", Strings.iterateJoin("", this.prefix, this.suffix, segments.subList(0, i + 1)));
                    node = null;
                    break;
                }
            }
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
        return node;
    }

    @Override
    public <E> void set(JsonTreeNode context, String expression, E value) {
        String[] segments = Navigators.getPathSegments(expression, this.prefix, this.suffix);
        if (Objs.isEmpty(segments)) {
            return;
        }
        String property = segments[segments.length - 1];
        List<String> parentExprSegments = Collects.asList(segments).subList(0, segments.length - 1);

        JsonTreeNode parent = navigate(context, parentExprSegments);

        try {
            if (parent instanceof JsonObjectNode) {
                JsonObjectNode objectNode = (JsonObjectNode) parent;
                objectNode.addProperty(property, JsonTreeNodes.toJsonTreeNode(value));
            } else if (parent instanceof JsonArrayNode) {
                JsonArrayNode arrayNode = (JsonArrayNode) parent;
                int index = Numbers.createInteger(property);
                arrayNode.set(index, JsonTreeNodes.toJsonTreeNode(value));
            } else {
                logger.warn("the node which at the path {} is not a object node or array node", Strings.iterateJoin("", this.prefix, this.suffix, parentExprSegments));
            }
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    @Override
    public <E> Class<E> getType(JsonTreeNode context, String expression) {
        JsonTreeNode node = get(context, expression);

        Class c = null;

        if (node != null) {
            c = node.getClass();
        } else {
            String parentExpr = getParentPath(expression);
            if (Strings.isNotEmpty(parentExpr)) {
                JsonTreeNode parent = get(context, parentExpr);
                if (parent != null) {
                    if (parent.getClass() == JsonNullNode.class || parent.getClass() == JsonPrimitiveNode.class) {
                        return null;
                    }
                    String leaf = getLeaf(expression);

                    if (parent instanceof JsonObjectNode) {
                        JsonObjectNode objectNode = (JsonObjectNode) node;
                        node = objectNode.getProperty(leaf);
                    } else if (parent instanceof JsonArrayNode) {
                        JsonArrayNode arrayNode = (JsonArrayNode) node;
                        int index = Numbers.createInteger(leaf);
                        node = arrayNode.get(index);
                    }

                    if (node != null) {
                        c = node.getClass();
                    }
                }
            }
        }
        return c;
    }

    @Override
    public String getParentPath(String s) {
        return Navigators.getParentPath(s, this.prefix, this.suffix);
    }

    @Override
    public String getLeaf(String s) {
        return Navigators.getLeaf(s, this.prefix, this.suffix);
    }

    public static <E extends JsonTreeNode> E getTreeNode(JsonTreeNode tree, String path) {
        return getTreeNode(null, tree, path);
    }

    public static <E extends JsonTreeNode> E getTreeNode(String separator, JsonTreeNode tree, String path) {
        return (E) new JsonNodeNavigator(separator).get(tree, path);
    }

    public static Integer getTreeNodeAsInteger(JsonTreeNode tree, String path) {
        return getTreeNodeAsInteger(null, tree, path);
    }

    public static Integer getTreeNodeAsInteger(String separator, JsonTreeNode tree, String path) {
        JsonTreeNode treeNode = new JsonNodeNavigator(separator).get(tree, path);
        if (treeNode != null) {
            return treeNode.getAsInt();
        }
        return null;
    }

    public static Long getTreeNodeAsLong(JsonTreeNode tree, String path) {
        return getTreeNodeAsLong(null, tree, path);
    }

    public static Long getTreeNodeAsLong(String separator, JsonTreeNode tree, String path) {
        JsonTreeNode treeNode = new JsonNodeNavigator(separator).get(tree, path);
        if (treeNode != null) {
            return treeNode.getAsLong();
        }
        return null;
    }

    public static Float getTreeNodeAsFloat(JsonTreeNode tree, String path) {
        return getTreeNodeAsFloat(null, tree, path);
    }

    public static Float getTreeNodeAsFloat(String separator, JsonTreeNode tree, String path) {
        JsonTreeNode treeNode = new JsonNodeNavigator(separator).get(tree, path);
        if (treeNode != null) {
            return treeNode.getAsFloat();
        }
        return null;
    }

    public static Double getTreeNodeAsDouble(JsonTreeNode tree, String path) {
        return getTreeNodeAsDouble(null, tree, path);
    }

    public static Double getTreeNodeAsDouble(String separator, JsonTreeNode tree, String path) {
        JsonTreeNode treeNode = new JsonNodeNavigator(separator).get(tree, path);
        if (treeNode != null) {
            return treeNode.getAsDouble();
        }
        return null;
    }

    public static String getTreeNodeAsString(JsonTreeNode tree, String path) {
        return getTreeNodeAsString(null, tree, path);
    }

    public static String getTreeNodeAsString(String separator, JsonTreeNode tree, String path) {
        JsonTreeNode treeNode = new JsonNodeNavigator(separator).get(tree, path);
        if (treeNode != null) {
            return treeNode.getAsString();
        }
        return null;
    }

    public static Boolean getTreeNodeAsBoolean(JsonTreeNode tree, String path) {
        return getTreeNodeAsBoolean(null, tree, path);
    }

    public static Boolean getTreeNodeAsBoolean(String separator, JsonTreeNode tree, String path) {
        JsonTreeNode treeNode = new JsonNodeNavigator(separator).get(tree, path);
        if (treeNode != null) {
            return treeNode.getAsBoolean();
        }
        return null;
    }

    public static void setTreeNode(JsonTreeNode tree, String path, Object value) {
        setTreeNode(null, tree, path, value);
    }

    public static void setTreeNode(String separator, JsonTreeNode tree, String path, Object value) {
        new JsonNodeNavigator(separator).set(tree, path, value);
    }
}
