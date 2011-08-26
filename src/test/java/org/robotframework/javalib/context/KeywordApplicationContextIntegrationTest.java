package org.robotframework.javalib.context;

import junit.framework.TestCase;

import org.robotframework.javalib.context.KeywordApplicationContext;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.keyword.SpringKeyword;
import org.robotframework.javalib.util.KeywordNameNormalizer;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;


public class KeywordApplicationContextIntegrationTest extends TestCase {
    public void testBeanDefinitionReadersRegisterNormalizedBeanNames() throws Exception {
        KeywordApplicationContext ctx = new KeywordApplicationContext(new KeywordNameNormalizer());
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ctx);
        reader.loadBeanDefinitions("org/robotframework/test/keywords.xml");
        ctx.refresh();

        Keyword keyword = (Keyword) ctx.getBean("keywordwithunnormalizedname");
        assertEquals(SpringKeyword.class, keyword.getClass());
    }
}
