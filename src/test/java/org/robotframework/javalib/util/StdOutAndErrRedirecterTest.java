package org.robotframework.javalib.util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class StdOutAndErrRedirecterTest {
    private String emptyString = "";
    private String logOutput = "StdOutAndErrRedirecterTest";
    private StdStreamRedirecter streamRedirecter;

    @BeforeEach
    public void setUp() {
        this.streamRedirecter = new StdStreamRedirecter();
        this.streamRedirecter.redirectStdStreams();
    }

    @AfterEach
    public void tearDown() {
        streamRedirecter.resetStdStreams();
    }

    @Test
    public void testRedirectsSystemOutToInternalBuffer() {
        System.out.print(logOutput);
        assertEquals(logOutput, streamRedirecter.getStdOutAsString());
    }

    @Test
    public void testRedirectsSystemErrToInternalBuffer() {
        System.err.print(logOutput);
        assertEquals(logOutput, streamRedirecter.getStdErrAsString());
    }

    @Test
    public void testResettingStreamsRedirectsSystemOutBackToSystemOut() {
        streamRedirecter.resetStdStreams();
        assertEquals(System.out, streamRedirecter.stdOut);
    }

    @Test
    public void testResettingStreamsRedirectsSystemErrBackToSystemOut() {
        streamRedirecter.resetStdStreams();
        assertEquals(System.err, streamRedirecter.stdErr);
    }

    @Test
    public void testGettingSystemOutEmptiesTheBuffer() {
        System.out.print(logOutput);
        streamRedirecter.getStdOutAsString();
        assertEquals(emptyString, streamRedirecter.getStdOutAsString());
    }

    @Test
    public void testGettingSystemErrEmptiesTheBuffer() {
        System.err.print(logOutput);
        streamRedirecter.getStdErrAsString();
        assertEquals(emptyString, streamRedirecter.getStdErrAsString());
    }
}
