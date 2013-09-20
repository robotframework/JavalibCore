package org.robotframework.javalib.factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.beans.annotation.IKeywordExtractor;
import org.robotframework.javalib.keyword.DocumentedKeyword;
import org.robotframework.javalib.library.AnnotationLibrary;

public class AnnotationKeywordFactoryTest extends MockObjectTestCase {
	private Object someKeywordBean = new Object();
	private Object anotherKeywordBean = new Object();
	private Object keyword1 = mock(DocumentedKeyword.class).proxy();
	private Object keyword2 = mock(DocumentedKeyword.class).proxy();
	private String[] expectedKeywordNames = new String[] { "keywordname1", "keywordname2", "keywordname3",
			"keywordname4" };
	private KeywordFactory<DocumentedKeyword> keywordFactory;
	private Mock keywordExtractor;
	private AnnotationLibrary library;

	private Map keywordBeans = new HashMap() {
		@Override
		public Collection values() {
			return new HashSet() {
				{
					add(someKeywordBean);
					add(anotherKeywordBean);
				}
			};
		}
	};

	@Override
	protected void setUp() throws Exception {
		library = new AnnotationLibrary();
		keywordExtractor = mock(IKeywordExtractor.class);
		keywordExtractor.expects(once()).method("extractKeywords").with(eq(library), eq(someKeywordBean), NOT_NULL)
				.will(returnValue(new HashMap() {
					{
						put("keywordname1", keyword1);
						put("keywordname2", keyword2);
					}
				}));

		keywordExtractor.expects(once()).method("extractKeywords").with(eq(library), eq(anotherKeywordBean), NOT_NULL)
				.will(returnValue(new HashMap() {
					{
						put("keywordname3", null);
						put("keywordname4", null);
					}
				}));

		keywordFactory = new AnnotationKeywordFactory(library, keywordBeans) {
			@Override
			IKeywordExtractor createKeywordExtractor() {
				return (IKeywordExtractor) keywordExtractor.proxy();
			}
		};
	}

	public void testExtractsKeywordNamesFromKeywordBeans() throws Exception {
		assertArraysHaveSameContent(expectedKeywordNames, keywordFactory.getKeywordNames());
	}

	public void testExtractsKeywordsFromKeywordBeansWithNormalizedName() throws Exception {
		String keywordName1 = "Keyword Name 1";
		String keywordName2 = "KEYWORD_NAME_2";
		assertEquals(keyword1, keywordFactory.createKeyword(keywordName1));
		assertEquals(keyword2, keywordFactory.createKeyword(keywordName2));
	}

	private void assertArraysHaveSameContent(String[] expected, String[] actual) {
		assertEquals(new HashSet(Arrays.asList(expected)), new HashSet(Arrays.asList(actual)));
	}
}
