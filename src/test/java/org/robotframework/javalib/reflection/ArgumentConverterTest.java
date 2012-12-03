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

    public void testConvertingNumericTypes() throws Exception {
        convertAndVerifyNumeric(Integer.class, Integer.class);
        convertAndVerifyNumeric(Long.class, Long.class);
        convertAndVerifyNumeric(Short.class, Short.class);
        convertAndVerifyNumeric(Float.class, Float.class);
        convertAndVerifyNumeric(Double.class, Double.class);
        convertAndVerifyNumeric(Byte.class, Byte.class);
        convertAndVerifyNumeric(Integer.TYPE, Integer.class);
        convertAndVerifyNumeric(Long.TYPE, Long.class);
        convertAndVerifyNumeric(Short.TYPE, Short.class);
        convertAndVerifyNumeric(Float.TYPE, Float.class);
        convertAndVerifyNumeric(Double.TYPE, Double.class);
        convertAndVerifyNumeric(Byte.TYPE, Byte.class);
    }

    private void convertAndVerifyNumeric(Class type, Class expected) {
        convertAndVerify(type, expected, "42");
    }

    public void testConvertingBoolean() throws Exception {
        convertAndVerify(Boolean.class, Boolean.class, "True");
        convertAndVerify(Boolean.TYPE, Boolean.class, "True");
    }

    public void testConvertingObjects() throws Exception {
        convertAndVerify(Object.class, Object.class, new Object());
    }

    private void convertAndVerify(Class type, Class expected, Object value) {
        IArgumentConverter converter = new ArgumentConverter(new Class[] {type});
        Object[] converted = converter.convertArguments(new Object[] {value});
        assertTrue(converted[0].getClass().equals(expected));
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
