package org.robotframework.javalib.autowired;

import java.util.List;

import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.library.AnnotationLibrary;

public class AnnotatedAutowiredLibrary extends AnnotationLibrary {

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

	public AnnotatedAutowiredLibrary() {
	}

	public AnnotatedAutowiredLibrary(String keywordPattern) {
		super(keywordPattern);
	}

	public AnnotatedAutowiredLibrary(List<String> keywordPatterns) {
		super(keywordPatterns);
	}

}
