package org.robotframework.javalib.beans.classpath;

import org.junit.jupiter.api.Test;
import org.robotframework.javalib.beans.common.IClassFilter;
import org.robotframework.javalib.keyword.EmptyKeyword;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterfaceBasedKeywordFilterTest {
    private IClassFilter keywordFilter = new InterfaceBasedKeywordFilter();;

    @Test
    public void testIgnoresClassesThatAreNotKeywords() {
        assertFalse(keywordFilter.accept(Object.class));
    }

    @Test
    public void testIdentifiesKeywordClass() {
        assertTrue(keywordFilter.accept(EmptyKeyword.class));
    }
}
