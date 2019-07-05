/*
 * $Id: JSONArray.java,v 1.1 2006/04/15 14:10:48 platform Exp $
 * Created on 2006-4-10
 */
package org.json.simple;

import com.github.fangjinuo.easyjson.core.JSONBuilderProvider;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A JSON array. JSONObject supports java.util.List interface.
 *
 * @author FangYidong<fangyidong       @       yahoo.com.cn>
 */
public class JSONArray extends ArrayList implements List, JSONAware, JSONStreamAware {
    private static final long serialVersionUID = 3957988303675231981L;

    /**
     * Encode a list into JSON text and write it to out.
     * If this list is also a JSONStreamAware or a JSONAware, JSONStreamAware and JSONAware specific behaviours will be ignored at this top level.
     *
     * @param list
     * @param out
     * @see org.json.simple.JSONValue#writeJSONString(Object, Writer)
     */
    public static void writeJSONString(List list, Writer out) throws IOException {
        String string = toJSONString(list);
        out.write(string);
    }

    @Override
    public void writeJSONString(Writer out) throws IOException {
        writeJSONString(this, out);
    }

    /**
     * Convert a list to JSON text. The result is a JSON array.
     * If this list is also a JSONAware, JSONAware specific behaviours will be omitted at this top level.
     *
     * @param list
     * @return JSON text, or "null" if list is null.
     * @see org.json.simple.JSONValue#toJSONString(Object)
     */
    public static String toJSONString(List list) {
        return JSONValue.toJSONString(list);
    }

    @Override
    public String toJSONString() {
        return toJSONString(this);
    }

    @Override
    public String toString() {
        return toJSONString();
    }


}
