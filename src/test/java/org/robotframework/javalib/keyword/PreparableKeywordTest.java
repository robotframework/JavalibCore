package org.robotframework.javalib.keyword;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.robotframework.javalib.keyword.PreparableKeyword;

import junit.framework.TestCase;

public class PreparableKeywordTest extends TestCase {
    private MockPreparableKeyword preparableKeyword;

    protected void setUp() throws Exception {
        preparableKeyword = new MockPreparableKeyword();
    }
    
    public void testKeywordIsPreparedBeforeExecution() throws Exception {
        List args = Arrays.asList("Argument1");
        preparableKeyword.execute(args, null);
        
        assertPrepareWasCalledWith(args, null);
        assertOperateWasCalledWith(args, null);
    }

    public void testSequenceIsPrepareOperateFinish() throws Exception {
        preparableKeyword.execute(null, null);
        assertEquals(0, preparableKeyword.prepareCallSequenceNumber);
        assertEquals(1, preparableKeyword.operateCallSequenceNumber);
        assertEquals(2, preparableKeyword.finishCallSequenceNumber);
    }
    
    public void testReturnsResultFromOperate() throws Exception {
        String returnValue = "Return Value";
        preparableKeyword.operateReturnValue = returnValue;
        
        assertEquals(returnValue, preparableKeyword.execute(null, null));
    }
    
    private void assertOperateWasCalledWith(List args, Map kwargs) {
        assertTrue(preparableKeyword.operateWasCalled);
        assertEquals(args, preparableKeyword.operateArguments);
    }

    private void assertPrepareWasCalledWith(List args, Map kwargs) {
        assertTrue(preparableKeyword.prepareWasCalled);
        assertEquals(args, preparableKeyword.prepareArguments);
    }

    private class MockPreparableKeyword extends PreparableKeyword {
        boolean prepareWasCalled;
        boolean operateWasCalled;
        List prepareArguments;
        List operateArguments;
        int callCount;
        int prepareCallSequenceNumber;
        int operateCallSequenceNumber;
        int finishCallSequenceNumber;
        Object operateReturnValue;

        protected void prepare(List arguments) {
            prepareArguments = arguments;
            prepareWasCalled = true;
            prepareCallSequenceNumber = callCount;
            callCount++;
        }

        protected Object operate(List arguments) {
            operateArguments = arguments;
            operateWasCalled = true;
            operateCallSequenceNumber = callCount;
            callCount++;
            return operateReturnValue;
        }
        
        protected void finish(List arguments) {
            finishCallSequenceNumber = callCount;
            callCount++;
        }
    }
}
