package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.tests.entity.BusEntity;
import org.testng.annotations.Test;

import java.sql.Timestamp;

public abstract class EasyjsonTimestampTest extends AbstractBaseTest {
    private static JSON json = JSONBuilderProvider.simplest();

    @Test(priority = 10001)
    public void testCase1(){
        System.out.println(json.toJson(getBusBean()));
    }

    protected BusEntity getBusBean(){
        BusEntity entity = new BusEntity();
        entity.setId("test-id-01");
        entity.setName("test-name");
        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return entity;
    }
}
