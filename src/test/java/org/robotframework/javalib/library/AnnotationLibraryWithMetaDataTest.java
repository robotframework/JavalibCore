package org.robotframework.javalib.library;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.robotframework.javalib.factory.KeywordFactory;
import org.robotframework.javalib.keyword.DocumentedKeyword;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class AnnotationLibraryWithMetaDataTest {
    private static String keywordName = "somekeyword";
    private static String keywordDocumentation = "documentation";
    private static AnnotationLibrary annotationLibrary;
    private static List keywordArguments = Arrays.asList("someArgument");

    @BeforeAll
    public static void setUp() {
        final KeywordFactory<DocumentedKeyword> keywordFactory = createKeywordFactory();
        annotationLibrary = new AnnotationLibrary() {
            @Override
            protected KeywordFactory<DocumentedKeyword> createKeywordFactory() {
                return keywordFactory;
            }
        };
    }

    @Test
    public void testGetsKeywordDocumentationFromKeywordFactory() {
        assertEquals(keywordDocumentation, annotationLibrary.getKeywordDocumentation(keywordName));
    }

    @Test
    public void testGetsKeywordArgumentsFromKeywordFactory() {
        assertIterableEquals(keywordArguments, annotationLibrary.getKeywordArguments(keywordName));
    }

    private static KeywordFactory<DocumentedKeyword> createKeywordFactory() {
        DocumentedKeyword documentedKeywordSpy = Mockito.spy(DocumentedKeyword.class);
        when(documentedKeywordSpy.getArgumentNames()).thenReturn(keywordArguments);
        when(documentedKeywordSpy.getDocumentation()).thenReturn(keywordDocumentation);

        KeywordFactory keywordFactorySpy = Mockito.spy(KeywordFactory.class);
        when(keywordFactorySpy.createKeyword(keywordName)).thenReturn(documentedKeywordSpy);
        return keywordFactorySpy;
    }
}
