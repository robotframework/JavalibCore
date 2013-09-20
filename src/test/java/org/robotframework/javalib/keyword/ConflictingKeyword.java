package org.robotframework.javalib.keyword;


public class ConflictingKeyword implements Keyword {
    public Object execute(Object[] arguments) {
        return "Classpath Keyword";
    }
}
