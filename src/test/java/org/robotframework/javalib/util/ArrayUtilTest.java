package org.robotframework.javalib.util;

import junit.framework.TestCase;

public class ArrayUtilTest extends TestCase {
    public void testAddsToArray() throws Exception {
        String[] oneTwoThree = ArrayUtil.add(new String[] { "one", "two" }, "three");
        ArrayUtil.assertArraysEquals(new String[] {"one", "two", "three"}, oneTwoThree);
    }
}
