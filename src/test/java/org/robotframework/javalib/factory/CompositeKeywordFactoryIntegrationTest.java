package org.robotframework.javalib.factory;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.keyword.ConflictingKeyword;
import org.robotframework.javalib.keyword.EmptyKeyword;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.keyword.KeywordNameCollisionException;


public class CompositeKeywordFactoryIntegrationTest extends MockObjectTestCase {
    private String keywordName = "Keyword Name";
    private EmptyKeyword emptyKeyword;
    private KeywordFactory mockKeywordFactory1;
    private ConflictingKeyword conflictingKeyword;
    private KeywordFactory mockKeywordFactory2;
    private CompositeKeywordFactory compositeKeywordFactory;

    protected void setUp() throws Exception {
        emptyKeyword = new EmptyKeyword();
        mockKeywordFactory1 = createMockKeywordFactoryWithOneKeyword(keywordName, emptyKeyword);

        conflictingKeyword = new ConflictingKeyword();
        mockKeywordFactory2 = createMockKeywordFactoryWithOneKeyword(keywordName, conflictingKeyword);

        compositeKeywordFactory = createCompositeKeywordFactory(new KeywordFactory[] { mockKeywordFactory1, mockKeywordFactory2 });
    }

    public void testCreatesCollisionKeywordInCaseOfCollidingKeywordNames() throws Exception {
        runKeywordAndExpectException();
    }

    public void testThirdCollidingKeywordIsIgnored() throws Exception {
        Mock mockKeyword = mock(Keyword.class);

        KeywordFactory thirdKeywordFactory = createMockKeywordFactoryWithOneKeyword(keywordName, (Keyword) mockKeyword.proxy());
        compositeKeywordFactory.addKeywordFactory(thirdKeywordFactory);

        runKeywordAndExpectException();
    }

    private void runKeywordAndExpectException() {
        Keyword keyword = compositeKeywordFactory.createKeyword(keywordName);
        try {
            keyword.execute(null);
            fail();
        } catch(KeywordNameCollisionException e) {
            assertEquals("Two keywords with same name not allowed. Alternative implementations available from " + emptyKeyword.getClass() + " and " + conflictingKeyword.getClass() + ".", e.getMessage());
        }
    }

    private CompositeKeywordFactory createCompositeKeywordFactory(KeywordFactory[] keywordFactories) {
        CompositeKeywordFactory compositeKeywordFactory = new CompositeKeywordFactory();
        for (int i = 0; i < keywordFactories.length; i++) {
            compositeKeywordFactory.addKeywordFactory(keywordFactories[i]);
        }
        return compositeKeywordFactory;
    }

    private KeywordFactory createMockKeywordFactoryWithOneKeyword(String keywordName, Keyword keyword) {
        Mock mockKeywordFactory = mock(KeywordFactory.class);
        mockKeywordFactory.stubs().method("getKeywordNames")
            .will(returnValue(new String[] { keywordName }));

        mockKeywordFactory.stubs().method("createKeyword")
            .with(eq(keywordName))
            .will(returnValue(keyword));
        return (KeywordFactory) mockKeywordFactory.proxy();
    }
}
