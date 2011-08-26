package org.robotframework.javalib.library;

import junit.framework.TestCase;

import org.robotframework.javalib.keyword.KeywordNameCollisionException;
import org.robotframework.javalib.util.ArrayUtil;


public class CompositeLibraryIntegrationTest extends TestCase {
    private CompositeLibrary compositeLibrary;

    protected void setUp() throws Exception {
        compositeLibrary = new CompositeLibrary("org/robotframework/**/keyword/**/**.class", "org/**/keywords.xml");
    }

    public void testFindsKeywords() throws Exception {
        String[] expectedKeywordNames = new String[] {
            "keywordwithunnormalizedname",
            "conflictingkeyword",
            "emptykeyword",
            "keywordwiredfromspring",
            "springkeyword",
            "failingkeyword",
            "somekeyword",
            "keywordthatreturnsitsarguments",
            "keywordwithvariableargumentcount"
            };

        ArrayUtil.assertArraysContainSame(expectedKeywordNames, compositeLibrary.getKeywordNames());
    }

    public void testRunsKeywordFromSpringKeywordFactory() throws Exception {
        Object result = compositeLibrary.runKeyword("Empty Keyword", null);
        assertEquals("Empty Keyword Return Value", result.toString());
    }

    public void testRunsKeywordFromSpringClasPathKeywordFactory() throws Exception {
        Object result = compositeLibrary.runKeyword("Keyword Wired From Spring", null);
        assertEquals("Spring Keyword", result.toString());
    }

    public void testRunsKeywordFromAnnotationKeywordFactory() throws Exception {
        String keywordArgument = "someArgument";
        assertNull(compositeLibrary.runKeyword("Some Keyword", new String[] { keywordArgument }));
        assertEquals(keywordArgument, compositeLibrary.runKeyword("Keyword That Returns Its Arguments",
            new String[] { keywordArgument }));
    }

    public void testRunsConflictKeywordIfConflictHasOccurred() throws Exception {
        try {
            compositeLibrary.runKeyword("conflictingkeyword", null);
            fail();
        } catch(KeywordNameCollisionException expected) {
            assertTrue(true);
        }
    }

    public void testPatternPropagatesToClassPathLibrary() throws Exception {
        compositeLibrary.setKeywordPattern("com/nonexistent/**.class");
        assertFalse(ArrayUtil.arrayContains("emptykeyword", compositeLibrary.getKeywordNames()));
    }

    public void testPatternPropagatesToSpringLibrary() throws Exception {
        compositeLibrary.setConfigFilePattern("com/nonexist/**.xml");
        assertFalse(ArrayUtil.arrayContains("keywordwiredfromspring", compositeLibrary.getKeywordNames()));
    }
}
