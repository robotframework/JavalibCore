package org.robotframework.javalib.reflection;

import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeywordInvokerHandlingInvocationsTest {
    private KeywordInvoker keywordInvoker;

    @BeforeEach
    protected void setUp() {
        keywordInvoker = new KeywordInvoker(this, new TestKeywordInvoker().getMethod("someMethod"));
    }

    public void testInvokesWrappedMethod() {
        List args = Arrays.asList("someArg", "moreArgs");
        assertEquals("someArg", keywordInvoker.invoke(args, null));
    }

    public void testGetsAnnotationValue() {
        assertEquals("documentation", keywordInvoker.getDocumentation());
    }
}
