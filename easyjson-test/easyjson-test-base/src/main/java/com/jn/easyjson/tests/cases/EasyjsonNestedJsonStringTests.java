package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.JsonObjectNode;
import com.jn.easyjson.core.util.JSONs;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.struct.counter.IntegerCounter;
import com.jn.langx.util.struct.counter.SimpleIntegerCounter;
import org.testng.annotations.Test;


public class EasyjsonNestedJsonStringTests {
    @Test
    public void test001() {
        Resource res = Resources.loadClassPathResource("00DE8E3E-95E3-4885-A760-A269A6836AE2.txt", EasyjsonNestedJsonStringTests.class);

        IntegerCounter counter = new SimpleIntegerCounter(0);
        Resources.readLines(res, Charsets.UTF_8, new Consumer<String>() {
            @Override
            public void accept(String line) {
                if (Strings.isNotBlank(line) && counter.get()<200) {
                    counter.increment();
                    if(!line.contains("container-cached.log")) {
                        System.out.println(StringTemplates.formatWithPlaceholder("************line: {}*************", counter.get()));
                        JsonTreeNode node = JSONs.toJsonTreeNode(line);
                        if (node instanceof JsonObjectNode) {
                            JsonTreeNode message = ((JsonObjectNode) node).getProperty("message");
                            if (message.isJsonPrimitiveNode()) {
                                String msg = message.getAsString();
                                JsonTreeNode messageNode = JSONs.parse(msg);
                                System.out.println(StringTemplates.formatWithPlaceholder("not object: {}",messageNode));
                            }else{
                                System.out.println(message);
                            }
                        }
                    }

                }
            }
        });


    }
}
