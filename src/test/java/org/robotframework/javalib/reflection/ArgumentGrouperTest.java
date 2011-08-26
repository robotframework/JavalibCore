package org.robotframework.javalib.reflection;

import junit.framework.TestCase;

import org.robotframework.javalib.util.ArrayUtil;

public class ArgumentGrouperTest extends TestCase {
    private String[] providedArguments = new String[] { "arg1", "arg2", "arg3", "arg4", "arg5", "arg6" };
    private Class<?>[] argumentTypes = new Class[] { String.class, String.class, String.class, String.class, String.class, String.class };

    public void testReturnsOriginalArgumentsIfArgumentCountMatches() throws Exception {
        IArgumentGrouper grouper = new ArgumentGrouper(argumentTypes);
        assertArraysEquals(providedArguments, grouper.groupArguments(providedArguments));
    }

    public void testReturnsOriginalArgumentsIfTheyAreNull() throws Exception {
        IArgumentGrouper grouper = new ArgumentGrouper(argumentTypes);
        assertNull(grouper.groupArguments(null));
    }

    public void testGroupsArgumentsToMatchTheActualArgumentCount() throws Exception {
        for (int i = 1; i <= providedArguments.length; i++) {
            assertArrayLengthMatches(i);
        }
    }

    public void testStacksAllProvidedArgumentsIfThereIsOnlyOneActualArgument() throws Exception {
        Object[] groupedArguments = new ArgumentGrouper(new Class[] { String.class }).groupArguments(providedArguments);
        assertArraysEquals(providedArguments, (String[]) groupedArguments[0]);
    }

    public void testStacksExcessArguments() throws Exception {
        for (int i = 1; i < providedArguments.length; i++) {
            assertGroupedCorrectlyWhenActualArgumentCountIs(i);
        }
    }

    public void testStacksArgumentsIfLastArgumentIsOfArrayType() throws Exception {
        Class<?>[] parameterClasses = new Class[] { String.class, String[].class };
        Object[] groupedArguments = new ArgumentGrouper(parameterClasses).groupArguments(new String[] { "arg1", "arg2" });
        assertEquals("arg1", groupedArguments[0]);
        assertArraysEquals(new String[] { "arg2" }, (String[])groupedArguments[1]);
    }

    public void testCanBeCalledWithoutArgumentsIfLastArgumentIsOfArrayType() throws Exception {
        Class<?>[] parameterTypes = new Class[] { String.class, String[].class };
        Object[] groupedArguments = new ArgumentGrouper(parameterTypes).groupArguments(new String[] { "arg1" });
        assertEquals("arg1", groupedArguments[0]);
        assertArraysEquals(new String[0], (String[])groupedArguments[1]);
    }
    
    public void testCanBeCalledWithoutArgumentsIfOnlyArgumentIsOfArrayType() throws Exception {
        Class<?>[] parameterTypes = new Class[] { String[].class };
        Object[] groupedArguments = new ArgumentGrouper(parameterTypes).groupArguments(new Object[0]);
        assertMatricesEquals(new Object[] { new String[0] }, groupedArguments);
    }
    
    private <T> void assertArraysEquals(T[] expected, T[] actual) {
        ArrayUtil.assertArraysEquals(expected, actual);
    }
    
    private void assertMatricesEquals(Object[] expected, Object[] actual) {
        assertIsMatrice(expected);
        assertIsMatrice(actual);
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertArraysEquals((Object[])expected[i], (Object[])actual[i]);
        }
    }
    
    private void assertIsMatrice(Object[] matrix) {
        for (Object array : matrix) {
            assertTrue(array instanceof Object[]);
        }
    }
    
    private void assertArrayLengthMatches(int argumentCount) {
        Class<?>[] parameterClasses = generateParameterClasses(argumentCount);
        int groupedArgumentCount = new ArgumentGrouper(parameterClasses).groupArguments(providedArguments).length;
        assertEquals(groupedArgumentCount, argumentCount);
    }
    
    private Class<?>[] generateParameterClasses(int argumentCount) {
        Class<?>[] types = new Class[argumentCount];
        for (int i = 0; i < types.length; i++) {
            types[i] = String.class;
        }
        return types;
    }

    private void assertGroupedCorrectlyWhenActualArgumentCountIs(int actualArgCount) {
        Object[] groupedArguments = new ArgumentGrouper(generateParameterClasses(actualArgCount)).groupArguments(providedArguments);
        for (int i = 0; i < actualArgCount - 1; i++) {
            assertEquals(providedArguments[i], groupedArguments[i]);
        }

        assertArgumentsAreStackedCorrectly(groupedArguments, actualArgCount - 1);
    }

    private void assertArgumentsAreStackedCorrectly(Object[] groupedArguments, int stackStartIndex) {
        String[] expectedStackedArguments = ArrayUtil.copyOfRange(providedArguments, stackStartIndex, providedArguments.length);
        Object[] actualStackedArguments = (Object[]) groupedArguments[stackStartIndex];

        assertArraysEquals(expectedStackedArguments, actualStackedArguments);
    }
}
