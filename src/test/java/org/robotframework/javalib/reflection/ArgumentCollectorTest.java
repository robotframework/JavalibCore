package org.robotframework.javalib.reflection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ArgumentCollectorTest {

    private List providedArguments = Arrays.asList("arg", "*varargs", "**kwargs");
    private Class<?>[] argumentTypes = new Class[] { String.class, List.class, Map.class};

    @Test
    void collectArguments() {
        IArgumentCollector collector = new ArgumentCollector(argumentTypes, providedArguments);
        List<String> args = Arrays.asList("1","2");
        Map<String, Object> kwargs = Collections.singletonMap("kw", 3);
        List collectedArgs = collector.collectArguments(args, kwargs);
        assertTrue(collectedArgs.size() == 3);
        assertTrue(collectedArgs.get(1) instanceof List);
        assertTrue(((Map)collectedArgs.get(2)).size() == 1);
    }

    @Test
    void namedArguments() {
        IArgumentCollector collector = new ArgumentCollector(argumentTypes, providedArguments);
        List<String> args = Arrays.asList();
        Map<String, Object> kwargs = Collections.singletonMap("arg", "value");
        List collectedArgs = collector.collectArguments(args, kwargs);
        assertTrue(collectedArgs.get(0) == "value");
        assertTrue(collectedArgs.size() == 3);
        assertTrue(collectedArgs.get(2) instanceof Map);
    }

    @Test
    void varargsTypeInt() {
        List providedArguments = Arrays.asList("arg", "*varargs");
        Class<?>[] argumentTypes = new Class[] { String.class, Integer[].class};
        IArgumentCollector collector = new ArgumentCollector(argumentTypes, providedArguments);
        List<Integer> args = Arrays.asList(2, 3, 4);
        List collectedArgs = collector.collectArguments(args, null);
        assertEquals(2, collectedArgs.size());
        assertTrue(collectedArgs.get(1).getClass().isArray());
        assertEquals(((Integer[])collectedArgs.get(1))[0].getClass(), Integer.class);
        assertEquals(((Integer[])collectedArgs.get(1))[1].getClass(), Integer.class);
    }

    @Test
    void varargsTypeString() {
        List providedArguments = Arrays.asList("arg", "*varargs");
        Class<?>[] argumentTypes = new Class[] { String.class, String[].class};
        IArgumentCollector collector = new ArgumentCollector(argumentTypes, providedArguments);
        List<String> args = Arrays.asList("2", "3", "4");
        List collectedArgs = collector.collectArguments(args, null);
        assertEquals(2, collectedArgs.size());
        assertTrue(collectedArgs.get(1).getClass().isArray());
        assertEquals(String.class, ((Object[])collectedArgs.get(1))[0].getClass());
        assertEquals(String.class, ((Object[])collectedArgs.get(1))[1].getClass());
    }
}
