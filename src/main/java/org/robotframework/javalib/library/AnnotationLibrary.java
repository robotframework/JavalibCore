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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.robotframework.javalib.beans.annotation.AnnotationBasedKeywordFilter;
import org.robotframework.javalib.beans.annotation.IBeanLoader;
import org.robotframework.javalib.beans.annotation.KeywordBeanLoader;
import org.robotframework.javalib.beans.common.IClassFilter;
import org.robotframework.javalib.factory.AnnotationKeywordFactory;
import org.robotframework.javalib.factory.KeywordFactory;
import org.robotframework.javalib.keyword.DocumentedKeyword;

public class AnnotationLibrary extends KeywordFactoryBasedLibrary<DocumentedKeyword> implements KeywordDocumentationRepository {
    protected List<IBeanLoader> beanLoaders = new ArrayList<IBeanLoader>();
    protected IClassFilter classFilter = new AnnotationBasedKeywordFilter();
    private KeywordFactory<DocumentedKeyword> keywordFactory;

    public AnnotationLibrary() {}

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
        }
        return keywordFactory;
    }

    public String[] getKeywordArguments(String keywordName) {
        return createKeywordFactory().createKeyword(keywordName).getArgumentNames();
    }

    public String getKeywordDocumentation(String keywordName) {
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
        beanLoaders.add(new KeywordBeanLoader(keywordPattern));
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
