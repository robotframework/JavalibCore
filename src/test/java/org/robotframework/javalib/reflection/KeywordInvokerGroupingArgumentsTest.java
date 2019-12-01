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

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

public class KeywordInvokerGroupingArgumentsTest {
    private static TestKeywordInvoker testKeywordInvoker = new TestKeywordInvoker();

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
