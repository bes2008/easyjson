package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.JsonObjectNode;
import com.jn.easyjson.core.util.JSONs;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.Charsets;
import org.testng.annotations.Test;


public class EasyjsonNestedJsonStringTests {
    @Test
    public void test001() {
        Resource res = Resources.loadClassPathResource("00DE8E3E-95E3-4885-A760-A269A6836AE2.txt", EasyjsonNestedJsonStringTests.class );
        Resources.readLines(res, Charsets.UTF_8, new Consumer<String>() {
            @Override
            public void accept(String line) {
                if(Strings.isNotBlank(line)){
                    JsonTreeNode node = JSONs.toJsonTreeNode(line);
                    if(node instanceof JsonObjectNode) {
                        System.out.println(((JsonObjectNode)node).getProperty("message").toString());
                        String json = node.toString();
                        System.out.println(JSONs.parse(json));
                    }
                }
            }
        });

    }
}
