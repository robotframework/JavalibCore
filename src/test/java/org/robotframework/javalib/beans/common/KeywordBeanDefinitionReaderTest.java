package org.robotframework.javalib.beans.common;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.beans.common.ClassFinder;
import org.robotframework.javalib.beans.common.IClassFilter;
import org.robotframework.javalib.beans.common.IKeywordBeanDefintionReader;
import org.robotframework.javalib.beans.common.KeywordBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;


public class KeywordBeanDefinitionReaderTest extends MockObjectTestCase {
    private String pattern = "org/test/**/pattern/**.class";
    private String packagePrefix = "org.test";
    private Mock mockBeanDefinitionRegistry;
    private Mock mockClassFinder;
    private IKeywordBeanDefintionReader beanDefinitionReader;
    private Mock mockFilter;

    protected void setUp() throws Exception {
        mockClassFinder = mock(ClassFinder.class);
        mockBeanDefinitionRegistry = mock(BeanDefinitionRegistry.class);
        beanDefinitionReader = new KeywordBeanDefinitionReader((BeanDefinitionRegistry) mockBeanDefinitionRegistry.proxy(), null);
        beanDefinitionReader.setClassFinder((ClassFinder) mockClassFinder.proxy());

        mockFilter = mock(IClassFilter.class);
    }

    public void testUsesClassNameAsBeanName() throws Exception {
        mockClassFinder.expects(once()).method("getClasses")
            .will(returnValue(new Class[] { Object.class, Class.class } ));

        mockBeanDefinitionRegistry.expects(once()).method("registerBeanDefinition")
            .with(eq("Object"), ANYTHING);
        mockBeanDefinitionRegistry.expects(once()).method("registerBeanDefinition")
            .with(eq("Class"), ANYTHING);

        beanDefinitionReader.loadBeanDefinitions(null, new AllAcceptingFilter());
    }

    public void testRegistersClassesAsRootBeanDefinitions() throws Exception {
        mockClassFinder.expects(once()).method("getClasses")
            .will(returnValue(new Class[] { Object.class, Class.class } ));

        RootBeanDefinition beanDefinitionForObjectClass = new RootBeanDefinition(Object.class);
        mockBeanDefinitionRegistry.expects(once()).method("registerBeanDefinition")
            .with(ANYTHING, eq(beanDefinitionForObjectClass));
        RootBeanDefinition beanDefinitionForClassClass = new RootBeanDefinition(Class.class);
        mockBeanDefinitionRegistry.expects(once()).method("registerBeanDefinition")
            .with(ANYTHING, eq(beanDefinitionForClassClass));

        beanDefinitionReader.loadBeanDefinitions(null, new AllAcceptingFilter());
    }

    public void testUsesFilterToAcceptClasses() throws Exception {
        mockClassFinder.expects(once()).method("getClasses")
            .will(returnValue(new Class[] { Object.class, Class.class }));

        mockFilter.expects(once()).method("accept")
            .with(eq(Object.class))
            .will(returnValue(true));
        mockFilter.expects(once()).method("accept")
            .with(eq(Class.class))
            .will(returnValue(true));

        mockBeanDefinitionRegistry.stubs().method("registerBeanDefinition");
        beanDefinitionReader.loadBeanDefinitions(null, (IClassFilter) mockFilter.proxy());
    }

    public void testUsesProvidedPatternAndPackagePrefixWithClassFinder() throws Exception {
        mockClassFinder.expects(once()).method("getClasses")
            .with(endsWith(pattern))
            .will(returnValue(new Class[0]));

        beanDefinitionReader.loadBeanDefinitions(pattern, null);
    }

    public void testOnlyRegistersClassesAcceptedByFilter() throws Exception {
        mockClassFinder.expects(once()).method("getClasses")
            .will(returnValue(new Class[] { Object.class, Class.class }));

        mockFilter.expects(once()).method("accept")
            .with(eq(Object.class))
            .will(returnValue(true));
        mockFilter.expects(once()).method("accept")
            .with(eq(Class.class))
            .will(returnValue(false));

        mockBeanDefinitionRegistry.expects(once()).method("registerBeanDefinition")
            .with(eq("Object"), ANYTHING);

        beanDefinitionReader.loadBeanDefinitions(null, (IClassFilter) mockFilter.proxy());
    }

    public void testReturnsNumberOfBeanDefinitionsRegistered() throws Exception {
        mockClassFinder.expects(once()).method("getClasses")
            .will(returnValue(new Class[] { Object.class, Class.class, String.class }));
        mockBeanDefinitionRegistry.stubs().method("registerBeanDefinition");
        assertEquals(3, beanDefinitionReader.loadBeanDefinitions(null, new AllAcceptingFilter()));

        mockClassFinder.expects(once()).method("getClasses")
            .will(returnValue(new Class[] { Object.class, Class.class }));
        mockBeanDefinitionRegistry.stubs().method("registerBeanDefinition");
        assertEquals(2, beanDefinitionReader.loadBeanDefinitions(null, new AllAcceptingFilter()));

        mockFilter.stubs().method("accept")
            .will(returnValue(false));

        mockClassFinder.expects(once()).method("getClasses")
            .will(returnValue(new Class[] { Object.class, Class.class }));
        mockBeanDefinitionRegistry.stubs().method("registerBeanDefinition");
        assertEquals(0, beanDefinitionReader.loadBeanDefinitions(null, (IClassFilter) mockFilter.proxy()));
    }

    public void testAddsClassPathAllToPattern() throws Exception {
        mockClassFinder.expects(once()).method("getClasses")
            .with(startsWith("classpath*:"))
            .will(returnValue(new Class[0]));

        beanDefinitionReader.loadBeanDefinitions(pattern, null);
    }
}
