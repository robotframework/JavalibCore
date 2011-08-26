package org.robotframework.javalib.beans.common;

import junit.framework.TestCase;

import org.robotframework.javalib.beans.common.ClassFinder;
import org.robotframework.javalib.beans.common.DefaultClassFinder;
import org.robotframework.javalib.keyword.CollisionKeyword;


public class ClassFinderIntegrationTest extends TestCase {
    public void testFindsMatchingClasses() throws Exception {
        ClassFinder classFinder = new DefaultClassFinder();
        Class[] classes = classFinder.getClasses("classpath*:org/robotframework/**/keyword/Col**Keyword.class");
        assertEquals(CollisionKeyword.class, classes[0]);
    }
}
