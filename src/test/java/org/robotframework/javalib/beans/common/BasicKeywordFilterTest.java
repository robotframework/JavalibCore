package org.robotframework.javalib.beans.common;

import org.junit.jupiter.api.Test;
import org.robotframework.javalib.beans.common.BasicKeywordFilter.Condition;
import org.robotframework.javalib.keyword.CollisionKeyword;
import org.robotframework.javalib.keyword.Keyword;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class BasicKeywordFilterTest {
    private BasicKeywordFilter keywordFilter = new BasicKeywordFilter();

    @Test
    public void testIgnoresInterfaces() throws Exception {
        assertFalse(keywordFilter.accept(Keyword.class));
    }

    @Test
    public void testIgnoresKeywordsWithoutDefaultConstructor() throws Exception {
        assertFalse(keywordFilter.accept(CollisionKeyword.class));
    }

    @Test
    public void testUsesAddedConditions() throws Exception {
        Condition conditionSpy = spy(Condition.class);
        when(conditionSpy.check(getClass())).thenReturn(false);
        keywordFilter.addCondition(conditionSpy);
        assertFalse(keywordFilter.accept(getClass()));
    }
}
