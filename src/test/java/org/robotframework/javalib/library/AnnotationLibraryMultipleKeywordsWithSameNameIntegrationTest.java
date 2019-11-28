package org.robotframework.javalib.library;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.robotframework.javalib.util.ArrayUtil;


public class AnnotationLibraryMultipleKeywordsWithSameNameIntegrationTest extends TestCase {
    private AnnotationLibrary annotationLibrary;
    private String keywordThatReturnsItsArguments = "keywordThatReturnsItsArguments";

    @Override
    protected void setUp() throws Exception {
    	List<String> searchPaths = new ArrayList<String>();
    	searchPaths.add("org/robotframework/**/keyword/**/**.class"); 
    	searchPaths.add("com/some/**/keyword/**/**.class");
        annotationLibrary = new AnnotationLibrary(searchPaths);
    }

    public void testFindsAnnotatedKeywordsFromClassPath() throws Exception {
        String[] keywordNames = annotationLibrary.getKeywordNames().toArray(new String[0]);
        String[] expectedKeywordNames = new String[] { "failingKeyword", "someKeyword", "overloaded",
        		keywordThatReturnsItsArguments, "keywordWithVariableArgumentCount", "keywordWithObjectArgument",
                "getSomeObject", "keywordWithNumericArguments",
        		"myFailingKeyword", "myKeywordThatReturnsItsArguments"};
        ArrayUtil.assertArraysContainSame(expectedKeywordNames, keywordNames);
    }

}
