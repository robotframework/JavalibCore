package org.robotframework.javalib.keyword;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KeywordMapTest {
    private KeywordMap map;
    private String keywordName = "My Keyword";
    private String keywordValue = "Value";

    @BeforeEach
    protected void setUp() {
        map = new KeywordMap();
    }

    @Test
    public void testAddsKeywordsToMap() {
        map.add(keywordName, keywordValue);
        assertEquals(1, map.size());
    }

    @Test
    public void testGetsValueUsingKeywordNameAsKey() {
        map.add(keywordName, keywordValue);
        assertEquals(keywordValue, map.get(keywordName));
    }

    @Test
    public void testStoredKeywordNamesAreUnique() {
        map.add(keywordName, "");
        assertThrows(IllegalArgumentException.class, () -> map.add(keywordName, ""));
    }

    @Test
    public void testNullKeywordNamesAreNotAllowed() {
        assertThrows(IllegalArgumentException.class, () -> map.add(null, ""));
    }

    @Test
    public void testNullKeywordValuesAreNotAllowed() {
        assertThrows(IllegalArgumentException.class, () -> map.add("", null));
    }

    @Test
    public void testNormalizesKeywordNames() {
        map.add("Keyword Name", "");
        assertTrue(map.getUnderlyingMap().containsKey("keywordname"));
    }

    @Test
    public void testCanReturnsArrayOfKeywordNames() {
        map.add("First Keyword", "");
        map.add("Second Keyword", "");
        String[] keywordNames = map.getKeywordNames().toArray(new String[0]);
        assertTrue(Arrays.equals(new String[] { "firstkeyword", "secondkeyword" }, keywordNames));
    }

    @Test
    public void testCanBeQueriedForContainedKeywords() {
        map.add(keywordName, keywordValue);
        assertTrue(map.containsKeyword(keywordName));
    }
}
