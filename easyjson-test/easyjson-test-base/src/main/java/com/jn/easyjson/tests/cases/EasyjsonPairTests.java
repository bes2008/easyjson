package com.jn.easyjson.tests.cases;

import com.jn.easyjson.core.factory.JsonFactorys;
import com.jn.easyjson.core.factory.JsonScope;
import com.jn.easyjson.tests.entity.struct.MyPairBean;
import com.jn.langx.util.reflect.type.Types;
import com.jn.langx.util.struct.pair.NameValuePair;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EasyjsonPairTests extends AbstractBaseTest {
    protected static List<MyPairBean> beans = new ArrayList<MyPairBean>();

    static {
        Set<NameValuePair> set = new HashSet<NameValuePair>();
        set.add(new NameValuePair("key1", "value1"));
        set.add(new NameValuePair("key2", "value2"));
        set.add(new NameValuePair("key3", "value3"));

        MyPairBean bean = new MyPairBean();
        bean.setPairSet(set);
        bean.setId("id-0001");
        beans.add(bean);
    }

    private static final String jsonstring = "[{\"pairSet\":[{\"key\":\"key1\",\"value\":\"value1\",\"name\":\"key1\"},{\"key\":\"key2\",\"value\":\"value2\",\"name\":\"key2\"},{\"key\":\"key3\",\"value\":\"value3\",\"name\":\"key3\"}],\"id\":\"id-0001\"}]";

    @Test(priority = 10001)
    public void toJson() {
        String str = JsonFactorys.getJSONFactory(JsonScope.SINGLETON).get().toJson(beans);
        System.out.println(str);
    }

    @Test(priority = 10002)
    public void fromJson() {
        List<MyPairBean> beans2 = JsonFactorys.getJSONFactory(JsonScope.SINGLETON).get().fromJson(jsonstring, Types.getListParameterizedType(MyPairBean.class));
        System.out.println(beans2.size());
    }
}
