package my.same.keyword;

import junit.framework.AssertionFailedError;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;


@RobotKeywords
public class AnnotatedKeywords {
    public static final String __PARANAMER_DATA =
        "<init> \n" +
        "myFailingKeyword \n";

    @RobotKeyword
    public void myFailingKeyword() {
        throw new AssertionFailedError("Assertion failed");
    }
}
