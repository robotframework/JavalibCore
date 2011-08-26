package org.robotframework.javalib.library;

import junit.framework.TestCase;

import org.robotframework.javalib.util.ArrayUtil;


public class AnnotationLibraryIntegrationTest extends TestCase {
    private AnnotationLibrary annotationLibrary;
    private String keywordThatReturnsItsArguments = "keywordThatReturnsItsArguments";

    @Override
    protected void setUp() throws Exception {
        annotationLibrary = new AnnotationLibrary("org/robotframework/**/keyword/**/**.class");
    }

    public void testFindsAnnotatedKeywordsFromClassPath() throws Exception {
        String[] keywordNames = annotationLibrary.getKeywordNames();
        String[] expectedKeywordNames = new String[] { "failingKeyword", "someKeyword", keywordThatReturnsItsArguments, "keywordWithVariableArgumentCount" };

        ArrayUtil.assertArraysContainSame(expectedKeywordNames, keywordNames);
    }

    public void testRunsKeywords() throws Exception {
        String keywordArgument = "someArgument";
        Object executionResult = annotationLibrary.runKeyword(keywordThatReturnsItsArguments,
            new String[] { keywordArgument });

        assertEquals(keywordArgument, executionResult);
    }

    public void testFindsKeywordDocumentation() throws Exception {
        String documentation = annotationLibrary.getKeywordDocumentation("someKeyword");
        assertEquals("Some documentation", documentation);
    }

    public void testFindsKeywordArguments() throws Exception {
        String[] keywordArguments = annotationLibrary.getKeywordArguments("keywordThatReturnsItsArguments");
        ArrayUtil.assertArraysEquals(new String[] { "arg" }, keywordArguments);
    }

    public void testFindsKeywordArgumentsWithKeywordArgumentsAnnotation() throws Exception {
        String[] keywordArguments = annotationLibrary.getKeywordArguments("someKeyword");
        ArrayUtil.assertArraysEquals(new String[] { "overridenArgumentName" }, keywordArguments);
    }

    public void testExtractsInnerExceptionFromInvocationTargetException() throws Exception {
        try {
            annotationLibrary.runKeyword("Failing Keyword", null);
            fail();
        } catch (RuntimeException e) {
           assertEquals("Assertion failed", e.getMessage());
        }
    }
}
