package com.jn.easyjson.tests.cases;

/**
 * JsonlibToGson复杂对象测试
 *
 * @author zhaohuihua
 * @version 20200615
 */
public class JsonlibToGsonPagingRequestTest extends EasyJsonPagingRequestTest {

    // gson不会序列化没有字段的getter方法

    protected String getPagingRequestStringIncludeNulls() {
        return readClassResourceText(this.getClass(), "gson/PagingRequestIncludeNulls.json");
    }

    protected String getPagingRequestStringExcludeNulls() {
        return readClassResourceText(this.getClass(), "gson/PagingRequestExcludeNulls.json");
    }

    protected String getPagingRequestStringNumberAsString() {
        return readClassResourceText(this.getClass(), "gson/PagingRequestNumberAsString.json");
    }
}
