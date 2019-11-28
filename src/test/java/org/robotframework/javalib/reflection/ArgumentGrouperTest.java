package org.robotframework.javalib.reflection;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.robotframework.javalib.util.ArrayUtil;

public class ArgumentGrouperTest extends TestCase {
    private List providedArguments = Arrays.asList("arg1", "arg2", "arg3", "arg4", "arg5", "arg6");
    private Class<?>[] argumentTypes = new Class[] { String.class, String.class, String.class, String.class, String.class, String.class };

    public void testReturnsOriginalArgumentsIfArgumentCountMatches() throws Exception {
        IArgumentGrouper grouper = new ArgumentGrouper(argumentTypes);
        assertArraysEquals(providedArguments.toArray(), grouper.groupArguments(providedArguments).toArray());
    }

    public void testReturnsOriginalArgumentsIfTheyAreNull() throws Exception {
        IArgumentGrouper grouper = new ArgumentGrouper(argumentTypes);
        assertNull(grouper.groupArguments(null));
    }

    public void testGroupsArgumentsToMatchTheActualArgumentCount() throws Exception {
        for (int i = 1; i <= providedArguments.size(); i++) {
            assertArrayLengthMatches(i);
        }
    }

    public void testStacksAllProvidedArgumentsIfThereIsOnlyOneActualArgument() throws Exception {
        List groupedArguments = new ArgumentGrouper(new Class[] { String.class }).groupArguments(providedArguments);
        assertArraysEquals(providedArguments.toArray(), (Object[])groupedArguments.get(0));
    }

    public void testStacksExcessArguments() throws Exception {
        for (int i = 1; i < providedArguments.size(); i++) {
            assertGroupedCorrectlyWhenActualArgumentCountIs(i);
        }
    }

    public void testStacksArgumentsIfLastArgumentIsOfArrayType() throws Exception {
        Class<?>[] parameterClasses = new Class[] { String.class, String[].class };
        List groupedArguments = new ArgumentGrouper(parameterClasses).groupArguments(Arrays.asList("arg1", "arg2"));
        assertEquals("arg1", groupedArguments.get(0));
        assertArraysEquals(Arrays.asList("arg2").toArray(), (String[])groupedArguments.get(1));
    }

    public void testCanBeCalledWithoutArgumentsIfLastArgumentIsOfArrayType() throws Exception {
        Class<?>[] parameterTypes = new Class[] { String.class, String[].class };
        List groupedArguments = new ArgumentGrouper(parameterTypes).groupArguments(Arrays.asList("arg1"));
        assertEquals("arg1", groupedArguments.get(0));
        assertArraysEquals(new String[0], (String[])groupedArguments.get(0));
    }
    
    public void testCanBeCalledWithoutArgumentsIfOnlyArgumentIsOfArrayType() throws Exception {
        Class<?>[] parameterTypes = new Class[] { String[].class };
        List groupedArguments = new ArgumentGrouper(parameterTypes).groupArguments(Arrays.asList(new Object[0]));
        assertMatricesEquals(Arrays.asList(new String[0]), groupedArguments);
    }
    
    private <T> void assertArraysEquals(T[] expected, T[] actual) {
        ArrayUtil.assertArraysEquals(expected, actual);
    }
    
    private void assertMatricesEquals(List expected, List actual) {
        assertIsMatrice(expected);
        assertIsMatrice(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertArraysEquals((Object[])expected.get(i), (Object[])actual.get(i));
        }
    }
    
    private void assertIsMatrice(List matrix) {
        for (Object array : matrix) {
            assertTrue(array instanceof Object[]);
        }
    }
    
    private void assertArrayLengthMatches(int argumentCount) {
        Class<?>[] parameterClasses = generateParameterClasses(argumentCount);
        int groupedArgumentCount = new ArgumentGrouper(parameterClasses).groupArguments(providedArguments).size();
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
        List groupedArguments = new ArgumentGrouper(generateParameterClasses(actualArgCount)).groupArguments(providedArguments);
        for (int i = 0; i < actualArgCount - 1; i++) {
            assertEquals(providedArguments.get(i), groupedArguments.get(i));
        }

        assertArgumentsAreStackedCorrectly(groupedArguments, actualArgCount - 1);
    }

    private void assertArgumentsAreStackedCorrectly(List groupedArguments, int stackStartIndex) {
        List expectedStackedArguments = providedArguments.subList(stackStartIndex, providedArguments.size());
        
        Object[] actualStackedArguments = (Object[]) groupedArguments.get(stackStartIndex);

        assertArraysEquals(expectedStackedArguments.toArray(), actualStackedArguments);
    }
}
