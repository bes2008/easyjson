package com.jn.easyjson.tests.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import org.testng.Assert;
import com.jn.langx.util.reflect.Reflects;

/**
 * 测试结果比对工具
 *
 * @author zhaohuihua
 * @version 20200614
 */
public class CompareTools {

    /** 比对两个对象的字段值是否一致 **/
    public static void compareEntityFields(Object actual, Object expected) {
        Assert.assertEquals(actual.getClass(), expected.getClass());
        Class<?> clazz = actual.getClass();
        // 遍历所有字段
        Collection<Field> fields = Reflects.getAllDeclaredFields(clazz);
        for (Field field : fields) {
            String desc = clazz.getSimpleName() + '.' + field.getName();
            // 获取actual的字段值
            Object aValue = null;
            Exception aException = null;
            try {
                aValue = field.get(actual);
            } catch (Exception e) {
                aException = e;
            }
            // 获取expected的字段值
            Object eValue = null;
            Exception eException = null;
            try {
                eValue = field.get(expected);
            } catch (Exception e) {
                eException = e;
            }
            // 如果发生异常了, 异常必须相等, 否则字段值必须相等
            if (aException != null || eException != null) {
                Assert.assertEquals(aException, eException, "Get " + desc + " exception");
            } else {
                Assert.assertEquals(aValue, eValue, desc + " value");
            }
        }
    }
}
