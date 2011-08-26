package org.robotframework.javalib.keyword;
import org.robotframework.javalib.keyword.ArgumentCheckingKeyword;

import junit.framework.TestCase;

public class ArgumentCheckingKeywordTest extends TestCase {
    private RecordingAbstractKeyword fakeKeyword;

    protected void setUp() throws Exception {
        fakeKeyword = new RecordingAbstractKeyword();
    }
    
    public void testRaisesExceptionIfArgumentCountIsSmallerThanExpected() throws Exception {
        fakeKeyword.expectedArgumentCount = 2;
        
        try {
            fakeKeyword.execute(new String[] { "arg0" } );
        } catch(IllegalArgumentException e) {
            assertEquals("Illegal number of arguments (should be 2, but got 1)", e.getMessage());
        }
    }
    
    public void testRaisesExceptionIfArgumentCountIsGreaterThanExpected() throws Exception {
        fakeKeyword.expectedArgumentCount = 1;
        
        try {
            fakeKeyword.execute(new String[] { "arg0", "arg1"} );
        } catch(IllegalArgumentException e) {
            assertEquals("Illegal number of arguments (should be 1, but got 2)", e.getMessage());
        }
    }

    public void testExecuteDelegatesToOperate() {
        fakeKeyword.execute(new Object[0]);
        assertTrue(fakeKeyword.wasDelegatedToOperate);
    }
    
    public void testExecutePassesArgumentsToOperate() throws Exception {
        String[] args = new String[] { "argument1", "argument2" };
        fakeKeyword.expectedArgumentCount = 2;
        fakeKeyword.execute(args);
        assertEquals(args, fakeKeyword.arguments);
    }
    
    public void testExecutePassesReturnValueFromOperate() throws Exception {
        fakeKeyword.returnValue = "My Return Value";
        assertEquals("My Return Value", fakeKeyword.execute(new Object[0]));
    }
    
    private class RecordingAbstractKeyword extends ArgumentCheckingKeyword {
        boolean wasDelegatedToOperate;
        int expectedArgumentCount;
        Object returnValue;
        Object[] arguments;
        
        protected Object operate(Object[] arguments) {
            this.arguments = arguments;
            wasDelegatedToOperate = true;
            return returnValue;
        }

        public int getExpectedArgumentCount() {
            return expectedArgumentCount;
        }
    }
}
