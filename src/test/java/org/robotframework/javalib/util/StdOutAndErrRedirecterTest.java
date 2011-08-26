package org.robotframework.javalib.util;

import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.util.StdStreamRedirecter;

public class StdOutAndErrRedirecterTest extends MockObjectTestCase {
    private String emptyString = "";
    private String logOutput = "StdOutAndErrRedirecterTest";
    private StdStreamRedirecter streamRedirecter;
    
    public void setUp() {
        streamRedirecter = new StdStreamRedirecter();
        streamRedirecter.redirectStdStreams();
    }
    
    public void tearDown() {
        streamRedirecter.resetStdStreams();
    }

    public void testRedirectsSystemOutToInternalBuffer() {
        System.out.print(logOutput);
        assertEquals(logOutput, streamRedirecter.getStdOutAsString());
    }
    
    public void testRedirectsSystemErrToInternalBuffer() {
        System.err.print(logOutput);
        assertEquals(logOutput, streamRedirecter.getStdErrAsString());
    }
    
    public void testResettingStreamsRedirectsSystemOutBackToSystemOut() {
        streamRedirecter.resetStdStreams();
        assertEquals(System.out, streamRedirecter.stdOut);
    }
    
    public void testResettingStreamsRedirectsSystemErrBackToSystemOut() {
        streamRedirecter.resetStdStreams();
        assertEquals(System.err, streamRedirecter.stdErr);
    }
    
    public void testGettingSystemOutEmptiesTheBuffer() {
        System.out.print(logOutput);
        streamRedirecter.getStdOutAsString();
        assertEquals(emptyString, streamRedirecter.getStdOutAsString());
    }
    
    public void testGettingSystemErrEmptiesTheBuffer() {
        System.err.print(logOutput);
        streamRedirecter.getStdErrAsString();
        assertEquals(emptyString, streamRedirecter.getStdErrAsString());
    }
}
