package org.robotframework.javalib.autowired;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnnotatedAutowiredLibraryTest {
    private static AnnotatedAutowiredLibrary annotatedAutowiredLibrary;

    @BeforeAll
    public static void setUp() {
        annotatedAutowiredLibrary = new AnnotatedAutowiredLibrary("org/robotframework/**/autowired/**/**.class");
        annotatedAutowiredLibrary.getKeywordNames();
    }

    @Test
    public void testAutowired() throws Exception {
        AnnotatedAutowiredKeywords1 annotatedAutowiredKeywords1 = annotatedAutowiredLibrary.getAnnotatedAutowiredKeywords1();
        AnnotatedAutowiredKeywords2 annotatedAutowiredKeywords2 = annotatedAutowiredLibrary.getAnnotatedAutowiredKeywords2();

        assertTrue(annotatedAutowiredLibrary == annotatedAutowiredLibrary.getAnnotatedAutowiredLibrary());
        assertTrue(annotatedAutowiredLibrary == annotatedAutowiredKeywords1.getAnnotatedAutowiredLibrary());
        assertTrue(annotatedAutowiredLibrary == annotatedAutowiredKeywords2.getAnnotatedAutowiredLibrary());

        assertTrue(annotatedAutowiredKeywords1 == annotatedAutowiredLibrary.getAnnotatedAutowiredKeywords1());
        assertTrue(annotatedAutowiredKeywords1 == annotatedAutowiredKeywords1.getAnnotatedAutowiredKeywords1());
        assertTrue(annotatedAutowiredKeywords1 == annotatedAutowiredKeywords2.getAnnotatedAutowiredKeywords1());

        assertTrue(annotatedAutowiredKeywords2 == annotatedAutowiredLibrary.getAnnotatedAutowiredKeywords2());
        assertTrue(annotatedAutowiredKeywords2 == annotatedAutowiredKeywords1.getAnnotatedAutowiredKeywords2());
        assertTrue(annotatedAutowiredKeywords2 == annotatedAutowiredKeywords2.getAnnotatedAutowiredKeywords2());
    }
}
