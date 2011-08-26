package org.robotframework.javalib.mocks;

import org.robotframework.javalib.keyword.Keyword;

public class RecordingKeyword implements Keyword  {
    public Object[] arguments;
    public boolean executed;
    public Object returnValue;
    
    public Object execute(Object[] arguments) {
        this.arguments = arguments; 
        executed = true;
        return returnValue;
    }
}
