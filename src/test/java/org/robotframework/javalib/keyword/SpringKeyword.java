package org.robotframework.javalib.keyword;

import java.util.List;
import java.util.Map;

import org.robotframework.javalib.keyword.Keyword;

public class SpringKeyword implements Keyword {
    public Object execute(List args, Map kwargs) {
        return "Spring Keyword";
    }
}
