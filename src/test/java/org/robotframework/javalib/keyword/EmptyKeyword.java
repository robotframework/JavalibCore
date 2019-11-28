package org.robotframework.javalib.keyword;

import java.util.List;
import java.util.Map;

import org.robotframework.javalib.keyword.Keyword;


public class EmptyKeyword implements Keyword {
    public Object execute(List arguments, Map kwargs) {
        return "Empty Keyword Return Value";
    }
}
