package org.robotframework.javalib.factory;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.factory.ApplicationContextKeywordFactory;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.library.MockClassLoader;
import org.springframework.context.ApplicationContext;


public class ApplicationContextKeywordFactoryTest extends MockObjectTestCase {
    private ApplicationContextKeywordFactory factory;
    private Mock mockApplicationContext;
    
    protected void setUp() {
        mockApplicationContext = mock(ApplicationContext.class);
        factory = new ApplicationContextKeywordFactory((ApplicationContext) mockApplicationContext.proxy());
    }

    public void testFindsKeywordsFromConfigFile() throws Exception {
        mockApplicationContext.expects(once()).method("getBeanNamesForType")
            .with(eq(Keyword.class))
            .will(returnValue(new String[] { "springkeyword" }));
        String[] keywords = factory.getKeywordNames();
        assertEquals("springkeyword", keywords[0]);
    }
    
    public void testCreatesInstancesOfKeywords() throws Exception {
        mockApplicationContext.expects(once()).method("getBean")
            .with(eq("springkeyword"));
        factory.createKeyword("springkeyword");
    }
    
    public void testGetClassLoaderReturnsApplicationContextClassLoader() throws Exception {
        MockClassLoader classLoader = new MockClassLoader();
        mockApplicationContext.expects(once())
            .will(returnValue(classLoader));
        
        assertEquals(classLoader, factory.getClassLoader());
    }
}
