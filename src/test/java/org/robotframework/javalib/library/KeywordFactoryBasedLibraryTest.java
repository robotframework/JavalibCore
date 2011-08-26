package org.robotframework.javalib.library;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.factory.KeywordFactory;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.library.KeywordFactoryBasedLibrary;


public class KeywordFactoryBasedLibraryTest extends MockObjectTestCase {
    private KeywordFactoryBasedLibrary<Keyword> library;
    private Mock mockKeywordFactory;
    private Mock mockKeyword;
    private String keywordName = "Keyword Name";;

    protected void setUp() throws Exception {
        mockKeyword = mock(Keyword.class);
        mockKeywordFactory = mock(KeywordFactory.class);

        mockKeywordFactory.stubs().method("createKeyword")
            .with(eq(keywordName))
            .will(returnValue(mockKeyword.proxy()));

        library = new KeywordFactoryBasedLibrary<Keyword>() {
            protected KeywordFactory createKeywordFactory() {
                return (KeywordFactory) mockKeywordFactory.proxy();
            }
        };
    }

    public void testUsesKeywordFactoryToCreateInstanceOfKeyword() throws Exception {
        mockKeyword.stubs().method("execute");

        mockKeywordFactory.reset();
        mockKeywordFactory.expects(once()).method("createKeyword")
            .with(eq(keywordName))
            .will(returnValue(mockKeyword.proxy()));
        library.runKeyword(keywordName, null);
    }

    public void testGetsKeywordNamesFromFactory() throws Exception {
        mockKeywordFactory.expects(once()).method("getKeywordNames")
            .will(returnValue(new String[0]));

        library.getKeywordNames();
    }

    public void testExecutesKeyword() throws Exception {
        Object[] args = new Object[0];
        mockKeyword.stubs().method("execute")
            .with(eq(args));

        library.runKeyword(keywordName, args);
    }

    public void testExecutionPassesKeywordReturnValue() throws Exception {
        String keywordReturnValue = "Return Value";
        mockKeyword.stubs().method("execute")
            .will(returnValue(keywordReturnValue));

        assertEquals(keywordReturnValue, library.runKeyword(keywordName, null));
    }

    public void testRunningAKeywordCreatesKeywordFactory() throws Exception {
        MockKeywordFactoryBasedLibrary mockKeywordFactoryBasedLibrary = new MockKeywordFactoryBasedLibrary();
        mockKeywordFactoryBasedLibrary.runKeyword(null, null);
        assertTrue(mockKeywordFactoryBasedLibrary.keywordFactoryWasCreated);
    }

    public void testGettingKeywordNamesCreatesKeywordFactory() throws Exception {
        MockKeywordFactoryBasedLibrary mockKeywordFactoryBasedLibrary = new MockKeywordFactoryBasedLibrary();
        mockKeywordFactoryBasedLibrary.getKeywordNames();
        assertTrue(mockKeywordFactoryBasedLibrary.keywordFactoryWasCreated);
    }

    public void testKeywordFactoryIsOnlyCreatedOnce() throws Exception {
        MockKeywordFactoryBasedLibrary mockKeywordFactoryBasedLibrary = new MockKeywordFactoryBasedLibrary();
        mockKeywordFactoryBasedLibrary.getKeywordNames();
        assertTrue(mockKeywordFactoryBasedLibrary.keywordFactoryWasCreated);

        mockKeywordFactoryBasedLibrary.keywordFactoryWasCreated = false;
        mockKeywordFactoryBasedLibrary.getKeywordNames();
        assertFalse(mockKeywordFactoryBasedLibrary.keywordFactoryWasCreated);

        mockKeywordFactoryBasedLibrary.keywordFactoryWasCreated = false;
        mockKeywordFactoryBasedLibrary.runKeyword(null, null);
        assertFalse(mockKeywordFactoryBasedLibrary.keywordFactoryWasCreated);
    }

    public void testDefaultClassLoaderIsThreadContextClassLoader() throws Exception {
        assertEquals(Thread.currentThread().getContextClassLoader(), library.getClassLoader());
    }

    private class MockKeywordFactoryBasedLibrary extends KeywordFactoryBasedLibrary<Keyword> {
        boolean keywordFactoryWasCreated;

        protected KeywordFactory<Keyword> createKeywordFactory() {
            keywordFactoryWasCreated = true;
            return new KeywordFactory<Keyword>() {
                public Keyword createKeyword(String keywordName) {
                    return new Keyword() {
                        public Object execute(Object[] arguments) {
                            return null;
                        }
                    };
                }

                public String[] getKeywordNames() {
                    return new String[0];
                }
            };
        }
    }
}