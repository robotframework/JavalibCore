package org.robotframework.example.keyword;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class AnnotationKeywords {
    @RobotKeyword
    public void annotationBasedKeyword() {
        System.out.println("Hello world");
    }
}
