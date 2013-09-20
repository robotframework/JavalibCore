package org.robotframework.javalib.beans.classpath;

import junit.framework.TestCase;

import org.robotframework.javalib.beans.common.IClassFilter;
import org.robotframework.javalib.keyword.EmptyKeyword;


public class InterfaceBasedKeywordFilterTest extends TestCase {
    private IClassFilter keywordFilter;

    protected void setUp() throws Exception {
        keywordFilter = new InterfaceBasedKeywordFilter();
    }

    public void testIgnoresClassesThatAreNotKeywords() throws Exception {
        assertFalse(keywordFilter.accept(Object.class));
    }

    public void testIdentifiesKeywordClass() throws Exception {
        assertTrue(keywordFilter.accept(EmptyKeyword.class));
    }
}
