package org.robotframework.javalib.library;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.robotframework.javalib.beans.annotation.KeywordBeanLoader;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;


public class AnnotationLibraryTest {
    private static AnnotationLibrary annotationLibrary;
    private static String keywordPattern = "somePattern";
    private static KeywordBeanLoader beanLoaderAtInitialization;
    private static KeywordBeanLoader beanLoaderAfterSettingKeywordPattern;

    @BeforeAll
    public static void setUp() throws Exception {
        annotationLibrary = new AnnotationLibrary();
        beanLoaderAtInitialization = extractBeanLoaderFromAnnotationLibrary();
        annotationLibrary.addKeywordPattern(keywordPattern);
        beanLoaderAfterSettingKeywordPattern = extractBeanLoaderFromAnnotationLibrary();
    }

    @Test
    public void testThrowsExceptionIfKeywordPatternIsNotSet() {

        try {
            new AnnotationLibrary().getKeywordNames();
            fail("Expected IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            assertEquals("Keyword pattern must be set before calling getKeywordNames.", e.getMessage());
        }
    }

    @Test
    public void testCreatesNewBeanLoaderWhenKeywordPatternSet() {
        assertNotSame(beanLoaderAtInitialization, beanLoaderAfterSettingKeywordPattern);
    }

    @Test
    public void testSetsKeywordPatternToBeanLoader() throws IllegalAccessException {
        String extractedKeywordPattern = extractKeywordPatternFrom(beanLoaderAfterSettingKeywordPattern);
        assertEquals(keywordPattern, extractedKeywordPattern);
    }

    private String extractKeywordPatternFrom(KeywordBeanLoader beanLoader) throws IllegalAccessException {
        for (Field f: fields(beanLoader)) {
            if (f.getName().equals("keywordPattern")) {
                f.setAccessible(true);
                return (String) f.get(beanLoader);
            }
        }
        return null;
    }

    private static Field[] fields(KeywordBeanLoader beanLoader) {
        return beanLoader.getClass().getDeclaredFields();
    }

    private static KeywordBeanLoader extractBeanLoaderFromAnnotationLibrary() {
    	try {
    		return (KeywordBeanLoader) annotationLibrary.beanLoaders.get(0);
    	} catch (IndexOutOfBoundsException e){
    		return null;
    	}
    }
}
