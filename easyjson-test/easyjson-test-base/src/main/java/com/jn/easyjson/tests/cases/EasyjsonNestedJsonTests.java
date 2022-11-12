package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.node.JsonObjectNode;
import com.jn.easyjson.core.util.JSONs;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class EasyjsonNestedJsonTests extends AbstractBaseTest {
    @Test
    public void test001(){
        Map<String, String> messageMap = new HashMap<String,String>();
        messageMap.put("stream","stdout");
        String messageString = "\"[2022-10-25 17:00:00] INFO com.xxx.yy :http request: time=1ms, url=/a/b, verb=POST, respStatus=200, contentType=application/json, token=null, params={}, requestBody={\"alarm1\":0}, responseBody={\"detail\":{},\"result\":0,\"resultNote\":\"操作成功\"}\"";
        messageMap.put("log",messageString);
        JsonObjectNode node = (JsonObjectNode) JSONs.toJsonTreeNode(messageMap);

        System.out.println(messageString);
        System.out.println(node.getProperty("log").toString());
    }
}
