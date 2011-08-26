package org.robotframework.javalib.factory;

import java.util.Arrays;

import org.robotframework.javalib.factory.CompositeKeywordFactory;
import org.robotframework.javalib.keyword.CollisionKeyword;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.keyword.KeywordMap;
import org.robotframework.javalib.mocks.KeywordFactoryWithOneKeyword;
import org.robotframework.javalib.mocks.RecordingKeyword;


import junit.framework.TestCase;

public class CompositeKeywordFactoryTest extends TestCase {
    private CompositeKeywordFactory keywordFactoryBundle;
    private KeywordFactoryWithOneKeyword[] factories;

    protected void setUp() throws Exception {
        keywordFactoryBundle = new CompositeKeywordFactory();
        factories = addTwoKeywordFactoriesEachWithOneKeyword(keywordFactoryBundle);
    }
    
    public void testCreatesKeywordsFromMultipleFactories() throws Exception {
        assertEquals(factories[0].keywordInstance, keywordFactoryBundle.createKeyword(factories[0].keywordName));
        assertEquals(factories[1].keywordInstance, keywordFactoryBundle.createKeyword(factories[1].keywordName));
    }
    
    public void testReturnsCombinedListOfKeywordNames() throws Exception {
        String[] keywordNames = keywordFactoryBundle.getKeywordNames();
        
        String keyword1 = KeywordMap.normalizeKeywordName(factories[0].keywordName);
        String keyword2 = KeywordMap.normalizeKeywordName(factories[1].keywordName);
        assertTrue(Arrays.equals(new String[] { keyword1, keyword2 }, keywordNames));
    }
    
    public void testOnlyUsesCorrectFactoryForCreation() throws Exception {
        keywordFactoryBundle.createKeyword(factories[0].keywordName);
        assertTrue(factoryWasUsedForCreatingKeyword(factories[0], factories[0].keywordName));
        assertFalse(factoryWasUsedForCreatingKeyword(factories[1], factories[0].keywordName));
    }

    public void testOnlyUsesCorrectFactoryForCreationInverse() throws Exception {
        keywordFactoryBundle.createKeyword(factories[1].keywordName);
        assertTrue(factoryWasUsedForCreatingKeyword(factories[1], factories[1].keywordName));
        assertFalse(factoryWasUsedForCreatingKeyword(factories[0], factories[1].keywordName));
    }
    
    public void testDoesntAddDuplicateKeywords() throws Exception {
        addFactoryWithCollidingKeyword();
        String[] keywordNames = keywordFactoryBundle.getKeywordNames();
        assertFirstKeywordExistsOnlyOnce(keywordNames);
    }
    
    public void testUsesCollisionKeywordInCaseOfCollision() throws Exception {
        KeywordFactoryWithOneKeyword factory = addFactoryWithCollidingKeyword();
        Keyword keyword = keywordFactoryBundle.createKeyword(factory.keywordName);
        assertEquals(CollisionKeyword.class, keyword.getClass());
    }
    
    private void assertFirstKeywordExistsOnlyOnce(String[] keywordNames) {
        int count = 0;
        for(int i = 0; i < keywordNames.length; i++) {
            if (keywordNames[i].equals(factories[0].keywordName)) {
                count++;
            }
            if (count > 1) {
                fail();
            }
        }
    }
    
    private KeywordFactoryWithOneKeyword addFactoryWithCollidingKeyword() {
        String keywordName = factories[0].keywordName;
        KeywordFactoryWithOneKeyword factory = createKeywordFactoryWithOneKeyword(keywordName);
        keywordFactoryBundle.addKeywordFactory(factory);
        return factory;
    }
    
    private boolean factoryWasUsedForCreatingKeyword(KeywordFactoryWithOneKeyword factory, String keywordName) {
        if (factory.createKeywordArgument == null) {
            return false;
        }
        return factory.createKeywordArgument.equals(keywordName);
    }

    private KeywordFactoryWithOneKeyword[] addTwoKeywordFactoriesEachWithOneKeyword(CompositeKeywordFactory keywordFactoryBundle2) {
        String firstKeywordName = "First Fake Keyword";
        KeywordFactoryWithOneKeyword factory1 = createKeywordFactoryWithOneKeyword(firstKeywordName);
        keywordFactoryBundle.addKeywordFactory(factory1);
        String secondKeywordName = "Second Fake Keyword";
        KeywordFactoryWithOneKeyword factory2 = createKeywordFactoryWithOneKeyword(secondKeywordName);
        keywordFactoryBundle.addKeywordFactory(factory2);
        
        return new KeywordFactoryWithOneKeyword[] { factory1, factory2 };
    }

    private KeywordFactoryWithOneKeyword createKeywordFactoryWithOneKeyword(String keywordname) {
        RecordingKeyword fakeKeyword = new RecordingKeyword();
        fakeKeyword.returnValue = keywordname + " Return Value";
        return new KeywordFactoryWithOneKeyword(keywordname, fakeKeyword);
    }
}

