package org.robotframework.javalib.reflection;

import junit.framework.TestCase;

public class ArgumentConverterTest extends TestCase {

    public void testReturnsNullIfNullArgumentIsPassed() throws Exception {
        IArgumentConverter converter = new ArgumentConverter(new Class[] {});
        assertNull(converter.convertArguments(null));
    }

    public void testConvertsArgumentsToMatchExpected() throws Exception {
        IArgumentConverter converter = new ArgumentConverter(new Class[] {Integer.class, String.class});
        Object[] converted = converter.convertArguments(new String[] {"42", "Hi!"});
        assertTrue(converted[0] instanceof Integer);
        assertTrue(converted[1] instanceof String);
    }

    public void testConvertsOnlyNonArrayArgumentsIfLastArgumentIsArray() throws Exception {
        String[] array = new String[] {"X", "Y"};
        IArgumentConverter converter = new ArgumentConverter(new Class[] {Long.class, Double.class, array.getClass()});
        Object[] converted = converter.convertArguments(new Object[] {"42", "3.14", array});
        assertTrue(converted[0] instanceof Long);
        assertTrue(converted[1] instanceof Double);
        assertTrue(converted[2] instanceof String[]);
    }

    public void testConvertsOnlyNonArrayArgumentsIfArrayIsNotLast() throws Exception {
        String[] array = new String[] {"X", "Y"};
        IArgumentConverter converter = new ArgumentConverter(new Class[] {String.class, array.getClass(), Long.class});
        Object[] converted = converter.convertArguments(new Object[] {"42", array, 42L});
        assertTrue(converted[0] instanceof String);
        assertTrue(converted[1] instanceof String[]);
        assertTrue(converted[2] instanceof Long);
    }
}
