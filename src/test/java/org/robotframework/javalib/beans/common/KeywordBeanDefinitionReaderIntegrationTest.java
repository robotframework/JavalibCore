package org.robotframework.javalib.beans.common;

import junit.framework.TestCase;

import org.robotframework.javalib.beans.classpath.InterfaceBasedKeywordFilter;
import org.robotframework.javalib.beans.common.IKeywordBeanDefintionReader;
import org.robotframework.javalib.beans.common.KeywordBeanDefinitionReader;
import org.robotframework.javalib.keyword.EmptyKeyword;
import org.springframework.context.support.GenericApplicationContext;


public class KeywordBeanDefinitionReaderIntegrationTest extends TestCase {
    private GenericApplicationContext ctx;
    private IKeywordBeanDefintionReader beanDefinitionReader;

    protected void setUp() throws Exception {
        ctx = new GenericApplicationContext();
        beanDefinitionReader = new KeywordBeanDefinitionReader(ctx, Thread.currentThread().getContextClassLoader());
    }

    public void testRegistersKeywordBeans() throws Exception {
        beanDefinitionReader.loadBeanDefinitions("org/robotframework/**/Empty**.class", new AllAcceptingFilter());
        ctx.refresh();
        ctx.containsBean("emptykeyword");
        assertEquals(1, ctx.getBeanDefinitionCount());
    }

    public void testPatternMatters() throws Exception {
        int beanDefinitionCount = beanDefinitionReader.loadBeanDefinitions("org/robotframework/**/nonexistent/**.class", new AllAcceptingFilter());
        assertEquals(0, beanDefinitionCount);
    }

    public void testDefaultFilterAcceptsKeywords() throws Exception {
        beanDefinitionReader.loadBeanDefinitions("org/robotframework/**/keyword/EmptyKeyword.class", new InterfaceBasedKeywordFilter());
        ctx.refresh();
        Object keywordInstance = ctx.getBean("EmptyKeyword");
        assertEquals(EmptyKeyword.class, keywordInstance.getClass());
    }
}
