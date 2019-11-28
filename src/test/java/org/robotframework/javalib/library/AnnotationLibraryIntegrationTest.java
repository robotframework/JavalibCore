package org.robotframework.javalib.library;

import junit.framework.TestCase;

import java.util.Arrays;

import org.robotframework.javalib.util.ArrayUtil;

public class AnnotationLibraryIntegrationTest extends TestCase {
    private AnnotationLibrary annotationLibrary;
    private String keywordThatReturnsItsArguments = "keywordThatReturnsItsArguments";

    @Override
    protected void setUp() throws Exception {
        annotationLibrary = new AnnotationLibrary("org/robotframework/**/keyword/**/**.class");
    }

    public void testFindsAnnotatedKeywordsFromClassPath() throws Exception {
        String[] keywordNames = annotationLibrary.getKeywordNames().toArray(new String[0]);
        String[] expectedKeywordNames = new String[] { "failingKeyword", "someKeyword", "overloaded", keywordThatReturnsItsArguments,
                "keywordWithVariableArgumentCount", "keywordWithObjectArgument", "getSomeObject", "keywordWithNumericArguments" };

        ArrayUtil.assertArraysContainSame(expectedKeywordNames, keywordNames);
    }

    public void testRunsKeywords() throws Exception {
        String keywordArgument = "someArgument";
        Object executionResult = annotationLibrary.runKeyword(keywordThatReturnsItsArguments,
            Arrays.asList(keywordArgument));

        assertEquals(keywordArgument, executionResult);
    }

    public void testOverloading() throws Exception {
        assertEquals(2, annotationLibrary.runKeyword("overloaded", Arrays.asList("one", "2")));
        assertEquals("one", annotationLibrary.runKeyword("overloaded", Arrays.asList("one")));
        assertEquals("3", annotationLibrary.runKeyword("overloaded", Arrays.asList("one", "two", "3")));
    }

    public void testOverloadingWithWrongNumberOfArguments() throws Exception {
       try{
           annotationLibrary.runKeyword("overloaded", Arrays.asList());
           fail();
       } catch (RuntimeException expected) {}
       try{
           annotationLibrary.runKeyword("overloaded", Arrays.asList(1, 2, 3, 4));
           fail();
       } catch (RuntimeException expected) {}
    }

    public void testFindsKeywordDocumentation() throws Exception {
        String documentation = annotationLibrary.getKeywordDocumentation("someKeyword");
        assertEquals("Some documentation", documentation);
    }

    public void testFindsKeywordArguments() throws Exception {
        String[] keywordArguments = annotationLibrary.getKeywordArguments("keywordThatReturnsItsArguments").toArray(new String[0]);
        ArrayUtil.assertArraysEquals(new String[] { "arg" }, keywordArguments);
    }

    public void testFindsKeywordArgumentsWithKeywordArgumentsAnnotation() throws Exception {
        String[] keywordArguments = annotationLibrary.getKeywordArguments("someKeyword").toArray(new String[0]);
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
