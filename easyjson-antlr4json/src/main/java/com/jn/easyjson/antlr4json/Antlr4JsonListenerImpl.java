package com.jn.easyjson.antlr4json;

import com.jn.easyjson.antlr4json.generated.JsonListener;
import com.jn.easyjson.antlr4json.generated.JsonParser;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.JsonArrayNode;
import com.jn.easyjson.core.node.JsonNullNode;
import com.jn.easyjson.core.node.JsonObjectNode;
import com.jn.easyjson.core.node.JsonPrimitiveNode;
import com.jn.easyjson.core.tree.JsonParseException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.collection.stack.SimpleStack;
import com.jn.langx.util.collection.stack.Stack;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Antlr4JsonListenerImpl implements JsonListener {
    private Stack<JsonTreeNode> contextNodeStack;

    public Antlr4JsonListenerImpl() {
        this(new SimpleStack<JsonTreeNode>());
    }

    public Antlr4JsonListenerImpl(Stack<JsonTreeNode> stack) {
        this.contextNodeStack = stack;
    }

    public JsonTreeNode getJsonTree() {
        return contextNodeStack.peek();
    }

    @Override
    public void enterValue(JsonParser.ValueContext ctx) {
    }

    @Override
    public void exitValue(JsonParser.ValueContext ctx) {
        ParserRuleContext parentRuleContext = ctx.getParent();
        if (parentRuleContext != null) {
            if (parentRuleContext instanceof JsonParser.ArrayContext) {
                JsonTreeNode treeNode = contextNodeStack.pop();
                JsonArrayNode arrayNode = (JsonArrayNode) contextNodeStack.peek();
                arrayNode.add(treeNode);
            }
        }
    }

    @Override
    public void enterPair(JsonParser.PairContext ctx) {
    }

    @Override
    public void exitPair(JsonParser.PairContext ctx) {
        String key = ctx.STRING().getText();
        key = key.substring(1);
        key = key.substring(0, key.length() - 1);
        JsonTreeNode value = contextNodeStack.pop();
        JsonObjectNode objectNode = (JsonObjectNode) contextNodeStack.peek();
        objectNode.addProperty(key, value);
    }

    @Override
    public void enterObject(JsonParser.ObjectContext ctx) {
        JsonObjectNode obj = new JsonObjectNode();

        ParserRuleContext parentContext = ctx.getParent();
        if (parentContext instanceof JsonParser.ArrayContext) {
            if (!contextNodeStack.isEmpty()) {
                JsonTreeNode parent = contextNodeStack.peek();
                if (parent instanceof JsonArrayNode) {
                    ((JsonArrayNode) parent).add(obj);
                }
            }
        }

        contextNodeStack.push(obj);
    }

    @Override
    public void exitObject(JsonParser.ObjectContext ctx) {
        ParserRuleContext parentContext = ctx.getParent();
        if (parentContext instanceof JsonParser.ArrayContext) {
            if (!contextNodeStack.isEmpty()) {
                contextNodeStack.pop();
            }
        }
    }

    @Override
    public void enterArray(JsonParser.ArrayContext ctx) {
        JsonArrayNode array = new JsonArrayNode();

        ParserRuleContext parentContext = ctx.getParent();
        if (parentContext instanceof JsonParser.ArrayContext) {
            if (!contextNodeStack.isEmpty()) {
                contextNodeStack.pop();
                JsonTreeNode parent = contextNodeStack.peek();
                if (parent instanceof JsonArrayNode) {
                    ((JsonArrayNode) parent).add(array);
                }
            }
        }

        contextNodeStack.push(array);
    }

    @Override
    public void exitArray(JsonParser.ArrayContext ctx) {
        ParserRuleContext parentContext = ctx.getParent();
        if (parentContext instanceof JsonParser.ArrayContext) {
            if (!contextNodeStack.isEmpty()) {
                contextNodeStack.pop();
            }
        }
    }

    @Override
    public void enterJson(JsonParser.JsonContext ctx) {
    }

    @Override
    public void exitJson(JsonParser.JsonContext ctx) {
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        ParseTree parentContext = node.getParent();
        if (parentContext instanceof JsonParser.ValueContext) {
            Token token = node.getSymbol();
            String str = token.getText();
            int tokenType = token.getType();

            JsonTreeNode treeNode = null;
            switch (tokenType) {
                case JsonParser.NULL:
                    treeNode = JsonNullNode.INSTANCE;
                    break;
                case JsonParser.NUMBER:
                    Number number = Numbers.createNumber(str);
                    treeNode = new JsonPrimitiveNode(number);
                    break;
                case JsonParser.BOOL:
                    Boolean b = Boolean.parseBoolean(str);
                    treeNode = new JsonPrimitiveNode(b);
                    break;
                case JsonParser.STRING:
                    str = str.substring(1, str.length()-1);
                    treeNode = new JsonPrimitiveNode(str);
                    break;
                case JsonParser.WS:
                case JsonParser.COMMENT:
                case JsonParser.OBJ_START:
                case JsonParser.OBJ_END:
                case JsonParser.PAIR_SEPAR:
                case JsonParser.ARRAY_START:
                case JsonParser.ARRAY_END:
                case JsonParser.ARRAY_SEPAR:
                default:
                    break;

            }

            if (treeNode != null) {
                contextNodeStack.push(treeNode);
            }
        }
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        throw new JsonParseException(StringTemplates.formatWithPlaceholder("invalid json :{}", node.getText()));
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
    }
}
