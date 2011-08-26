package org.robotframework.javalib.keyword;

import org.robotframework.javalib.keyword.Keyword;


public class EmptyKeyword implements Keyword {
    public Object execute(Object[] arguments) {
        return "Empty Keyword Return Value";
    }
}
