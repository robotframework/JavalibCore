package org.robotframework.javalib.beans.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.keyword.DocumentedKeyword;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.javalib.util.ArrayUtil;

public class AnnotationKeywordExtractorTest extends MockObjectTestCase {
	private boolean keywordWasCalled = false;
	private String keywordWithoutArgumentsExecutionResult = "keyword1ExecutionResult";
	private String keywordWithArgumentsExecutionResult = "keyword2ExecutionResult";

	private Map extractedKeywords;
	private DocumentedKeyword keywordWithArguments;
	private DocumentedKeyword keywordWithoutArguments;
	private DocumentedKeyword keywordWithoutReturnValue;
	private IKeywordExtractor extractor;

	@Override
	protected void setUp() throws Exception {
		AnnotationLibrary library = new AnnotationLibrary();
		MyKeywordsBean myKeywordsBean = new MyKeywordsBean();
		Collection<Object> beans = new ArrayList<Object>();
		beans.add(myKeywordsBean);
		extractor = new AnnotationKeywordExtractor();
		extractedKeywords = extractor.extractKeywords(library, myKeywordsBean, beans);
		keywordWithArguments = (DocumentedKeyword) extractedKeywords.get("keywordWithArguments");
		keywordWithoutArguments = (DocumentedKeyword) extractedKeywords.get("keywordWithoutArguments");
		keywordWithoutReturnValue = (DocumentedKeyword) extractedKeywords.get("keywordWithoutReturnValue");
	}

	public void testExtractsCorrectNumberOfKeywordsFromKeywordBean() throws Exception {
		assertEquals(expectedKeywordCount(), extractedKeywords.size());
	}

	public void testExtractsKeywordsWithReturnValue() throws Exception {
		assertEquals(keywordWithoutArgumentsExecutionResult, keywordWithoutArguments.execute(null));
	}

	public void testExtractsKeywordsWithArguments() throws Exception {
		String keywordArgument = "someArgument";
		assertEquals(keywordWithArgumentsExecutionResult + keywordArgument,
				keywordWithArguments.execute(new String[] { keywordArgument }));
	}

	public void testExtractsKeywordsWithoutReturnValue() throws Exception {
		assertNull(keywordWithoutReturnValue.execute(null));
		assertTrue(keywordWasCalled);
	}

	public void testExtractsKeywordDocumentation() throws Exception {
		assertEquals("This is a keyword with arguments", keywordWithArguments.getDocumentation());
		assertEquals("This is a keyword without arguments", keywordWithoutArguments.getDocumentation());
		assertEquals("This is a keyword without return value", keywordWithoutReturnValue.getDocumentation());
	}

	public void testExtractsKeywordArguments() throws Exception {
		ArrayUtil.assertArraysEquals(new String[] { "overridenArgumentName" }, keywordWithArguments.getArgumentNames());
	}

	private int expectedKeywordCount() {
		Method[] methods = MyKeywordsBean.class.getMethods();
		int keywordCount = 0;
		for (Method method : methods) {
			if (method.isAnnotationPresent(RobotKeyword.class)) {
				++keywordCount;
			}
		}
		return keywordCount;
	}

	public class MyKeywordsBean {
		@RobotKeyword("This is a keyword without arguments")
		public Object keywordWithoutArguments() {
			return keywordWithoutArgumentsExecutionResult;
		}

		@ArgumentNames({ "overridenArgumentName" })
		@RobotKeyword("This is a keyword with arguments")
		public Object keywordWithArguments(String argument) {
			return keywordWithArgumentsExecutionResult + argument;
		}

		@RobotKeyword("This is a keyword without return value")
		public void keywordWithoutReturnValue() {
			keywordWasCalled = true;
		}

		@SuppressWarnings("unused")
		@RobotKeyword
		private void annotatedPrivateMethod() {
		}

		@SuppressWarnings("unused")
		private void notAKeyword() {
		}
	}
}
