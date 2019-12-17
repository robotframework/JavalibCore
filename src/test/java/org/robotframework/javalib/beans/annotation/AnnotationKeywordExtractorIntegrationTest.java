package org.robotframework.javalib.beans.annotation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.robotframework.javalib.keyword.AnnotatedKeywords;
import org.robotframework.javalib.keyword.DocumentedKeyword;
import org.robotframework.javalib.keyword.Keyword;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AnnotationKeywordExtractorIntegrationTest {
    private IKeywordExtractor<DocumentedKeyword> extractor = new AnnotationKeywordExtractor();
    private Map<String, DocumentedKeyword> extractedKeywords = extractor.extractKeywords(new AnnotatedKeywords());

    @Test
    public void testReturnsKeywordNamesInCamelCase() {
        assertTrue(extractedKeywords.keySet().contains("someKeyword"));
    }

    @Test
    public void testExtractsKeywordArguments() {
        DocumentedKeyword keywordThatReturnsItsArguments = extractedKeywords.get("keywordThatReturnsItsArguments");
        DocumentedKeyword someKeyword =extractedKeywords.get("someKeyword");
        assertIterableEquals(Arrays.asList("arg"), keywordThatReturnsItsArguments.getArgumentNames());
        assertIterableEquals(Arrays.asList("overridenArgumentName"), someKeyword.getArgumentNames());
    }

    @Test
    public void testExtractsKeywordsThatHandleVariableArgumentCount() {
        Keyword keyword = extractedKeywords.get("keywordWithVariableArgumentCount");

        assertLeftoverArgumentsAreCorrectlyGrouped(keyword, Arrays.asList("arg1", "arg2", "arg3", "arg4"));
        assertLeftoverArgumentsAreCorrectlyGrouped(keyword, Arrays.asList( "arg1", "arg2", "arg3"));
        assertLeftoverArgumentsAreCorrectlyGrouped(keyword, Arrays.asList( "arg1" ));
    }

    private void assertLeftoverArgumentsAreCorrectlyGrouped(Keyword keyword, List arguments) {
        List expected = arguments.subList(1, arguments.size());
        assertIterableEquals(expected, Arrays.asList((Object[])keyword.execute(arguments, null)));
    }
}
