package com.some.own.keyword;

import junit.framework.AssertionFailedError;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;


@RobotKeywords
public class AnnotatedKeywords {
    public static final String __PARANAMER_DATA =
        "<init> \n" +
        "myFailingKeyword \n" +
        "myKeywordThatReturnsItsArguments java.lang.String arg\n";

    @RobotKeyword
    public void myFailingKeyword() {
        throw new AssertionFailedError("Assertion failed");
    }

    @RobotKeyword
    public String myKeywordThatReturnsItsArguments(String arg) {
        return arg;
    }

}
