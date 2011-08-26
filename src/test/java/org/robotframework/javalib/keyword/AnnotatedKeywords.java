package org.robotframework.javalib.keyword;

import junit.framework.AssertionFailedError;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;


@RobotKeywords
public class AnnotatedKeywords {
    public static final String __PARANAMER_DATA =
        "<init> \n" +
        "myKeyword \n" +
        "keywordThatReturnsItsArguments java.lang.String arg\n" +
        "someKeyword java.lang.String someArgument\n" +
        "keywordWithVariableArgumentCount java.lang.String,java.lang.String[] someArgument,restOfTheArguments\n";

    @RobotKeyword
    public void failingKeyword() {
        throw new AssertionFailedError("Assertion failed");
    }

    @RobotKeyword
    public String keywordThatReturnsItsArguments(String arg) {
        return arg;
    }

    @ArgumentNames({"overridenArgumentName"})
    @RobotKeyword("Some documentation")
    public void someKeyword(String someArgument) {
    }

    @RobotKeyword("This is a keyword with variable argument count")
    public Object[] keywordWithVariableArgumentCount(String someArgument, String[] restOfTheArguments) {
        return restOfTheArguments;
    }
}
