package org.robotframework.javalib.beans.spring;

import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.library.MockClassLoader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;


public class KeywordsXmlBeanDefinitionReaderIntegrationTest extends MockObjectTestCase {
    private KeywordsXmlBeanDefinitionReader beanDefinitionReader;
    private MockClassLoader classLoader;
    private DefaultListableBeanFactory beanDefinitionRegistry;

    protected void setUp() throws Exception {
        classLoader = new MockClassLoader();
        beanDefinitionRegistry = new DefaultListableBeanFactory();
        beanDefinitionReader = new KeywordsXmlBeanDefinitionReader(beanDefinitionRegistry, classLoader);
    }

    public void testFindsConfigurationFilesAndRegistersBeans() throws Exception {
        beanDefinitionReader.loadBeanDefinitions("org/**/keywords.xml");
        assertEquals(3, beanDefinitionRegistry.getBeanDefinitionCount());
    }

    public void testUsesProvidedClassLoader() throws Exception {
        beanDefinitionReader.loadBeanDefinitions("org/robotframework/**/keywords.xml");
        assertTrue(classLoader.searchedResources.contains("org/robotframework/"));
    }
}
