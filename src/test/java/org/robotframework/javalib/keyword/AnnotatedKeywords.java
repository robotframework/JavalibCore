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

    @RobotKeyword
    @ArgumentNames({"one", "two=", "three="})
    public void overloaded(String one, String two, String three) {
    }

    @RobotKeyword
    public void overloaded(String one) {
    }

    @RobotKeyword
    public void overloaded(String one, int two) {
    }

    @ArgumentNames({"overridenArgumentName"})
    @RobotKeyword("Some documentation")
    public void someKeyword(String someArgument) {
    }

    @RobotKeyword("This is a keyword with variable argument count")
    public Object[] keywordWithVariableArgumentCount(String someArgument, String... restOfTheArguments) {
        return restOfTheArguments;
    }

    @RobotKeyword("This is a keyword with numeric arguments. The keyword will fail unless all are 42.")
    public void keywordWithNumericArguments(long l1, Long l2, short s1, Short s2) {
        if (l1 != 42 || l2 != 42 || s1 != 42 || s2 != 42)
            throw new AssertionFailedError("All arguments should be 42.");
    }

    @RobotKeyword("This is a keyword with object argument.")
    public SomeObject getSomeObject() {
        SomeObject obj = new SomeObject();
        obj.name = "Hello";
        obj.value = "World";
        return obj;
    }

    @RobotKeyword("This is a keyword with object argument.")
    public void keywordWithObjectArgument(SomeInterface arg) {
        if (arg == null)
            throw new AssertionFailedError("Argument was null.");
    }

    public interface SomeInterface {
    }

    public class SomeObject implements SomeInterface {
        public String name;
        public String value;
    }
}
