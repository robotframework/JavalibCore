package org.robotframework.javalib.keyword;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
            fakeKeyword.execute(Arrays.asList("arg0"), null );
        } catch(IllegalArgumentException e) {
            assertEquals("Illegal number of arguments (should be 2, but got 1)", e.getMessage());
        }
    }
    
    public void testRaisesExceptionIfArgumentCountIsGreaterThanExpected() throws Exception {
        fakeKeyword.expectedArgumentCount = 1;
        
        try {
            fakeKeyword.execute(Arrays.asList("arg0", "arg1"), null );
        } catch(IllegalArgumentException e) {
            assertEquals("Illegal number of arguments (should be 1, but got 2)", e.getMessage());
        }
    }

    public void testExecuteDelegatesToOperate() {
        fakeKeyword.execute(Arrays.asList(), null);
        assertTrue(fakeKeyword.wasDelegatedToOperate);
    }
    
    public void testExecutePassesArgumentsToOperate() throws Exception {
        List args = Arrays.asList("argument1", "argument2");
        fakeKeyword.expectedArgumentCount = 2;
        fakeKeyword.execute(args, null);
        assertEquals(args, fakeKeyword.arguments);
    }
    
    public void testExecutePassesReturnValueFromOperate() throws Exception {
        fakeKeyword.returnValue = "My Return Value";
        assertEquals("My Return Value", fakeKeyword.execute(Arrays.asList(), null));
    }
    
    private class RecordingAbstractKeyword extends ArgumentCheckingKeyword {
        boolean wasDelegatedToOperate;
        int expectedArgumentCount;
        Object returnValue;
        List arguments;
        
        protected Object operate(List arguments) {
            this.arguments = arguments;
            wasDelegatedToOperate = true;
            return returnValue;
        }

        public int getExpectedArgumentCount() {
            return expectedArgumentCount;
        }

        public Object execute(List args, Map kwargs) {
            return super.execute(arguments, kwargs);
        }
    }
}
