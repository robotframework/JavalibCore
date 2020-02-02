/*
 * Copyright 2013 Nokia Solutions and Networks Oyj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.robotframework.javalib.reflection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class KeywordInvokerGroupingArgumentsTest {
    private static TestKeywordInvoker testKeywordInvoker = new TestKeywordInvoker();

    @Test
    public void testSingleArgumentWithMapForKeywordInvoker() {
        Parameter mockParam = mock(Parameter.class);
        doReturn(Map.class).when(mockParam).getType();
        when(mockParam.getName()).thenReturn("arg0");
        Parameter[] parameters = new Parameter[]{mockParam};
        Method mockedMethod = mock(Method.class);
        when(mockedMethod.getParameterCount()).thenReturn(1);
        when(mockedMethod.getParameters()).thenReturn(parameters);
        doReturn(new Class[]{Map.class}).when(mockedMethod).getParameterTypes();
        KeywordInvoker keywordInvoker = new KeywordInvoker(new Object(), mockedMethod);
        List<String> parameterNames = keywordInvoker.getParameterNames();
        assertEquals(1, parameterNames.size());
        assertEquals("**arg0", parameterNames.get(0));

        List args = Arrays.asList(buildHashMap());
        ArgumentCollector argumentCollector = new ArgumentCollector(mockedMethod.getParameterTypes(), parameterNames);

        List collectedArgs = argumentCollector.collectArguments(args, null);
        // since the map has 3 entries
        assertEquals(3, collectedArgs.size());
    }

    private Map buildHashMap() {
        Map map = new HashMap();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        return map;
    }

    private Parameter[] buildParameters() {
        Parameter mockParam = mock(Parameter.class);
        doReturn(Map.class).when(mockParam).getType();
        when(mockParam.getName()).thenReturn("arg0");
        return new Parameter[]{mockParam};
    }

    @Test
    public void testGroupsRestOfTheArgumentsIfProvidedArgumentCountIsGreaterThanActualArgumentCount() {
        List providedArguments = Arrays.asList("arg1", "arg2", "arg3");
        List groupedArguments = Arrays.asList("arg1", new String[] { "arg2", "arg3" });

        KeywordInvoker spyInvoker = spy(new KeywordInvoker(testKeywordInvoker, testKeywordInvoker.getMethod("someMethod")));
        ArgumentCollector spyCollector = spy(new ArgumentCollector(null, null));
        when(spyCollector.collectArguments(providedArguments, null)).thenReturn(groupedArguments);
        when(spyInvoker.createArgumentCollector()).thenReturn(spyCollector);

        spyInvoker.invoke(providedArguments, null);
        assertIterableEquals(Arrays.asList((Object[]) groupedArguments.get(1)), Arrays.asList(testKeywordInvoker.restOfArgs));
    }

    @Test
    public void testProvidesEmptyArgumentIfNoArgumentsProvided() {
        List providedArguments = Arrays.asList();
        List groupedArguments = Arrays.asList(new String[1]);

        KeywordInvoker spyInvoker = spy(new KeywordInvoker(testKeywordInvoker, testKeywordInvoker.getMethod("keywordWithVariableArgCount")));
        ArgumentCollector spyCollector = spy(new ArgumentCollector(null, null));
        when(spyCollector.collectArguments(providedArguments, null)).thenReturn(groupedArguments);
        when(spyInvoker.createArgumentCollector()).thenReturn(spyCollector);

        spyInvoker.invoke(providedArguments, null);
    }

}
