package org.robotframework.javalib.beans.annotation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.keyword.DocumentedKeyword;

import static org.junit.jupiter.api.Assertions.*;


public class AnnotationKeywordExtractorTest {
    private static boolean keywordWasCalled = false;
    private static String keywordWithoutArgumentsExecutionResult = "keyword1ExecutionResult";
    private static String keywordWithArgumentsExecutionResult = "keyword2ExecutionResult";

    private static Map extractedKeywords;
    private static DocumentedKeyword keywordWithArguments;
    private static DocumentedKeyword keywordWithoutArguments;
    private static DocumentedKeyword keywordWithoutReturnValue;
    private static IKeywordExtractor extractor;

    @BeforeAll
    public static void setUp() {
        extractor = new AnnotationKeywordExtractor();
        extractedKeywords = extractor.extractKeywords(new MyKeywordsBean());
        keywordWithArguments = (DocumentedKeyword) extractedKeywords.get("keywordWithArguments");
        keywordWithoutArguments = (DocumentedKeyword) extractedKeywords.get("keywordWithoutArguments");
        keywordWithoutReturnValue = (DocumentedKeyword) extractedKeywords.get("keywordWithoutReturnValue");
    }

    @Test
    public void testExtractsCorrectNumberOfKeywordsFromKeywordBean() {
        assertEquals(expectedKeywordCount(), extractedKeywords.size());
    }

    @Test
    public void testExtractsKeywordsWithReturnValue() {
        assertEquals(keywordWithoutArgumentsExecutionResult, keywordWithoutArguments.execute(null, null));
    }

    @Test
    public void testExtractsKeywordsWithArguments() {
        String keywordArgument = "someArgument";
        assertEquals(keywordWithArgumentsExecutionResult + keywordArgument, keywordWithArguments.execute(Arrays.asList(keywordArgument), null));
    }

    @Test
    public void testExtractsKeywordsWithoutReturnValue() {
        assertNull(keywordWithoutReturnValue.execute(null, null));
        assertTrue(keywordWasCalled);
    }

    @Test
    public void testExtractsKeywordDocumentation() {
        assertEquals("This is a keyword with arguments", keywordWithArguments.getDocumentation());
        assertEquals("This is a keyword without arguments", keywordWithoutArguments.getDocumentation());
        assertEquals("This is a keyword without return value", keywordWithoutReturnValue.getDocumentation());
    }

    @Test
    public void testExtractsKeywordArguments() {
        assertIterableEquals(Arrays.asList("overridenArgumentName"), keywordWithArguments.getArgumentNames());
    }

    private int expectedKeywordCount() {
        Method[] methods = MyKeywordsBean.class.getMethods();
        int keywordCount = 0;
        for (Method method : methods) {
            if (method.isAnnotationPresent(RobotKeyword.class)) {
                ++keywordCount;
            }
        }
        return keywordCount;
    }

    public static class MyKeywordsBean {
        @RobotKeyword("This is a keyword without arguments")
        public Object keywordWithoutArguments() {
            return keywordWithoutArgumentsExecutionResult;
        }

        @ArgumentNames({"overridenArgumentName"})
        @RobotKeyword("This is a keyword with arguments")
        public Object keywordWithArguments(String argument) {
            return keywordWithArgumentsExecutionResult + argument;
        }

        @RobotKeyword("This is a keyword without return value")
        public void keywordWithoutReturnValue() {
            keywordWasCalled = true;
        }

        @SuppressWarnings("unused")
        @RobotKeyword
        private void annotatedPrivateMethod() {}

        @SuppressWarnings("unused")
        private void notAKeyword() {}
    }
}
