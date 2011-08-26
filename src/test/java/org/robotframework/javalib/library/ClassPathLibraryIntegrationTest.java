package org.robotframework.javalib.library;

import org.jmock.cglib.MockObjectTestCase;
import org.robotframework.javalib.util.ArrayUtil;


public class ClassPathLibraryIntegrationTest extends MockObjectTestCase {
    private ClassPathLibrary classPathLibrary;

    protected void setUp() throws Exception {
        classPathLibrary = new ClassPathLibrary("org/robotframework/**/**.class");
    }

    public void testFindsKeywords() throws Exception {
        String[] keywordNames = classPathLibrary.getKeywordNames();
        assertEquals(4, keywordNames.length);
        ArrayUtil.arrayContains("springkeyword", keywordNames);
        ArrayUtil.arrayContains("emptykeyword", keywordNames);
        ArrayUtil.arrayContains("conflictingkeyword", keywordNames);
    }

    public void testRunsKeyword() throws Exception {
        Object result = classPathLibrary.runKeyword("Conflicting Keyword", null);
        assertEquals("Classpath Keyword", result.toString());
    }

    public void testUsesProvidedPattern() throws Exception {
        assertTrue(classPathLibrary.getKeywordNames().length > 0);

        classPathLibrary = new ClassPathLibrary();
        classPathLibrary.setKeywordPattern("com/nonexistent/**.class");
        assertEquals(0, classPathLibrary.getKeywordNames().length);
    }

    public void testThrowsExceptionIfKeywordPatternIsNotSet() throws Exception {
        try {
            new ClassPathLibrary().getKeywordNames();
            fail("Expected IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            assertEquals("Keyword pattern must be set before calling getKeywordNames.", e.getMessage());
        }
    }
}
