package org.robotframework.javalib.beans.annotation;

import java.util.Arrays;
import java.util.Map;

import junit.framework.TestCase;

import org.robotframework.javalib.keyword.AnnotatedKeywords;
import org.robotframework.javalib.keyword.DocumentedKeyword;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.util.ArrayUtil;


public class AnnotationKeywordExtractorIntegrationTest extends TestCase {
    private IKeywordExtractor<DocumentedKeyword> extractor = new AnnotationKeywordExtractor();
    private Map<String, DocumentedKeyword> extractedKeywords;

    @Override
    protected void setUp() throws Exception {
        extractedKeywords = extractor.extractKeywords(new AnnotatedKeywords());
    }

    public void testReturnsKeywordNamesInCamelCase() throws Exception {
        assertTrue(extractedKeywords.keySet().contains("someKeyword"));
    }

    public void testExtractsKeywordArguments() throws Exception {
        DocumentedKeyword keywordThatReturnsItsArguments = (DocumentedKeyword) extractedKeywords.get("keywordThatReturnsItsArguments");
        DocumentedKeyword someKeyword = (DocumentedKeyword) extractedKeywords.get("someKeyword");
        assertArraysEquals(new String[] { "arg" }, keywordThatReturnsItsArguments.getArgumentNames());
        assertArraysEquals(new String[] { "overridenArgumentName" }, someKeyword.getArgumentNames());
    }

    public void testExtractsKeywordsThatHandleVariableArgumentCount() throws Exception {
        Keyword keyword = (Keyword) extractedKeywords.get("keywordWithVariableArgumentCount");

        assertLeftoverArgumentsAreCorrectlyGrouped(keyword, new String[] { "arg1", "arg2", "arg3", "arg4" });
        assertLeftoverArgumentsAreCorrectlyGrouped(keyword, new String[] { "arg1", "arg2", "arg3" });
        assertLeftoverArgumentsAreCorrectlyGrouped(keyword, new String[] { "arg1" });
    }

    private void assertArraysEquals(String[] expected, String[] actual) {
        assertTrue(Arrays.equals(expected, actual));
    }
    
    private void assertLeftoverArgumentsAreCorrectlyGrouped(Keyword keyword, String[] arguments) {
        Object[] expected = ArrayUtil.copyOfRange(arguments, 1, arguments.length);
        ArrayUtil.assertArraysEquals(expected, (Object[]) keyword.execute(arguments));
    }
}
