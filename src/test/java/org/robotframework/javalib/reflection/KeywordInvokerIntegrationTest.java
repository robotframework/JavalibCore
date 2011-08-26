package org.robotframework.javalib.reflection;

import java.lang.reflect.Method;

import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.keyword.AnnotatedKeywords;
import org.robotframework.javalib.util.ArrayUtil;


public class KeywordInvokerIntegrationTest extends MockObjectTestCase {
    public void testReturnsParameterNames() throws Exception {
        String[] expectedParameterNames = new String [] { "arg" };
        ArrayUtil.assertArraysEquals(expectedParameterNames, getParameterNamesFromMethod("keywordThatReturnsItsArguments"));
    }

    public void testFindsKeywordArgumentsWithKeywordArgumentsAnnotation() throws Exception {
        String[] expectedParameterNames = new String [] { "overridenArgumentName" };
        ArrayUtil.assertArraysEquals(expectedParameterNames, getParameterNamesFromMethod("someKeyword"));
    }

    private String[] getParameterNamesFromMethod(String string) throws NoSuchMethodException {
        IKeywordInvoker keywordInvoker = createKeywordInvoker(string);
        return keywordInvoker.getParameterNames();
    }

    private IKeywordInvoker createKeywordInvoker(String methodName) throws NoSuchMethodException {
        Method method = AnnotatedKeywords.class.getMethod(methodName, String.class);
        return new KeywordInvoker(this, method);
    }
}
