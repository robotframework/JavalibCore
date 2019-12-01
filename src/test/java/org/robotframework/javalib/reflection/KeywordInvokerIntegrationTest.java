package org.robotframework.javalib.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.robotframework.javalib.keyword.AnnotatedKeywords;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;


public class KeywordInvokerIntegrationTest {

    @Test
    public void testReturnsParameterNames() throws Exception {
        List expectedParameterNames = Arrays.asList("arg" );
        assertIterableEquals(expectedParameterNames, getParameterNamesFromMethod("keywordThatReturnsItsArguments"));
    }

    @Test
    public void testFindsKeywordArgumentsWithKeywordArgumentsAnnotation() throws Exception {
        List expectedParameterNames = Arrays.asList("overridenArgumentName" );
        assertIterableEquals(expectedParameterNames, getParameterNamesFromMethod("someKeyword"));
    }

    private List getParameterNamesFromMethod(String string) throws NoSuchMethodException {
        IKeywordInvoker keywordInvoker = createKeywordInvoker(string);
        return keywordInvoker.getParameterNames();
    }

    private IKeywordInvoker createKeywordInvoker(String methodName) throws NoSuchMethodException {
        Method method = AnnotatedKeywords.class.getMethod(methodName, String.class);
        return new KeywordInvoker(this, method);
    }
}
