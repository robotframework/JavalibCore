package org.robotframework.javalib.beans.annotation;

import org.junit.jupiter.api.Test;
import org.robotframework.javalib.beans.common.IClassFilter;
import org.robotframework.javalib.keyword.AnnotatedKeywords;

import static org.junit.jupiter.api.Assertions.*;


public class AnnotationBasedKeywordFilterTest {
    private IClassFilter keywordFilter = new AnnotationBasedKeywordFilter();

    @Test
    public void testIdentifiesAnnotatedKeywordClasses() throws Exception {
        assertTrue(keywordFilter.accept(AnnotatedKeywords.class));
    }

    @Test
    public void testIgnoresClassesThatAreNotAnnotated() throws Exception {
        assertFalse(keywordFilter.accept(Object.class));
    }
}
