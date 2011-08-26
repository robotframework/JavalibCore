package org.robotframework.javalib.beans.common;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.beans.common.BasicKeywordFilter.Condition;
import org.robotframework.javalib.keyword.ArgumentCheckingKeyword;
import org.robotframework.javalib.keyword.CollisionKeyword;
import org.robotframework.javalib.keyword.Keyword;


public class BasicKeywordFilterTest extends MockObjectTestCase {
    private BasicKeywordFilter keywordFilter;

    @Override
    protected void setUp() throws Exception {
        keywordFilter = new BasicKeywordFilter();
    }

    public void testIgnoresAbstractKeywordClasses() throws Exception {
        assertFalse(keywordFilter.accept(ArgumentCheckingKeyword.class));
    }

    public void testIgnoresInterfaces() throws Exception {
        assertFalse(keywordFilter.accept(Keyword.class));
    }

    public void testIgnoresKeywordsWithoutDefaultConstructor() throws Exception {
        assertFalse(keywordFilter.accept(CollisionKeyword.class));
    }

    public void testUsesAddedConditions() throws Exception {
        Mock someCondition = mock(Condition.class);
        someCondition.expects(once()).method("check")
            .with(eq(getClass()))
            .will(returnValue(false));

        keywordFilter.addCondition((Condition) someCondition.proxy());
        assertFalse(keywordFilter.accept(getClass()));
    }
}
