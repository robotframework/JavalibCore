package org.robotframework.javalib.beans.common;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.junit.Before;


public class DefaultClassFinderCreatingURLResourcesTest extends MockObjectTestCase {
    private DefaultClassFinder defaultClassFinder;
    private File pathToJar = new File("./src/test/resources/test.jar");
    private JarFile expectedJarFile;
    private String url = "http://somedomain/someresource.jar";

    @Before
    protected void setUp() throws Exception {
        defaultClassFinder = new DefaultClassFinder() {
            JarFile createJarFile(File fileFromUrl) throws IOException {
                expectedJarFile = new JarFile(pathToJar);
                return expectedJarFile;
            }
            @Override
            protected URLFileFactory createURLFileFactory() {
                Mock fileFactory = mock(URLFileFactory.class, new Class[]{String.class}, new Object[]{"/tmp"});
                fileFactory.expects(once()).method("createFileFromUrl")
                    .with(eq(url))
                    .will(returnValue(pathToJar));
                return (URLFileFactory) fileFactory.proxy();
            }
        };
    }

    public void testCreatesJarFileFromURL() throws Exception {
        JarFile actualJarFile = defaultClassFinder.getJarFile(url);
        assertEquals(expectedJarFile, actualJarFile);
    }

}
