package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.factory.JsonFactorys;
import com.jn.easyjson.core.util.JSONs;
import com.jn.langx.http.rest.RestRespBody;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.net.http.HttpMethod;
import org.testng.annotations.Test;

import java.util.*;

public class EasyjsonRestRespBodyTest extends AbstractBaseTest{

    @Test(priority = 10001)
    public void testMultiValue(){
        RestRespBody respBody = new RestRespBody();
        respBody.setMethod(HttpMethod.GET);
        respBody.setData("3");
        respBody.setStatusCode(200);
        respBody.setSuccess(true);
        respBody.setTimestamp(new Date().getTime());
        Map<String, List<String>> requestHeaders  = new HashMap<String, List<String>>();
        List<String> values = Collects.asList("1","2","3","4","5");
        requestHeaders.put("head1", values);
        respBody.setRequestHeaders(requestHeaders);

        JSON json  = JsonFactorys.getJSONFactory().get();
        String body = json.toJson(respBody);
        RestRespBody bd= json.fromJson(body,RestRespBody.class);
        System.out.println(bd);

        Map map = JSONs.toMap(body);
        System.out.println(map);
    }

}
