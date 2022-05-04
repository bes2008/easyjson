package com.jn.easyjson.antlr4json.tests;

import com.jn.easyjson.antlr4json.Antlr4Jsons;
import com.jn.easyjson.core.JsonTreeNode;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.io.IOs;
import org.junit.Test;

import java.io.InputStream;

public class JsonParseTests {
    @Test
    public void parseSingleLine() {
        JsonTreeNode node = Antlr4Jsons.parse("{\"a\":12, \"b\":[12, 23, \"hello world\", [\"k1\", 1003]]}");
        System.out.println(node);
    }

    @Test
    public void parseExample001() {
        InputStream inputStream = null;
        try {
            Resource resource = Resources.loadClassPathResource("/example001.json");
            inputStream = resource.getInputStream();
            JsonTreeNode node = Antlr4Jsons.parse(inputStream);
            System.out.println(node);
        } catch (Throwable ex) {
            IOs.close(inputStream);
        }
    }
}
