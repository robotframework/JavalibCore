package org.robotframework.javalib.reflection;

import java.util.Arrays;
import java.util.List;

public class KeywordInvokerHandlingInvocationsTest extends KeywordInvokerTestCase {
    private KeywordInvoker keywordInvoker;

    protected void setUp() throws Exception {
        keywordInvoker = new KeywordInvoker(this, getMethod("someMethod"));
    }
    
    public void testInvokesWrappedMethod() throws Exception {
        List args = Arrays.asList("someArg", "moreArgs");
        assertEquals("someArg", keywordInvoker.invoke(args, null));
    }

    public void testThrowsRuntimeExceptionInCaseOfException() throws Exception {
        try {
            keywordInvoker.invoke(null, null);
            fail();
        } catch (RuntimeException e) {
            //Expected
        }
    }

    public void testGetsAnnotationValue() throws Exception {
        assertEquals("documentation", keywordInvoker.getDocumentation());
    }
}
