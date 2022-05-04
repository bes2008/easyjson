package com.jn.easyjson.antlr4json;

import com.jn.easyjson.antlr4json.generated.JsonLexer;
import com.jn.easyjson.antlr4json.generated.JsonParser;
import com.jn.easyjson.core.JsonTreeNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class Antlr4Jsons {

    public static JsonTreeNode parse(String json) {
        ANTLRInputStream in = new ANTLRInputStream(json);
        return parse(in);
    }

    public static JsonTreeNode parse(InputStream inputStream) throws IOException {
        ANTLRInputStream in = new ANTLRInputStream(inputStream);
        return parse(in);
    }

    public static JsonTreeNode parse(Reader reader) throws IOException {
        ANTLRInputStream inputStream = new ANTLRInputStream(reader);
        return parse(inputStream);
    }

    public static JsonTreeNode parse(ANTLRInputStream inputStream) {
        JsonLexer lexer = new JsonLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JsonParser parser = new JsonParser(tokens);
        ParseTree textTree = parser.json();

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        Antlr4JsonListenerImpl listener = new Antlr4JsonListenerImpl();
        parseTreeWalker.walk(listener, textTree);
        JsonTreeNode node = listener.getJsonTree();
        return node;
    }
}
