package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.tests.entity.BusEntity;
import com.jn.easyjson.tests.utils.Asserts;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import java.sql.Timestamp;
import java.util.Calendar;

public abstract class EasyjsonTimestampTest extends AbstractBaseTest {
    private static JSON json = JSONBuilderProvider.create().serializeNulls(true).serializeEnumUsingIndex(true).build();

    @Test(priority = 10001)
    public void testCase1() {
        BusEntity busEntity1 = getBusBean();
        String jsonstring1= json.toJson(busEntity1);
        System.out.println(jsonstring1);
        BusEntity busEntity2 = json.fromJson(jsonstring1, BusEntity.class);
        String jsonstring2 = json.toJson(busEntity2);
        System.out.println(jsonstring2);
        Asserts.assertDeepEquals(busEntity1,busEntity2);
    }

    protected BusEntity getBusBean() {
        BusEntity entity = new BusEntity();
        entity.setId("test-id-01");
        entity.setName("test-name");
        entity.setCreateTime(1607831015520L);
        entity.setUpdateTime(new Timestamp(1607831015520L));
        entity.setCalendar(Calendar.getInstance());
        return entity;
    }
}
