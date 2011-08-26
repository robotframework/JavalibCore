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

package org.robotframework.javalib.beans.annotation;

import java.util.Map;

import org.robotframework.javalib.beans.common.IClassFilter;
import org.robotframework.javalib.beans.common.IKeywordBeanDefintionReader;
import org.robotframework.javalib.beans.common.KeywordBeanDefinitionReader;
import org.robotframework.javalib.context.KeywordApplicationContext;
import org.robotframework.javalib.util.KeywordNameNormalizer;
import org.springframework.context.support.GenericApplicationContext;

public class KeywordBeanLoader implements IBeanLoader {
    protected GenericApplicationContext context = new KeywordApplicationContext(new KeywordNameNormalizer());
    protected IKeywordBeanDefintionReader beanDefinitionReader = new KeywordBeanDefinitionReader(context, Thread.currentThread().getContextClassLoader());
    protected String keywordPattern = null;

    public KeywordBeanLoader(String keywordPattern) {
        this.keywordPattern = keywordPattern;
    }

    public Map loadBeanDefinitions(IClassFilter classFilter) {
        beanDefinitionReader.loadBeanDefinitions(keywordPattern, classFilter);
        context.refresh();
        return context.getBeansOfType(Object.class);
    }
}
