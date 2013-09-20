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

package org.robotframework.javalib.library;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.beans.annotation.AnnotationBasedKeywordFilter;
import org.robotframework.javalib.beans.annotation.IBeanLoader;
import org.robotframework.javalib.beans.annotation.KeywordBeanLoader;
import org.robotframework.javalib.beans.common.IClassFilter;
import org.robotframework.javalib.factory.AnnotationKeywordFactory;
import org.robotframework.javalib.factory.KeywordFactory;
import org.robotframework.javalib.keyword.DocumentedKeyword;

public class AnnotationLibrary extends KeywordFactoryBasedLibrary<DocumentedKeyword> implements
        KeywordDocumentationRepository {
    protected List<IBeanLoader> beanLoaders = new ArrayList<IBeanLoader>();
    protected IClassFilter classFilter = new AnnotationBasedKeywordFilter();
    private KeywordFactory<DocumentedKeyword> keywordFactory;

    public AnnotationLibrary() {
    }

    public AnnotationLibrary(String keywordPattern) {
        addKeywordPattern(keywordPattern);
    }

    public AnnotationLibrary(List<String> keywordPatterns) {
        for (String pattern : keywordPatterns) {
            addKeywordPattern(pattern);
        }
    }

    @Override
    protected KeywordFactory<DocumentedKeyword> createKeywordFactory() {
        assumeKeywordPatternIsSet();
        if (keywordFactory == null) {
            List<Map> keywordBeansMaps = new ArrayList<Map>();
            for (IBeanLoader beanLoader : beanLoaders) {
                keywordBeansMaps.add(beanLoader.loadBeanDefinitions(classFilter));
            }
            keywordFactory = new AnnotationKeywordFactory(keywordBeansMaps);

            List<Object> injectionValues = new ArrayList<Object>();
            injectionValues.add(this);
            for (Map keywordBeansMap : keywordBeansMaps) {
                injectionValues.addAll(keywordBeansMap.values());
            }
            for (Object injectionTarget : injectionValues) {
                autowireFields(injectionTarget, injectionValues);
            }
        }
        return keywordFactory;
    }

    protected void autowireFields(Object injectionTarget, Collection<Object> injectionValues) {
        Class<?> objectClass = injectionTarget.getClass();
        while (objectClass != null) {
            Field[] fields = objectClass.getDeclaredFields();
            next_field: for (final Field field : fields) {
                try {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass()
                                .getModifiers())) && !field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        Class<?> fieldClass = field.getType();
                        for (Object injectionValue : injectionValues) {
                            if (injectionValue.getClass().equals(fieldClass)) {
                                field.set(injectionTarget, injectionValue);
                                continue next_field;
                            }
                        }
                        throw new IllegalArgumentException(String.format(
                                "Can't autowire field '%s' at keyword class '%s'.", field.getName(), injectionTarget
                                        .getClass().getName()));
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(String.format(
                            "Can't autowire field '%s' at keyword class '%s'.", field.getName(), injectionTarget
                                    .getClass().getName()), e);
                }
            }
            objectClass = objectClass.getSuperclass();
        }
    }

    public String[] getKeywordArguments(String keywordName) {
        return createKeywordFactory().createKeyword(keywordName).getArgumentNames();
    }

    /**
     * This method should be overridden in the Library implementation including
     * the equals comparison for '__intro__'.
     * 
     * Default implementation returns empty String for the '__intro__'.
     */
    public String getKeywordDocumentation(String keywordName) {
        if (keywordName.equals("__intro__"))
            return "";
        return createKeywordFactory().createKeyword(keywordName).getDocumentation();
    }

    @Override
    public Object runKeyword(String keywordName, Object[] args) {
        try {
            return super.runKeyword(keywordName, args);
        } catch (RuntimeException e) {
            throw retrieveInnerException(e);
        }
    }

    public void addKeywordPattern(String keywordPattern) {
        beanLoaders.add(new KeywordBeanLoader(keywordPattern, Thread.currentThread().getContextClassLoader()));
    }

    private void assumeKeywordPatternIsSet() {
        if (beanLoaders.isEmpty()) {
            throw new IllegalStateException("Keyword pattern must be set before calling getKeywordNames.");
        }
    }

    private RuntimeException retrieveInnerException(RuntimeException e) {
        Throwable cause = e.getCause();
        if (InvocationTargetException.class.equals(cause.getClass())) {
            Throwable original = cause.getCause();
            return new RuntimeException(original.getMessage(), original);
        }

        return e;
    }
}
