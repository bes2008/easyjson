package com.jn.easyjson.tests.cases.fastjson_to_jackson;

import com.alibaba.fastjson.easyjson.FastEasyJsons;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import org.testng.annotations.Test;

public class FastjsonAdapterTests {
    @Test(priority = 1000)
    public void test() throws Throwable {
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 20; i++) {
            executorService.submit(() -> {
                System.out.println(FastEasyJsons.getJSONFactory(JSON.DEFAULT_GENERATE_FEATURE).get());
            });
        }
        TimeUnit.SECONDS.sleep(100);
    }
}
