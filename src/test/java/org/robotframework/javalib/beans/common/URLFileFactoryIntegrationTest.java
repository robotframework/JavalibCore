package org.robotframework.javalib.beans.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class URLFileFactoryIntegrationTest {
    private String localDirectoryPath = System.getProperty("java.io.tmpdir");
    private String fileSeparator = System.getProperty("file.separator");
    private String localFilePath = localDirectoryPath + "/network_file.txt";
    private String url = FileServer.URL_BASE + "/network_file.txt";
    
    @BeforeClass
    public static void startFileServer() throws Exception {
        FileServer.start();
    }

    @Before
    public void deleteTemporaryResources() {
        new File(localFilePath).delete();
    }
    
    @AfterClass
    public static void stopServer() throws Exception {
        FileServer.stop();
    }

    @Test
    public void retrievesFileFromURL() throws Exception {
        File localFile = new URLFileFactory(localDirectoryPath).createFileFromUrl(url);
        assertEquals(localFilePath, localFile.getAbsolutePath());
        assertFileEqualsToUrl(localFile);
    }

    @Test
    public void doesntRetrieveFileWhenLocalCopyExistsAndURLHasntChanged() throws Exception {
        File localFile = new URLFileFactory(localDirectoryPath).createFileFromUrl(url);
        long lastModified = localFile.lastModified();
        localFile = new URLFileFactory(localDirectoryPath).createFileFromUrl(url);
        
        assertEquals(lastModified, localFile.lastModified());
    }
    
    @Test
    public void retrievesFileWhenURLHasChanged() throws Exception {
        File localFile = new URLFileFactory(localDirectoryPath).createFileFromUrl(url);
        localFile.setLastModified(1000);
        long oldLastModified = localFile.lastModified();
        
        updateURLContent();
        
        localFile = new URLFileFactory(localDirectoryPath).createFileFromUrl(url);
        long newLastModified = localFile.lastModified();
        
        assertTrue("Expected " + oldLastModified + " to be less than " + newLastModified, oldLastModified < newLastModified);
    }
    
    private void assertFileEqualsToUrl(File file) throws Exception {
        assertStreamsEqual(new FileInputStream(file), new URL(url).openStream());
    }

    private void assertStreamsEqual(InputStream expectedStream, InputStream actualStream) throws IOException {
        try {
            assertTrue(IOUtils.contentEquals(expectedStream, actualStream));
        } finally {
            actualStream.close();
            expectedStream.close();
        }
    }
    
    private void updateURLContent() throws IOException {
        FileUtils.touch(new File(FileServer.RESOURCE_BASE + fileSeparator + "network_file.txt"));
    }
}