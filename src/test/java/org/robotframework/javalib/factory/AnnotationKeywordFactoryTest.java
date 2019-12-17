package org.robotframework.javalib.factory;

import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.robotframework.javalib.beans.annotation.IKeywordExtractor;
import org.robotframework.javalib.keyword.DocumentedKeyword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;


public class AnnotationKeywordFactoryTest {
    private static Object someKeywordBean = new Object();
    private static Object anotherKeywordBean = new Object();
    private static DocumentedKeyword keyword1 = mock(DocumentedKeyword.class);
    private static DocumentedKeyword keyword2 = mock(DocumentedKeyword.class);
    private List expectedKeywordNames = Arrays.asList("keywordname1", "keywordname2", "keywordname3", "keywordname4" );
    private static KeywordFactory<DocumentedKeyword> keywordFactory;
    private static IKeywordExtractor keywordExtractor;

    private static Map keywordBeans = new HashMap() {
        @Override
        public Collection values() {
            return new HashSet() {{ add(someKeywordBean); add(anotherKeywordBean); }};
        }
    };

    @BeforeAll
    public static void setUp() {
        keywordExtractor = spy(IKeywordExtractor.class);
        when(keywordExtractor.extractKeywords(someKeywordBean)).thenReturn(new HashMap() {{
            put("keywordname1", keyword1);
            put("keywordname2", keyword2);
        }});

        when(keywordExtractor.extractKeywords(anotherKeywordBean)).thenReturn(new HashMap() {{
            put("keywordname3", null);
            put("keywordname4", null);
        }});

        keywordFactory = new AnnotationKeywordFactory(keywordBeans) {
            @Override
            IKeywordExtractor createKeywordExtractor() {
                return keywordExtractor;
            }
        };
    }

    @Test
    public void testExtractsKeywordNamesFromKeywordBeans() {
        List keywordNames = keywordFactory.getKeywordNames();
        keywordNames.sort(Comparator.naturalOrder());
        assertIterableEquals(expectedKeywordNames, keywordNames);
    }

    @Test
    public void testExtractsKeywordsFromKeywordBeansWithNormalizedName() {
        String keywordName1 = "Keyword Name 1";
        String keywordName2 = "KEYWORD_NAME_2";
        assertEquals(keyword1, keywordFactory.createKeyword(keywordName1));
        assertEquals(keyword2, keywordFactory.createKeyword(keywordName2));
    }
}

