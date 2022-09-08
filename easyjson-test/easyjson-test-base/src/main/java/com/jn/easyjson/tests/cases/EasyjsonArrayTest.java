package com.jn.easyjson.tests.cases;

import com.gitee.qdbp.tools.files.PathTools;
import com.jn.easyjson.core.util.JSONs;
import com.jn.easyjson.tests.entity.user.UserEntityWrapper;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class EasyjsonArrayTest extends AbstractBaseTest {

    @Test(priority = 10003)
    public void testReader10003() throws IOException {
        URL url = PathTools.findClassResource(EasyJsonBaseTest.class, "json/PersonArrayString.json");
        InputStream input = null;
        try {
            input = url.openStream();
            Reader reader = new InputStreamReader(input, Charsets.UTF_8);
            UserEntityWrapper actual = JSONs.parse(reader, UserEntityWrapper.class);
            System.out.println(actual.getData().size());
            System.out.println("name:" + actual.getName());
        } catch (IOException ex) {

        } finally {
            IOs.close(input);
        }
    }
}
