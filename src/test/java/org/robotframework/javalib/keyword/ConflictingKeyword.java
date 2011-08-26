package org.robotframework.javalib.keyword;

import org.robotframework.javalib.keyword.Keyword;

public class ConflictingKeyword implements Keyword {
    public Object execute(Object[] arguments) {
        return "Classpath Keyword";
    }
}
