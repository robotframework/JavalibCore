package org.robotframework.javalib.beans.annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import junit.framework.TestCase;

import org.robotframework.javalib.keyword.AnnotatedKeywords;
import org.robotframework.javalib.keyword.DocumentedKeyword;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.javalib.util.ArrayUtil;

public class AnnotationKeywordExtractorIntegrationTest extends TestCase {
	private IKeywordExtractor<DocumentedKeyword> extractor = new AnnotationKeywordExtractor();
	private Map<String, DocumentedKeyword> extractedKeywords;
	private AnnotatedKeywords annotatedKeywords;
	private AnnotationLibrary library;

	@Override
	protected void setUp() throws Exception {
		library = new AnnotationLibrary();
		annotatedKeywords = new AnnotatedKeywords();
		Collection<Object> beans = new ArrayList<Object>();
		beans.add(annotatedKeywords);
		extractedKeywords = extractor.extractKeywords(library, annotatedKeywords, beans);
	}

	public void testAutowire() throws Exception {
		assertTrue(annotatedKeywords.getAnnotatedKeywords() == annotatedKeywords);
		assertTrue(annotatedKeywords.getLibrary() == library);
	}

	public void testReturnsKeywordNamesInCamelCase() throws Exception {
		assertTrue(extractedKeywords.keySet().contains("someKeyword"));
	}

	public void testExtractsKeywordArguments() throws Exception {
		DocumentedKeyword keywordThatReturnsItsArguments = (DocumentedKeyword) extractedKeywords
				.get("keywordThatReturnsItsArguments");
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
