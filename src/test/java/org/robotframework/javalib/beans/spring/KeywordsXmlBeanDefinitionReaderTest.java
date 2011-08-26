package org.robotframework.javalib.beans.spring;

import java.io.IOException;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.beans.spring.KeywordsXmlBeanDefinitionReader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

public class KeywordsXmlBeanDefinitionReaderTest extends MockObjectTestCase {
    private KeywordsXmlBeanDefinitionReader reader;
    private Mock mockResourcePatternResolver;
    private DefaultListableBeanFactory beanDefinitionRegistry;

    protected void setUp() throws Exception {
        beanDefinitionRegistry = new DefaultListableBeanFactory();
        reader = new KeywordsXmlBeanDefinitionReader(beanDefinitionRegistry, null);

        mockResourcePatternResolver = mock(ResourcePatternResolver.class);
        reader.setResourcePatternResolver((ResourcePatternResolver) mockResourcePatternResolver.proxy());
    }

    public void testUsesResolverWithProvidedPatternToFindConfigurationFiles() throws Exception {
        String pattern = "com/test/**/pattern.xml";

        mockResourcePatternResolver.expects(once()).method("getResources")
            .with(endsWith(pattern))
            .will(returnValue(new Resource[0]));
        reader.setResourcePatternResolver((ResourcePatternResolver) mockResourcePatternResolver.proxy());

        reader.loadBeanDefinitions(pattern);
    }

    public void testRegistersKeywordsFromXml() throws Exception {
        setTestXmlConfigFile();

        reader.loadBeanDefinitions(null);

        assertTrue(beanDefinitionRegistry.containsBeanDefinition("keywordwiredfromspring"));
    }

    public void testReturnsNumberOfRegisteredBeans() throws Exception {
        setTestXmlConfigFile();

        int count = reader.loadBeanDefinitions(null);
        assertEquals(count, beanDefinitionRegistry.getBeanDefinitionCount());
    }

    public void testAddsClassPathAllPrefixToPattern() throws Exception {
        mockResourcePatternResolver.expects(once()).method("getResources")
            .with(startsWith("classpath*:"))
            .will(returnValue(new Resource[0]));

        reader.loadBeanDefinitions("");
    }

    public void testThrowsRuntimeExceptionIfGetResourcesThrowsIOException() throws Exception {
        mockResourcePatternResolver.expects(once()).method("getResources")
            .will(throwException(new IOException()));

        try {
            reader.loadBeanDefinitions("");
            fail();
        } catch(RuntimeException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    private void setTestXmlConfigFile() {
        Resource[] resources = new Resource[] { new ClassPathResource("org/robotframework/test/keywords.xml") };
        mockResourcePatternResolver.expects(once()).method("getResources")
            .will(returnValue(resources));
    }
}
