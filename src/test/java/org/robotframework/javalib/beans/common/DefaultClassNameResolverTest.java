package org.robotframework.javalib.beans.common;

import java.io.IOException;
import java.net.URL;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.beans.common.DefaultClassNameResolver;
import org.springframework.core.io.Resource;

public class DefaultClassNameResolverTest extends MockObjectTestCase {
    private Mock mockResource;
    private DefaultClassNameResolver classNameResolver;

    protected void setUp() throws Exception {
        mockResource = mock(Resource.class);
        classNameResolver = new DefaultClassNameResolver();
    }

    public void testResolvesResourceToAClassName() throws Exception {
        URL url = new URL("file:///home/someone/workspace/project/target/classes/com/test/package/MyClass.class");

        mockResource.expects(once()).method("getURL")
            .will(returnValue(url));

        String className = classNameResolver.resolveClassName((Resource) mockResource.proxy(), "com.test");
        assertEquals("com.test.package.MyClass", className);
    }

    public void testThrowsExceptionIfFilesystemPathIsInvalid() throws Exception {
        String path = "com/test/for/example/a/directory/is/invalid";
        URL url = new URL("file:///" + path);

        mockResource.expects(once()).method("getURL")
            .will(returnValue(url));

        try {
            classNameResolver.resolveClassName((Resource) mockResource.proxy(), "com.test");
            fail();
        } catch(IllegalArgumentException e) {
            assertEquals("Couldn't convert class location <" + path + "> to class name", e.getMessage());
        }
    }

    public void testThrowsExceptionIfIOExceptionOccurs() throws Exception {
        mockResource.expects(once()).method("getURL")
            .will(throwException(new IOException()));
        Resource resource = (Resource) mockResource.proxy();

        try {
            classNameResolver.resolveClassName(resource, "");
            fail();
        } catch(RuntimeException e) {
            assertEquals("Couldn't determine filesystem path for resource <" + resource + ">", e.getMessage());
        }
    }
}
