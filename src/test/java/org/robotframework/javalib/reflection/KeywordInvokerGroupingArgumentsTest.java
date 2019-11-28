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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jmock.Mock;
import org.robotframework.javalib.util.ArrayUtil;

public class KeywordInvokerGroupingArgumentsTest extends KeywordInvokerTestCase {
    private Mock argumentGrouper;
    
    protected void setUp() throws Exception {
        argumentGrouper = mock(IArgumentGrouper.class);
    }
    
    public void testGroupsRestOfTheArgumentsIfProvidedArgumentCountIsGreaterThanActualArgumentCount() throws Exception {
        List providedArguments = Arrays.asList("arg1", "arg2", "arg3");
        List groupedArguments = Arrays.asList("arg1", new String[] { "arg2", "arg3" });

        argumentGrouper.expects(once()).method("groupArguments")
            .with(same(providedArguments))
            .will(returnValue(groupedArguments));
        
        IKeywordInvoker invoker = createKeywordInvokerWithMockArgumentGrouper("someMethod");

        invoker.invoke(providedArguments, null);
        ArrayUtil.assertArraysEquals((Object[]) groupedArguments.get(1), restOfArgs);
    }
    
    public void testProvidesEmptyArgumentIfNoArgumentsProvided() throws Exception {
        List providedArguments = new ArrayList<String>();
        List emptyArgument = new ArrayList<String>();

        argumentGrouper.expects(once()).method("groupArguments")
            .with(same(providedArguments))
            .will(returnValue(emptyArgument));

        IKeywordInvoker invoker = createKeywordInvokerWithMockArgumentGrouper("keywordWithVariableArgCount");

        invoker.invoke(providedArguments, null);
    }

    private IKeywordInvoker createKeywordInvokerWithMockArgumentGrouper(String methodName) {
        Method method = getMethod(methodName);
        return new KeywordInvoker(this, method) {
            IArgumentGrouper createArgumentGrouper() {
                return (IArgumentGrouper) argumentGrouper.proxy();
            }
        };
    }
}
