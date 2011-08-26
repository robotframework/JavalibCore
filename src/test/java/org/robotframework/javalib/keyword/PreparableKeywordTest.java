package org.robotframework.javalib.keyword;

import org.robotframework.javalib.keyword.PreparableKeyword;

import junit.framework.TestCase;

public class PreparableKeywordTest extends TestCase {
    private MockPreparableKeyword preparableKeyword;

    protected void setUp() throws Exception {
        preparableKeyword = new MockPreparableKeyword();
    }
    
    public void testKeywordIsPreparedBeforeExecution() throws Exception {
        Object[] args = new Object[] { "Argument1" };
        preparableKeyword.execute(args);
        
        assertPrepareWasCalledWith(args);
        assertOperateWasCalledWith(args);
    }

    public void testSequenceIsPrepareOperateFinish() throws Exception {
        preparableKeyword.execute(null);
        assertEquals(0, preparableKeyword.prepareCallSequenceNumber);
        assertEquals(1, preparableKeyword.operateCallSequenceNumber);
        assertEquals(2, preparableKeyword.finishCallSequenceNumber);
    }
    
    public void testReturnsResultFromOperate() throws Exception {
        String returnValue = "Return Value";
        preparableKeyword.operateReturnValue = returnValue;
        
        assertEquals(returnValue, preparableKeyword.execute(null));
    }
    
    private void assertOperateWasCalledWith(Object[] args) {
        assertTrue(preparableKeyword.operateWasCalled);
        assertEquals(args, preparableKeyword.operateArguments);
    }

    private void assertPrepareWasCalledWith(Object[] args) {
        assertTrue(preparableKeyword.prepareWasCalled);
        assertEquals(args, preparableKeyword.prepareArguments);
    }

    private class MockPreparableKeyword extends PreparableKeyword {
        boolean prepareWasCalled;
        boolean operateWasCalled;
        Object[] prepareArguments;
        Object[] operateArguments;
        int callCount;
        int prepareCallSequenceNumber;
        int operateCallSequenceNumber;
        int finishCallSequenceNumber;
        Object operateReturnValue;

        protected void prepare(Object[] arguments) {
            prepareArguments = arguments;
            prepareWasCalled = true;
            prepareCallSequenceNumber = callCount;
            callCount++;
        }

        protected Object operate(Object[] arguments) {
            operateArguments = arguments;
            operateWasCalled = true;
            operateCallSequenceNumber = callCount;
            callCount++;
            return operateReturnValue;
        }
        
        protected void finish(Object[] arguments) {
            finishCallSequenceNumber = callCount;
            callCount++;
        }
    }
}
