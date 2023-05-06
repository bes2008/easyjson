package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.JsonTreeNode;
import com.jn.easyjson.core.node.JsonObjectNode;
import com.jn.easyjson.core.util.JSONs;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.unicode.BOM;
import com.jn.langx.util.struct.counter.IntegerCounter;
import com.jn.langx.util.struct.counter.SimpleIntegerCounter;
import org.testng.annotations.Test;


public class EasyjsonNestedJsonStringTests {
    @Test
    public void test001() {
        String[] filenames = {
                "session_kafka_conatienr_data.log",
                "00DE8E3E-95E3-4885-A760-A269A6836AE2.txt"
        };
        Collects.forEach(filenames, new Consumer<String>() {
            @Override
            public void accept(String filename) {
                testFile(filename);
            }
        });
    }


    private void testFile(final String file) {
        Resource res = Resources.loadClassPathResource(file, EasyjsonNestedJsonStringTests.class);

        final IntegerCounter counter = new SimpleIntegerCounter(0);
        Resources.readLines(res, Charsets.UTF_8, new Consumer<String>() {
            @Override
            public void accept(String line) {
                if (Strings.isNotBlank(line) && counter.get() < 200) {
                    if (counter.get() == 0) {
                        byte[] bytes = line.getBytes();
                        BOM bom = Strings.findBom(line);
                        if (bom != null) {
                            byte[] newBytes = new byte[bytes.length - bom.getBytes().length];
                            System.arraycopy(bytes, bom.getBytes().length, newBytes, 0, newBytes.length);
                            bytes = newBytes;
                            line = new String(bytes, Charsets.getCharset(bom.getName()));
                        }
                    }
                    counter.increment();
                    if (!line.contains("container-cached.log")) {
                        System.out.println(StringTemplates.formatWithPlaceholder("************line: {}, file: {}*************", counter.get(), file));
                        System.out.println(line);
                        JsonObjectNode node = JSONs.parseObject(line);
                        JsonTreeNode message = node.getProperty("message");
                        if (message.isJsonPrimitiveNode()) {
                            String msg = message.getAsString();
                            JsonTreeNode messageNode = JSONs.parse(msg);
                            System.out.println(StringTemplates.formatWithPlaceholder("not object: {}", messageNode));
                        }
                    }

                }
            }
        });
    }

}
