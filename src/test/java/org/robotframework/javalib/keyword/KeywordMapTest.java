package org.robotframework.javalib.keyword;

import java.util.Arrays;

import org.robotframework.javalib.keyword.KeywordMap;

import junit.framework.TestCase;

public class KeywordMapTest extends TestCase {
    private KeywordMap map;
    private String keywordName = "My Keyword";
    private String keywordValue = "Value";

    protected void setUp() throws Exception {
        map = new KeywordMap();
    }
    
    public void testAddsKeywordsToMap() throws Exception {
        map.add(keywordName, keywordValue);
        assertEquals(1, map.size());
    }
    
    public void testGetsValueUsingKeywordNameAsKey() throws Exception {
        map.add(keywordName, keywordValue);
        assertEquals(keywordValue, map.get(keywordName));
    }
    
    public void testStoredKeywordNamesAreUnique() throws Exception {
        map.add(keywordName, "");
        try {
            map.add(keywordName, "");
            fail();
        } catch(IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public void testNullKeywordNamesAreNotAllowed() throws Exception {
        try {
            map.add(null, "");
            fail();
        } catch(IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public void testNullKeywordValuesAreNotAllowed() throws Exception {
        try {
            map.add("", null);
            fail();
        } catch(IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public void testNormalizesKeywordNames() throws Exception {
        map.add("Keyword Name", "");
        assertTrue(map.getUnderlyingMap().containsKey("keywordname"));
    }
    
    public void testCanReturnsArrayOfKeywordNames() throws Exception {
        map.add("First Keyword", "");
        map.add("Second Keyword", "");
        String[] keywordNames = map.getKeywordNames();
        assertTrue(Arrays.equals(new String[] { "firstkeyword", "secondkeyword" }, keywordNames));
    }
    
    public void testCanBeQueriedForContainedKeywords() throws Exception {
        map.add(keywordName, keywordValue);
        assertTrue(map.containsKeyword(keywordName));
    }
}
