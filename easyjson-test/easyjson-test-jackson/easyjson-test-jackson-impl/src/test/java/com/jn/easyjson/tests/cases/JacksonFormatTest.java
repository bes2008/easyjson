package com.jn.easyjson.tests.cases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jn.easyjson.core.util.JSONs;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SampleTime)
@State(Scope.Benchmark)
@Threads(4)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JacksonFormatTest {


    @Test
    public void benchmarkTest() throws Exception{
        Options options = new OptionsBuilder().include(JacksonFormatTest.class.getSimpleName()).build();
        new Runner(options).run();
    }


    @Test
    public void concurrentTest()throws Throwable {
        int threadCount = 10;
        int iteration = 10000;
        for (int i=0; i< threadCount; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    for(int j=0 ; j<iteration;j++) {
                        doTest();
                    }
                }
            }) .start();
        }

        Thread.sleep(10000000L);
    }

    @Benchmark
    @Warmup(iterations = 1, batchSize = 20)
    @Measurement(iterations = 300, batchSize = 100)
    public void doTest(){
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
