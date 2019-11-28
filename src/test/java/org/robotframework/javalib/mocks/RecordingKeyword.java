package org.robotframework.javalib.mocks;

import java.util.List;
import java.util.Map;

import org.robotframework.javalib.keyword.Keyword;

public class RecordingKeyword implements Keyword  {
    public List arguments;
    public boolean executed;
    public Object returnValue;
    
    public Object execute(List arguments, Map kwargs) {
        this.arguments = arguments; 
        executed = true;
        return returnValue;
    }
}
