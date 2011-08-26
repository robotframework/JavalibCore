package org.robotframework.javalib.library;

import java.util.Arrays;

import junit.framework.TestCase;

public class SpringLibraryIntegrationTest extends TestCase {
    private SpringLibrary springLibrary;

    protected void setUp() throws Exception {
        springLibrary = new SpringLibrary("org/robotframework/**/keywords.xml");
    }

    public void testFindsKeywords() throws Exception {
        String[] keywordNames = springLibrary.getKeywordNames();
        assertTrue(Arrays.equals(new String[] { "keywordwiredfromspring", "conflictingkeyword", "keywordwithunnormalizedname" }, keywordNames));
    }

    public void testRunsKeyword() throws Exception {
        Object returnValue = springLibrary.runKeyword("Keyword Wired From Spring", null);
        assertEquals("Spring Keyword", returnValue.toString());
    }

    public void testUsesProvidedPattern() throws Exception {
        assertTrue(springLibrary.getKeywordNames().length > 0);

        springLibrary = new SpringLibrary();
        springLibrary.setConfigFilePattern("com/nonexistent/**.xml");
        assertEquals(0, springLibrary.getKeywordNames().length);
    }

    public void testThrowsExceptionIfTheConfigFilePatternIsNotSet() throws Exception {
        try {
            new SpringLibrary().getKeywordNames();
            fail("Expected IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            assertEquals("Config file pattern must be set before calling getKeywordNames.", e.getMessage());
        }
    }
}
