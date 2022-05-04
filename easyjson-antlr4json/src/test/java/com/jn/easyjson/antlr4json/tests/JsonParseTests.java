package com.jn.easyjson.antlr4json.tests;

import com.jn.easyjson.antlr4json.Antlr4Jsons;
import com.jn.easyjson.core.JsonTreeNode;
import org.junit.Test;

public class JsonParseTests {
    @Test
    public void parseSingleLine() {
        JsonTreeNode node = Antlr4Jsons.parse("{\"a\":12, \"b\":[12, 23, \"hello world\", [\"k1\", 1003]]}");
        System.out.println(node);
    }
}
