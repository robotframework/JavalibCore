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

package org.robotframework.javalib.beans.common;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @see BeanDefinitionReader
 */
public class KeywordBeanDefinitionReader implements IKeywordBeanDefintionReader {
    private ClassFinder classFinder;
    private BeanDefinitionRegistry registry;

    /**
     * Constructs a reader for the given registry with given classloader.
     *
     * @param registry registry to register beans to
     * @param classLoader classloader to use
     */
    public KeywordBeanDefinitionReader(BeanDefinitionRegistry registry, ClassLoader classLoader) {
        this.registry = registry;
        this.classFinder = new DefaultClassFinder(classLoader);
    }

    public int loadBeanDefinitions(String pattern, IClassFilter filter) {
        int acceptedClasses = 0;

        Class[] classes = classFinder.getClasses("classpath*:" + pattern);
        for (int i = 0; i < classes.length; i++) {
            Class clazz = classes[i];
            if (filter.accept(clazz)) {
                registerKeywordBean(clazz);
                acceptedClasses++;
            }
        }
        return acceptedClasses;
    }

    public void setClassFinder(ClassFinder classFinder) {
        this.classFinder = classFinder;
    }

    private void registerKeywordBean(Class clazz) {
        String keywordName = extractKeywordName(clazz.getName());
        BeanDefinition beanDefinition = new RootBeanDefinition(clazz);
        registry.registerBeanDefinition(keywordName, beanDefinition);
    }

    private String extractKeywordName(String className) {
        int index = className.lastIndexOf(".");
        return className.substring(index + 1);
    }
}
