package org.robotframework.javalib.keyword;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class AnnotatedKeywords {
    public static final String __PARANAMER_DATA = "<init> \n" + "myKeyword \n"
            + "keywordThatReturnsItsArguments java.lang.String arg\n" + "someKeyword java.lang.String someArgument\n"
            + "keywordWithVariableArgumentCount java.lang.String,java.lang.String[] someArgument,restOfTheArguments\n";

    @RobotKeyword
    public void failingKeyword() {
        throw new AssertionFailedError("Assertion failed");
    }

    @RobotKeyword
    public String keywordThatReturnsItsArguments(String arg) {
        return arg;
    }

    @RobotKeyword
    @ArgumentNames({ "one", "two=", "three=" })
    public Object overloaded(String one, String two, String three) {
        return three;
    }

    @RobotKeywordOverload
    public Object overloaded(String one) {
        return one;
    }

    @RobotKeywordOverload
    public Object overloaded(String one, int two) {
        return two;
    }

    @ArgumentNames({ "overridenArgumentName" })
    @RobotKeyword("Some documentation")
    public void someKeyword(String someArgument) {
    }

    @RobotKeyword("This is a keyword with variable argument count")
    public Object[] keywordWithVariableArgumentCount(String someArgument, String... restOfTheArguments) {
        return restOfTheArguments;
    }

    @RobotKeyword
    public void variousArgs(String arg, List<String> varargs, Map<String, Object> kwargs) {
        System.out.println("arg: " + arg);
        for (String varg: varargs)
            System.out.println("vararg: " + varg);
        for (String key: kwargs.keySet())
            System.out.println("kwarg: " + key + " " + kwargs.get(key));
    }

    @RobotKeyword
    @ArgumentNames({ "firstArg", "secondArg=two", "thirdArg=three" })
    public String defaultValues(String first, String second, String third) {
        return String.format("%s %s %s", first, second, third);
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

    @RobotKeyword("Handle byteArray")
    public byte[] byteArrayTest(String expectedContent, byte[] bytesIn) {
        if (!expectedContent.equals(new String(bytesIn))) {
            throw new AssertionFailedError("Arguments were not same");
        }
        return expectedContent.getBytes();
    }

    public interface SomeInterface {
    }

    public class SomeObject implements SomeInterface {
        public String name;
        public String value;
    }

    @RobotKeyword
    @ArgumentNames({"*Technical arguments"})
    public String[] onlyVarargs(String[] arguments) {
        return arguments;
    }

    @RobotKeyword
    @ArgumentNames({"Image or text to wait", "Similarity of images=0.7", "*Technical arguments"})
    public void defaultAndVarargs(String imageNameOrText, double similarity, String[] arguments) {
        Assertions.assertEquals(0.7, similarity);
    }

    @RobotKeyword
    @ArgumentNames({"port=0"})
    public int useInt(int port) {
        return port;
    }

    @RobotKeyword
    @ArgumentNames({"port=0"})
    public Integer useInteger(Integer port) {
        return port;
    }

    @RobotKeyword
    @ArgumentNames("arg")
    public List<?> listAsArgument(List<?> arg) {
        return arg;
    }

    @RobotKeyword
    @ArgumentNames({ "arg" })
    public Map<Object, Object> mapAsArgument(Map<Object, Object> arg) {
        return arg;
    }
}
