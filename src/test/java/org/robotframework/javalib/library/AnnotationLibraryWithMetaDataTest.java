package org.robotframework.javalib.library;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.factory.KeywordFactory;
import org.robotframework.javalib.keyword.DocumentedKeyword;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.javalib.util.ArrayUtil;


public class AnnotationLibraryWithMetaDataTest extends MockObjectTestCase {
    private String keywordName = "somekeyword";
    private String keywordDocumentation = "documentation";
    private AnnotationLibrary annotationLibrary;
    private String[] keywordArguments = new String[] { "someArgument" };

    @Override
    protected void setUp() throws Exception {
        final KeywordFactory<DocumentedKeyword> keywordFactory = createKeywordFactory();
        annotationLibrary = new AnnotationLibrary() {
            @Override
            protected KeywordFactory<DocumentedKeyword> createKeywordFactory() {
                return keywordFactory;
            }
        };
    }

    public void testGetsKeywordDocumentationFromKeywordFactory() throws Exception {
        assertEquals(keywordDocumentation, annotationLibrary.getKeywordDocumentation(keywordName));
    }

    public void testGetsKeywordArgumentsFromKeywordFactory() throws Exception {
        ArrayUtil.assertArraysEquals(keywordArguments, annotationLibrary.getKeywordArguments(keywordName));
    }

    private KeywordFactory<DocumentedKeyword> createKeywordFactory() {
        Mock documentedKeyword = mock(DocumentedKeyword.class);
        documentedKeyword.stubs().method("getDocumentation")
            .will(returnValue(keywordDocumentation));
        documentedKeyword.stubs().method("getArgumentNames")
            .will(returnValue(keywordArguments));

        Mock keywordFactory = mock(KeywordFactory.class);
        keywordFactory.expects(once()).method("createKeyword")
            .with(eq(keywordName))
            .will(returnValue(documentedKeyword.proxy()));

        return (KeywordFactory<DocumentedKeyword>) keywordFactory.proxy();
    }
}
