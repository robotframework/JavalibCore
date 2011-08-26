package org.robotframework.javalib.context;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.keyword.EmptyKeyword;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.util.IKeywordNameNormalizer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.HashMap;
import java.util.Map;

public class KeywordApplicationContextTest extends MockObjectTestCase {
    private KeywordApplicationContext ctx;
    private BeanDefinition beanDefinition;
    private Mock stringNormalizer;

    protected void setUp() throws Exception {
        stringNormalizer = mock(IKeywordNameNormalizer.class);
        stringNormalizer.stubs().method("normalize")
                .will(returnValue("normalized"));
        ctx = new KeywordApplicationContext((IKeywordNameNormalizer) stringNormalizer.proxy());
        Mock mockBeanDefinition = mock(BeanDefinition.class);
        mockBeanDefinition.stubs().method(ANYTHING);
        beanDefinition = (BeanDefinition) mockBeanDefinition.proxy();

    }

    public void testRegistersAliasesForBeansWithNormalizedNames() throws Exception {
        String beanIdUnnormalized = "Keyword Name";
        String beanIdNormalized = "keywordname";

        mockNormalizer(beanIdUnnormalized, beanIdNormalized);
        ctx.registerBeanDefinition(beanIdUnnormalized, beanDefinition);

        String[] aliases = ctx.getAliases(beanIdUnnormalized);
        assertEquals(beanIdNormalized, aliases[0]);
    }

    public void testRegistersBeansWithOriginalNames() throws Exception {
        ctx.registerBeanDefinition("keywordName", beanDefinition);

        String[] names = ctx.getBeanDefinitionNames();
        assertEquals("keywordName", names[0]);
    }

    public void testNormalizesNamesWhenGettingBeans() throws Exception {
        String keywordNameUnNormalized = "Keyword Name";
        String keywordNameNormalized = "keywordname";
        String beanIdUnnormalized = "keywordName";

        mockNormalizer(beanIdUnnormalized, keywordNameNormalized);

        ctx.registerBeanDefinition(beanIdUnnormalized, new RootBeanDefinition(Object.class));

        mockNormalizer(keywordNameUnNormalized, keywordNameNormalized);

        Object bean = ctx.getBean(keywordNameUnNormalized);
        assertEquals(Object.class, bean.getClass());
    }

    public void testReturnsNormalizednamesWhenGettingBeanNamesForCertainType() throws Exception {
        String keywordNameUnNormalized = "emptyKeyword";
        String keywordNameNormalized = "emptykeyword";
        int registeredBeansCount = registerBeanDefinitionsToContext(keywordNameUnNormalized);

        stringNormalizer.expects(exactly(registeredBeansCount)).method("normalize")
            .with(eq(keywordNameUnNormalized))
            .will(returnValue(keywordNameNormalized));

        String[] names = ctx.getBeanNamesForType(Keyword.class);
        assertEquals(keywordNameNormalized, names[0]);
    }

    private int registerBeanDefinitionsToContext(final String keywordNameUnNormalized) {
        Map beans = new HashMap() {{
            put(keywordNameUnNormalized, new RootBeanDefinition(EmptyKeyword.class));
        }};

        for (Object keywordName : beans.keySet()) {
            ctx.registerBeanDefinition(keywordName.toString(), (BeanDefinition) beans.get(keywordName.toString()));
        }
        return beans.size();
    }

    private void mockNormalizer(String keywordNameUnNormalized, String keywordNameNormalized) {
        stringNormalizer.expects(once()).method("normalize")
            .with(eq(keywordNameUnNormalized))
            .will(returnValue(keywordNameNormalized));
    }
}

