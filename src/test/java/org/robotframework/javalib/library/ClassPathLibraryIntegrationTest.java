package org.robotframework.javalib.library;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ClassPathLibraryIntegrationTest {
    private static ClassPathLibrary classPathLibrary;

    @BeforeAll
    public static void setUp() {
        classPathLibrary = new ClassPathLibrary("org/robotframework/**/**.class");
    }

    @Test
    public void testFindsKeywords() {
        List keywordNames = classPathLibrary.getKeywordNames();
        assertEquals(4, keywordNames.size());
        List expectedKeywordNames = Arrays.asList("recordingkeyword", "springkeyword", "emptykeyword", "conflictingkeyword");
        keywordNames.sort(Comparator.naturalOrder());
        expectedKeywordNames.sort(Comparator.naturalOrder());
        assertIterableEquals(expectedKeywordNames, keywordNames);
    }

    @Test
    public void testRunsKeyword() {
        Object result = classPathLibrary.runKeyword("Conflicting Keyword", null);
        assertEquals("Classpath Keyword", result.toString());
    }

    @Test
    public void testUsesProvidedPattern() {
        assertTrue(classPathLibrary.getKeywordNames().size() > 0);

        classPathLibrary = new ClassPathLibrary();
        classPathLibrary.setKeywordPattern("com/nonexistent/**.class");
        assertEquals(0, classPathLibrary.getKeywordNames().size());
    }

    @Test
    public void testThrowsExceptionIfKeywordPatternIsNotSet() {
        try {
            new ClassPathLibrary().getKeywordNames();
            fail("Expected IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            assertEquals("Keyword pattern must be set before calling getKeywordNames.", e.getMessage());
        }
    }
}
