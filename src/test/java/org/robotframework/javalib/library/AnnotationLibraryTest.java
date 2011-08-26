package org.robotframework.javalib.library;

import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.beans.annotation.KeywordBeanLoader;

import java.lang.reflect.Field;


public class AnnotationLibraryTest extends MockObjectTestCase {
    private AnnotationLibrary annotationLibrary;
    private String keywordPattern = "somePattern";
    private KeywordBeanLoader beanLoaderAtInitialization;
    private KeywordBeanLoader beanLoaderAfterSettingKeywordPattern;

    protected void setUp() throws Exception {
        annotationLibrary = new AnnotationLibrary();
        beanLoaderAtInitialization = extractBeanLoaderFromAnnotationLibrary();
        annotationLibrary.addKeywordPattern(keywordPattern);
        beanLoaderAfterSettingKeywordPattern = extractBeanLoaderFromAnnotationLibrary();
    }

    public void testThrowsExceptionIfKeywordPatternIsNotSet() throws Exception {
        try {
            new AnnotationLibrary().getKeywordNames();
            fail("Expected IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            assertEquals("Keyword pattern must be set before calling getKeywordNames.", e.getMessage());
        }
    }

    public void testCreatesNewBeanLoaderWhenKeywordPatternSet() throws Exception {
        assertNotSame(beanLoaderAtInitialization, beanLoaderAfterSettingKeywordPattern);
    }

    public void testSetsKeywordPatternToBeanLoader() throws Exception {
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

    private Field[] fields(KeywordBeanLoader beanLoader) {
        return beanLoader.getClass().getDeclaredFields();
    }

    private KeywordBeanLoader extractBeanLoaderFromAnnotationLibrary() {
    	try {
    		return (KeywordBeanLoader) annotationLibrary.beanLoaders.get(0);
    	} catch (IndexOutOfBoundsException e){
    		return null;
    	}
    }
}
