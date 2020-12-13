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
        BusEntity busEntity1 =getBusBean();
        String jsonstring = json.toJson(busEntity1);
        System.out.println(jsonstring);
        BusEntity busEntity2 = json.fromJson(jsonstring, BusEntity.class);
        System.out.println(busEntity1.equals(busEntity2));
    }

    protected BusEntity getBusBean(){
        BusEntity entity = new BusEntity();
        entity.setId("test-id-01");
        entity.setName("test-name");
        entity.setCreateTime(1607831015520L);
        entity.setUpdateTime(new Timestamp(1607831015520L));
        return entity;
    }
}
