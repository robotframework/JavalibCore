package org.robotframework.javalib.beans.common;

import java.io.IOException;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.robotframework.javalib.beans.common.ClassNameResolver;
import org.robotframework.javalib.beans.common.DefaultClassFinder;
import org.springframework.core.io.Resource;

public class DefaultClassFinderTest extends MockObjectTestCase {
    private DefaultClassFinder classFinder;
    private Mock mockClassLoader;
    private Mock mockClassNameResolver;
    private String pattern = "com/test/**/pattern/**.class";
    private Resource[] resources = new Resource[0];

    protected void setUp() throws Exception {
        mockClassLoader = mock(ClassLoader.class);
        mockClassNameResolver = mock(ClassNameResolver.class);
        classFinder = new DefaultClassFinder((ClassLoader) mockClassLoader.proxy()) {
            @Override
            public Resource[] getResources(String locationPattern) throws IOException {
                return resources;
            }
        };
        classFinder.setClassNameResolver((ClassNameResolver) mockClassNameResolver.proxy());
    }

    protected void tearDown() throws Exception {
        resources = new Resource[0];
    }

    public void testReturnsMatchingClasses() throws Exception {
        String className1 = "com.test.package.Keyword";
        String className2 = "com.test.anotherpackage.AnotherKeyword";

        resources = new Resource[] {
            ((Resource) mock(Resource.class).proxy()),
            ((Resource) mock(Resource.class).proxy())
        };

        setClassNameResolution(resources[0], className1);
        setClassNameResolution(resources[1], className2);

        setClassToLoad(className1, Class.class);
        setClassToLoad(className2, Object.class);


        Class[] classes = classFinder.getClasses(pattern);
        assertEquals(Class.class, classes[0]);
        assertEquals(Object.class, classes[1]);
    }

    private void setClassToLoad(String className, Class<?> classToLoad) {
        mockClassLoader.expects(once()).method("loadClass")
          .with(eq(className))
          .will(returnValue(classToLoad));
    }

    public void testIOExceptionIsThrownAsRuntimeException() throws Exception {
        try {
            new DefaultClassFinder() {
                public Resource[] getResources(String locationPattern) throws IOException {
                    throw new IOException("exception");
                }
            }.getClasses(null);
            fail();
        } catch(RuntimeException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    private void setClassNameResolution(Resource resource, String className) {
        mockClassNameResolver.expects(once()).method("resolveClassName")
            .with(eq(resource),    ANYTHING)
            .will(returnValue(className));
    }
}
