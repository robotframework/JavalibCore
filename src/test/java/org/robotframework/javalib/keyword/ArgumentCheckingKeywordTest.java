package org.robotframework.javalib.keyword;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ArgumentCheckingKeywordTest {
    private RecordingAbstractKeyword fakeKeyword;

    @BeforeEach
    public void setupTest() {
        this.fakeKeyword = new RecordingAbstractKeyword();
    }

    @Test
    public void testExecuteDelegatesToOperate() {
        fakeKeyword.execute(Arrays.asList(), null);
        assertTrue(fakeKeyword.wasDelegatedToOperate);
    }

    @Test
    public void testExecutePassesArgumentsToOperate() {
        List args = Arrays.asList("argument1", "argument2");
        fakeKeyword.expectedArgumentCount = 2;
        fakeKeyword.execute(args, null);
        assertEquals(args, fakeKeyword.arguments);
    }

    @Test
    public void testExecutePassesReturnValueFromOperate() {
        fakeKeyword.returnValue = "My Return Value";
        assertEquals("My Return Value", fakeKeyword.execute(Arrays.asList(), null));
    }

    private class RecordingAbstractKeyword extends PreparableKeyword {
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
            return super.execute(args, kwargs);
        }

        @Override
        public List<String> getArgumentTypes() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
