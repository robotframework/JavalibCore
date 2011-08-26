package org.robotframework.javalib.beans.common;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DefaultClassFinderIntegrationTest {
    private String fileSeparator = System.getProperty("file.separator");
    private String tmpDir = System.getProperty("java.io.tmpdir");
    
    @BeforeClass
    public static void startServer() throws Exception {
        FileServer.start();
    }
    
    @AfterClass
    public static void stopServer() throws Exception {
        FileServer.stop();
    }
    
    @Test
    public void getsJarsFromURL() throws Exception {
        new DefaultClassFinder().getJarFile(FileServer.URL_BASE + "/test.jar");
        assertTrue(new File(tmpDir + fileSeparator + "test.jar").exists());
    }
}
