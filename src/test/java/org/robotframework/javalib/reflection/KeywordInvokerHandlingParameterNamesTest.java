/*
 * Copyright 2008 Nokia Siemens Networks Oyj
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

import com.thoughtworks.paranamer.ParameterNamesNotFoundException;
import com.thoughtworks.paranamer.Paranamer;
import org.jmock.Mock;
import org.robotframework.javalib.util.ArrayUtil;

import java.lang.reflect.Method;

public class KeywordInvokerHandlingParameterNamesTest extends KeywordInvokerTestCase {
    private Mock paranamer;

    protected void setUp() throws Exception {
        paranamer = mock(Paranamer.class);
    }

    public void testReturnsParameterNamesFromArgumentAnnotation() throws Exception {
        KeywordInvoker invoker = new KeywordInvoker(this, getMethod("keywordWithArgumentAnnotation"));
        invoker.parameterNames = (Paranamer) paranamer.proxy();

        paranamer.expects(never()).method("lookupParameterNames");

        ArrayUtil.assertArraysEquals(new String[] {"firstArg", "*args"}, invoker.getParameterNames());
    }

    public void testReturnsParameterNamesFromParameterInformationIfArgumentAnnotationIsNotPresent() throws Exception {
        Method keywordMethod = getMethod("someMethod");

        KeywordInvoker keywordInvoker = new KeywordInvoker(this, keywordMethod);
        keywordInvoker.parameterNames = (Paranamer) paranamer.proxy();

        String[] parameterNames = new String [] { "parameter1", "parameter2" };
        paranamer.expects(once()).method("lookupParameterNames")
            .with(eq(keywordMethod))
            .will(returnValue(parameterNames));

        ArrayUtil.assertArraysEquals(parameterNames, keywordInvoker.getParameterNames());
    }

    public void testReturnsNullParameterNamesIfArgumentAnnotationAndParameterNameInformationIsNotPresent() throws Exception {
        Method keywordMethod = getMethod("someMethod");
        KeywordInvoker keywordInvoker = new KeywordInvoker(this, keywordMethod);
        keywordInvoker.parameterNames = (Paranamer) paranamer.proxy();

        paranamer.expects(once()).method("lookupParameterNames")
            .with(eq(keywordMethod))
            .will(throwException(new ParameterNamesNotFoundException("not found")));

        assertNull(keywordInvoker.getParameterNames());
    }
}
