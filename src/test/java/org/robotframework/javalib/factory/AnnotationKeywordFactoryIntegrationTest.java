package org.robotframework.javalib.factory;

import java.util.HashMap;

import junit.framework.TestCase;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.util.ArrayUtil;

public class AnnotationKeywordFactoryIntegrationTest extends TestCase {
    private AnnotationKeywordFactory annotationKeywordFactory;
    private String keywordName = "someKeyword";

    @Override
    protected void setUp() throws Exception {
        annotationKeywordFactory = new AnnotationKeywordFactory(new HashMap() {{
            put("keywordBean", new Object() {
                @SuppressWarnings("unused")
                @RobotKeyword
                public void someKeyword() { }
            });
        }});
    }

    public void testFindsAnnotatedKeywordsFromKeywordBeans() throws Exception {
        String[] expectedKeywordNames = new String[] { keywordName };
        ArrayUtil.assertArraysContainSame(expectedKeywordNames, annotationKeywordFactory.getKeywordNames());
    }

    public void testNormalizesKeywordNamesBeforeExecution() throws Exception {
        assertNotNull(annotationKeywordFactory.createKeyword(keywordName));
    }
}
