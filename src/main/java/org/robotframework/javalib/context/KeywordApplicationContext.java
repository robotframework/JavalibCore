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

package org.robotframework.javalib.context;

import org.robotframework.javalib.util.IKeywordNameNormalizer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;

/**
 * An application context for keyword beans. Decorates methods
 * by normalizing bean names while registering and getting beans.
 */
public class KeywordApplicationContext extends GenericApplicationContext {
    private IKeywordNameNormalizer keywordNameNormalizer;

    public KeywordApplicationContext(IKeywordNameNormalizer keywordNameNormalizer) {
        super(new DefaultListableBeanFactory() {
            public void preInstantiateSingletons() throws BeansException {
                // empty implementation in order to disable preinstantiation.
            }
        });
        this.keywordNameNormalizer = keywordNameNormalizer;
    }

    /**
     * Registers a bean definition with original id and creates a normalized alias.
     * @see BeanDefinitionRegistry#registerBeanDefinition(String, BeanDefinition)
     */
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) throws BeansException {
        String normalizedName = keywordNameNormalizer.normalize(name);
        super.registerBeanDefinition(name, beanDefinition);
        registerAlias(name, normalizedName);
    }

    /**
     * Gets a bean. Name will be normalized.
     * @see BeanFactory#getBean(String)
     */
    public Object getBean(String name) throws BeansException {
        return super.getBean(keywordNameNormalizer.normalize(name));
    }

    public String[] getBeanNamesForType(Class type) {
        String[] beanNames = super.getBeanNamesForType(type);
        for (int i = 0; i < beanNames.length; i++) {
            beanNames[i] = keywordNameNormalizer.normalize(beanNames[i]);
        }

        return beanNames;
    }
}
