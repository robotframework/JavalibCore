package org.robotframework.javalib.factory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.robotframework.javalib.annotation.RobotKeyword;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnnotationKeywordFactoryIntegrationTest {
    private static AnnotationKeywordFactory annotationKeywordFactory;
    private String keywordName = "someKeyword";

    @BeforeAll
    public static void setUp() {
        annotationKeywordFactory = new AnnotationKeywordFactory(new HashMap() {{
            put("keywordBean", new Object() {
                @SuppressWarnings("unused")
                @RobotKeyword
                public void someKeyword() { }
            });
        }});
    }

    @Test
    public void testFindsAnnotatedKeywordsFromKeywordBeans() throws Exception {
        List expectedKeywordNames = Arrays.asList(keywordName);
        assertIterableEquals(expectedKeywordNames, annotationKeywordFactory.getKeywordNames());
    }

    @Test
    public void testNormalizesKeywordNamesBeforeExecution() throws Exception {
        assertNotNull(annotationKeywordFactory.createKeyword(keywordName));
    }
}
