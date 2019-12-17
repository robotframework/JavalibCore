package org.robotframework.javalib.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.robotframework.javalib.factory.KeywordFactory;
import org.robotframework.javalib.keyword.Keyword;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class KeywordFactoryBasedLibraryTest {
    private static KeywordFactoryBasedLibrary<Keyword> library;
    private static KeywordFactory keywordFactorySpy;
    private static Keyword keywordSpy;
    private static String keywordName = "Keyword Name";;

    @BeforeAll
    public static void setUp() {
        keywordSpy = spy(Keyword.class);
        keywordFactorySpy = spy(KeywordFactory.class);
        when(keywordFactorySpy.createKeyword(keywordName)).thenReturn(keywordSpy);

        library = new KeywordFactoryBasedLibrary<Keyword>() {
            protected KeywordFactory createKeywordFactory() {
                return keywordFactorySpy;
            }
        };
    }

    @Test
    public void testUsesKeywordFactoryToCreateInstanceOfKeyword() throws Exception {
        when(keywordSpy.execute(any(), any())).thenReturn(null);
        when(keywordFactorySpy.createKeyword(keywordName)).thenReturn(keywordSpy);

        library.runKeyword(keywordName, null);
    }

    @Test
    public void testGetsKeywordNamesFromFactory() throws Exception {
        when(keywordFactorySpy.getKeywordNames()).thenReturn(new ArrayList());
        library.getKeywordNames();
    }

    @Test
    public void testExecutesKeyword() throws Exception {
        List args = Arrays.asList(new Object[0]);
        when(keywordSpy.execute(args, null)).thenReturn(any());
        library.runKeyword(keywordName, args);
    }

    @Test
    public void testExecutionPassesKeywordReturnValue() throws Exception {
        String keywordReturnValue = "Return Value";
        when(keywordSpy.execute(null, null)).thenReturn(keywordReturnValue);
        assertEquals(keywordReturnValue, library.runKeyword(keywordName, null));
    }

    @Test
    public void testRunningAKeywordCreatesKeywordFactory() throws Exception {
        keywordFactorySpyBasedLibrary keywordFactorySpyBasedLibrary = new keywordFactorySpyBasedLibrary();
        keywordFactorySpyBasedLibrary.runKeyword(null, null);
        assertTrue(keywordFactorySpyBasedLibrary.keywordFactoryWasCreated);
    }

    @Test
    public void testGettingKeywordNamesCreatesKeywordFactory() throws Exception {
        keywordFactorySpyBasedLibrary keywordFactorySpyBasedLibrary = new keywordFactorySpyBasedLibrary();
        keywordFactorySpyBasedLibrary.getKeywordNames();
        assertTrue(keywordFactorySpyBasedLibrary.keywordFactoryWasCreated);
    }

    @Test
    public void testKeywordFactoryIsOnlyCreatedOnce() throws Exception {
        keywordFactorySpyBasedLibrary keywordFactorySpyBasedLibrary = new keywordFactorySpyBasedLibrary();
        keywordFactorySpyBasedLibrary.getKeywordNames();
        assertTrue(keywordFactorySpyBasedLibrary.keywordFactoryWasCreated);

        keywordFactorySpyBasedLibrary.keywordFactoryWasCreated = false;
        keywordFactorySpyBasedLibrary.getKeywordNames();
        assertFalse(keywordFactorySpyBasedLibrary.keywordFactoryWasCreated);

        keywordFactorySpyBasedLibrary.keywordFactoryWasCreated = false;
        keywordFactorySpyBasedLibrary.runKeyword(null, null);
        assertFalse(keywordFactorySpyBasedLibrary.keywordFactoryWasCreated);
    }

    @Test
    public void testDefaultClassLoaderIsThreadContextClassLoader() throws Exception {
        assertEquals(Thread.currentThread().getContextClassLoader(), library.getClassLoader());
    }

    private class keywordFactorySpyBasedLibrary extends KeywordFactoryBasedLibrary<Keyword> {
        boolean keywordFactoryWasCreated;

        protected KeywordFactory<Keyword> createKeywordFactory() {
            keywordFactoryWasCreated = true;
            return new KeywordFactory<Keyword>() {
                public Keyword createKeyword(String keywordName) {
                    return new Keyword() {
                        public Object execute(List arguments, Map kwargs) {
                            return null;
                        }

                        @Override
                        public List<String> getArgumentTypes() {
                            // TODO Auto-generated method stub
                            return null;
                        }
                    };
                }

                public List getKeywordNames() {
                    return new ArrayList();
                }
            };
        }
    }
}
