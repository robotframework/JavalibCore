package org.robotframework.javalib.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;


public class AnnotationLibraryMultipleKeywordsWithSameNameIntegrationTest {
    private static AnnotationLibrary annotationLibrary;
    private String keywordThatReturnsItsArguments = "keywordThatReturnsItsArguments";

    @BeforeAll
    public static void setUp() throws Exception {
    	List<String> searchPaths = new ArrayList<String>();
    	searchPaths.add("org/robotframework/**/keyword/**/**.class");
    	searchPaths.add("com/some/**/keyword/**/**.class");
        annotationLibrary = new AnnotationLibrary(searchPaths);
    }

    @Test
    public void testFindsAnnotatedKeywordsFromClassPath() {
        List keywordNames = annotationLibrary.getKeywordNames();
        List expectedKeywordNames = Arrays.asList("failingKeyword", "someKeyword", "overloaded",
        		keywordThatReturnsItsArguments, "keywordWithVariableArgumentCount", "variousArgs", "defaultValues",
                "keywordWithObjectArgument", "getSomeObject", "keywordWithNumericArguments",
        		"myFailingKeyword", "myKeywordThatReturnsItsArguments", "byteArrayTest", "defaultAndVarargs", "onlyVarargs",
                "useInt", "useInteger");
        keywordNames.sort(Comparator.naturalOrder());
        expectedKeywordNames.sort(Comparator.naturalOrder());
        assertIterableEquals(keywordNames, expectedKeywordNames);
    }

}
