package org.robotframework.javalib.beans.annotation;

import junit.framework.TestCase;

import org.robotframework.javalib.beans.common.IClassFilter;
import org.robotframework.javalib.keyword.AnnotatedKeywords;


public class AnnotationBasedKeywordFilterTest extends TestCase {
    private IClassFilter keywordFilter;

    protected void setUp() throws Exception {
        keywordFilter = new AnnotationBasedKeywordFilter();
    }

    public void testIdentifiesAnnotatedKeywordClasses() throws Exception {
        assertTrue(keywordFilter.accept(AnnotatedKeywords.class));
    }

    public void testIgnoresClassesThatAreNotAnnotated() throws Exception {
        assertFalse(keywordFilter.accept(Object.class));
    }
}
