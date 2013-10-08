package org.robotframework.javalib.autowired;

import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class AnnotatedAutowiredKeywords1 {

	@Autowired
	private AnnotatedAutowiredKeywords1 annotatedAutowiredKeywords1;

	public AnnotatedAutowiredKeywords1 getAnnotatedAutowiredKeywords1() {
		return annotatedAutowiredKeywords1;
	}

	@Autowired
	private AnnotatedAutowiredKeywords2 annotatedAutowiredKeywords2;

	public AnnotatedAutowiredKeywords2 getAnnotatedAutowiredKeywords2() {
		return annotatedAutowiredKeywords2;
	}

	@Autowired
	private AnnotatedAutowiredLibrary annotatedAutowiredLibrary;

	public AnnotatedAutowiredLibrary getAnnotatedAutowiredLibrary() {
		return annotatedAutowiredLibrary;
	}

}
