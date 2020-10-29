package com.jn.easyjson.tests.cases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jn.easyjson.tests.entity.struct.Lic;
import com.jn.langx.http.rest.RestRespBody;
import com.jn.langx.util.reflect.type.Types;

import java.lang.reflect.Type;
import java.util.List;

public class GsonBytesTest {
    private static String message = "{\"success\":true,\"statusCode\":200,\"timestamp\":1603951506731,\"errorCode\":\"\",\"errorMessage\":\"\",\"data\":[{\"serialNumer\":\"ABCD-EDFG-HIJK-LMNO\",\"licDataBytes\":\"NGtkUxIRL3RabDk4ZTJ6e\"}]}";

    public static void main(String[] args) {
        Type t = Types.getParameterizedType(RestRespBody.class, Types.getListParameterizedType(Lic.class));
        TypeToken<RestRespBody<List<Lic>>> typeToken = new TypeToken<RestRespBody<List<Lic>>>() {
        };
        RestRespBody r = new Gson().fromJson(message, t);
        System.out.println(r);
    }
}
