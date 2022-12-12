package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.node.JsonObjectNode;
import com.jn.easyjson.core.util.JSONs;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class EasyjsonPerformanceTest extends AbstractBaseTest {
    @Test
    public void test001() {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start > 30 * 60 * 1000) {
            Map<String, String> messageMap = new HashMap<String, String>();
            messageMap.put("stream", "stdout");
            String messageString = "{\"detail\":{\"a\":\"{\"detail\":{},\"result\":0,\"resultNote\":\"操作成功\\r\\n\"}\"},\"result\":0,\"resultNote\":\"操作成功\"}";
            messageMap.put("log", messageString);
            JsonObjectNode node = (JsonObjectNode) JSONs.toJsonTreeNode(messageMap);

            System.out.println(messageString);
            System.out.println(node.getProperty("log").toString());
            String json = node.toString();
            System.out.println(JSONs.parse(json));
        }
    }
}
