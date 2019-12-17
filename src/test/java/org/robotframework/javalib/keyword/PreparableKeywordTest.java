package org.robotframework.javalib.keyword;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class PreparableKeywordTest {
    private MockPreparableKeyword preparableKeyword;

    @BeforeEach
    public void setUp() {
        this.preparableKeyword = new MockPreparableKeyword();
    }

    @Test
    public void keywordIsPreparedBeforeExecution() throws Exception {
        List args = Arrays.asList("Argument1");
        preparableKeyword.execute(args, null);

        assertPrepareWasCalledWith(args, null);
        assertOperateWasCalledWith(args, null);
    }

    @Test
    public void sequenceIsPrepareOperateFinish() throws Exception {
        preparableKeyword.execute(null, null);
        assertEquals(0, preparableKeyword.prepareCallSequenceNumber);
        assertEquals(1, preparableKeyword.operateCallSequenceNumber);
        assertEquals(2, preparableKeyword.finishCallSequenceNumber);
    }

    @Test
    public void returnsResultFromOperate() throws Exception {
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

        @Override
        public List<String> getArgumentTypes() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
