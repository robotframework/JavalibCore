package org.robotframework.javalib.library;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.robotframework.javalib.beans.annotation.IBeanLoader;
import org.robotframework.javalib.beans.common.IClassFilter;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class AnnotationLibraryLoadingBeansTest {
    private AnnotationLibrary annotationLibrary = new AnnotationLibrary();

    @Test
    public void loadsKeywordClassesWithBeanLoader() throws Exception {
        injectBeanDefinitionsToAnnotationLibrary();
        List expectedKeywordNames = Arrays.asList("someKeyword", "anotherKeyword");
        assertIterableEquals(expectedKeywordNames, annotationLibrary.getKeywordNames());
    }

    private void injectBeanDefinitionsToAnnotationLibrary() {
        IBeanLoader mockBeanLoader = Mockito.mock(IBeanLoader.class);
        IClassFilter mockClassFilter = Mockito.mock(IClassFilter.class);

        List<IBeanLoader> beanLoaders = new ArrayList<IBeanLoader>();
        beanLoaders.add(mockBeanLoader);
        annotationLibrary.beanLoaders = beanLoaders;
        annotationLibrary.classFilter = mockClassFilter;

        Mockito.when(mockBeanLoader.loadBeanDefinitions(mockClassFilter)).thenReturn(createKeywordBeans());
    }

    private Map createKeywordBeans() {
        return new HashMap() {{
            put("keywordsBean1", new SomeKeywords());
            put("keywordsBean2", new AnotherKeywords());
        }};
    }

    @RobotKeywords
    private static class SomeKeywords {
        @RobotKeyword
        public void someKeyword() { }
    }

    @RobotKeywords
    private static class AnotherKeywords {
        @RobotKeyword
        public void anotherKeyword() { }
    }
}
