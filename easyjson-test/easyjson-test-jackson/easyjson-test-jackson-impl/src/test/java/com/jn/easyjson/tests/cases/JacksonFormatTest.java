package com.jn.easyjson.tests.cases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jn.easyjson.core.util.JSONs;
import org.junit.Test;
import java.util.Date;


public class JacksonFormatTest {


    @Test
    public void doTest()throws Throwable {
        int threadCount = 10;
        int iteration = 10000;
        for (int i=0; i< threadCount; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    for(int j=0 ; j<iteration;j++) {
                        try {
                            Xyz xyz = new Xyz();
                            xyz.setCreateTime(new Date());
                            xyz.setId("001");
                        //    System.out.println(xyz.getCreateTime().getTime());

                            String json1 = JSONs.toJson(xyz);
                            String json2 = new ObjectMapper().writeValueAsString(xyz);
                        //    System.out.println(json1);
                        //    System.out.println(json2);
                            Xyz xyz1 = JSONs.parse(json1, Xyz.class);
                            Xyz xyz2 = new ObjectMapper().readValue(json2, Xyz.class);

                        //    System.out.println(xyz2.getCreateTime().getTime());
                        //    System.out.println(xyz1.getCreateTime().getTime());

                            System.out.println(xyz2.getCreateTime().getTime() - xyz1.getCreateTime().getTime());
                        }catch (Throwable e){
                            e.printStackTrace();
                        }
                    }
                }
            }) .start();
        }

        Thread.sleep(10000000L);
    }


}
