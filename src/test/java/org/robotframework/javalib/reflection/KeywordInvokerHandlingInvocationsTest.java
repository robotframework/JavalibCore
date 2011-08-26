package org.robotframework.javalib.reflection;

public class KeywordInvokerHandlingInvocationsTest extends KeywordInvokerTestCase {
    private KeywordInvoker keywordInvoker;

    protected void setUp() throws Exception {
        keywordInvoker = new KeywordInvoker(this, getMethod("someMethod"));
    }
    
    public void testInvokesWrappedMethod() throws Exception {
        Object[] args = new String[] { "someArg", "moreArgs" };
        assertEquals("someArg", keywordInvoker.invoke(args));
    }

    public void testThrowsRuntimeExceptionInCaseOfException() throws Exception {
        try {
            keywordInvoker.invoke(null);
            fail();
        } catch (RuntimeException e) {
            //Expected
        }
    }

    public void testGetsAnnotationValue() throws Exception {
        assertEquals("documentation", keywordInvoker.getDocumentation());
    }
}
