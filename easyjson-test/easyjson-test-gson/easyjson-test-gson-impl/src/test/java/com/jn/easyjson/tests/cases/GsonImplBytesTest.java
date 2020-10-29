package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.tests.entity.struct.Lic;
import com.jn.langx.http.rest.RestRespBody;
import com.jn.langx.util.reflect.type.Types;

import java.lang.reflect.Type;

public class GsonImplBytesTest {
    private static String message = "{\"success\":true,\"statusCode\":200,\"timestamp\":1603951506731,\"errorCode\":\"\",\"errorMessage\":\"\",\"data\":[{\"serialNumer\":\"ABCD-EDFG-HIJK-LMNO\",\"licDataBytes\":\"NGtkUxIRL3RabDk4ZTJ6e\"}]}";

    public static void main(String[] args) {
        JSON json = JSONBuilderProvider.simplest();
        Type t = Types.getParameterizedType(RestRespBody.class, Types.getListParameterizedType(Lic.class));
        RestRespBody r = json.fromJson(message, t);
        System.out.println(r);
    }
}
