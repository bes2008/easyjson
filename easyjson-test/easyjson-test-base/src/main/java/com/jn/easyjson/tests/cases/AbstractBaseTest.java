package com.jn.easyjson.tests.cases;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import org.testng.annotations.BeforeMethod;
import com.gitee.qdbp.tools.files.PathTools;

public abstract class AbstractBaseTest {

    protected String readClassResourceText(String path) {
        URL url = PathTools.findClassResource(this.getClass(), path);
        try {
            return PathTools.downloadString(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeMethod
    public void testStart(Method method) {
        System.out.println("/************************************************************\\");
        System.out.println("| " + method.getDeclaringClass().getSimpleName() + '.' + method.getName());
        System.out.println("\\************************************************************/");
    }
}
