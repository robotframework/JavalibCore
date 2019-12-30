package org.robotframework.javalib.library;

import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnnotationLibraryIntegrationTest {
    private static AnnotationLibrary annotationLibrary;
    private String keywordThatReturnsItsArguments = "keywordThatReturnsItsArguments";
    private String byteArrayTest = "byteArrayTest";

    @BeforeAll
    public static void setUp() {
        annotationLibrary = new AnnotationLibrary("org/robotframework/**/keyword/**/**.class");
    }

    @Test
    public void findsAnnotatedKeywordsFromClassPath() throws Exception {
        List keywordNames = annotationLibrary.getKeywordNames();
        List expectedKeywordNames = Arrays.asList("failingKeyword", "someKeyword", "overloaded",
                keywordThatReturnsItsArguments, "keywordWithVariableArgumentCount", "variousArgs", "defaultValues",
                "keywordWithObjectArgument", "getSomeObject", "keywordWithNumericArguments", byteArrayTest );
        keywordNames.sort(Comparator.naturalOrder());
        expectedKeywordNames.sort(Comparator.naturalOrder());
        assertIterableEquals(keywordNames, expectedKeywordNames);
    }

    @Test
    public void variousArgsKeywordRuns() {
        String keywordName = "variousArgs";
        List arguments = null;
        Map<String, Object> kwargs = Collections.singletonMap("arg", (Object)"world");
        Object executionResult = annotationLibrary.runKeyword(keywordName, arguments, kwargs);
    }

    @Test
    public void runsKeywords() throws Exception {
        String keywordArgument = "someArgument";
        Object executionResult = annotationLibrary.runKeyword(keywordThatReturnsItsArguments,
            Arrays.asList(keywordArgument));

        assertEquals(keywordArgument, executionResult);
    }

    @Test
    public void testOverloading() throws Exception {
        assertEquals(2, annotationLibrary.runKeyword("overloaded", Arrays.asList("one", 2)));
        assertEquals("one", annotationLibrary.runKeyword("overloaded", Arrays.asList("one")));
        assertEquals("3", annotationLibrary.runKeyword("overloaded", Arrays.asList("one", "two", "3")));
    }

    @Test
    public void testFindsKeywordDocumentation() throws Exception {
        String documentation = annotationLibrary.getKeywordDocumentation("someKeyword");
        assertEquals("Some documentation", documentation);
    }

    @Test
    public void testFindsKeywordArguments() throws Exception {
        List keywordArguments = annotationLibrary.getKeywordArguments("keywordThatReturnsItsArguments");
        assertIterableEquals(keywordArguments, Arrays.asList("arg"));
    }

    @Test
    public void testFindsKeywordArgumentsWithKeywordArgumentsAnnotation() throws Exception {
        List keywordArguments = annotationLibrary.getKeywordArguments("someKeyword");
        assertIterableEquals(keywordArguments, Arrays.asList("overridenArgumentName"));
    }

    @Test
    public void testExtractsInnerExceptionFromInvocationTargetException() throws Exception {
        try {
            annotationLibrary.runKeyword("Failing Keyword", null);
            fail();
        } catch (RuntimeException e) {
           assertEquals("Assertion failed", e.getMessage());
        }
    }

    @Test
    public void testByteArrayHandling() {
        String testString = "testString";
        annotationLibrary.runKeyword(byteArrayTest, Arrays.asList(testString, testString.getBytes()));
    }

    @Test
    public void testByteArrayHandlingResponse() {
        String testString = "testString";
        Object response = annotationLibrary.runKeyword(byteArrayTest, Arrays.asList(testString, testString.getBytes()));
        assertEquals(testString, new String((byte[]) response));
    }
}
