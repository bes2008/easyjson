package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.tests.entity.struct.Lic;
import com.jn.langx.http.rest.RestRespBody;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.reflect.type.Types;

import java.lang.reflect.Type;
import java.util.List;

public class GsonImplBytesTest {
    public static void main(String[] args) {
        JSON json = JSONBuilderProvider.simplest();

        Lic lic = new Lic();
        lic.setSerialNumber("ABCD-EDFG-HIJK-LMNO");
        lic.setLicDataBytes("123456".getBytes(Charsets.UTF_8));

        List<Lic> d = Collects.newArrayList(lic);
        RestRespBody restRespBody = RestRespBody.success(d);


        String message = json.toJson(restRespBody);
        System.out.println(message);

        Type t = Types.getParameterizedType(RestRespBody.class, Types.getListParameterizedType(Lic.class));
        RestRespBody r = json.fromJson(message, t);
        System.out.println(r);
    }
}
