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

import org.robotframework.javalib.factory.KeywordFactory;
import org.robotframework.javalib.keyword.Keyword;

/**
 * A library that wraps a keyword factory. The keyword factory is used
 * to create the keyword instance and this library simply executes the
 * keyword. Subclasses must implement factory method
 * {@link #createKeywordFactory()}.
 */
public abstract class KeywordFactoryBasedLibrary<T extends Keyword> implements RobotJavaLibrary {
    private KeywordFactory<T> keywordFactory;
    private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    /**
     * @see RobotJavaLibrary#runKeyword(String, Object[])
     */
    public Object runKeyword(String keywordName, Object[] args) {
        Keyword keyword = getKeywordFactory().createKeyword(keywordName);
        return keyword.execute(args);
    }

    /**
     * @see RobotJavaLibrary#getKeywordNames()
     */
    public String[] getKeywordNames() {
        return getKeywordFactory().getKeywordNames();
    }

    /**
     * Gets the classloader. Simply a property that the subclasses can use
     * if the need to. The default classloader is the current thread's
     * context class loader, {@link Thread#getContextClassLoader()}.
     *
     * @return classloader
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Sets the classloader. Simply a property that the subclasses can use
     * if the need to.
     *
     * @param classLoader new classloader
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Creates a keyword factory. Must be implemented by subclasses.
     * The keyword factory is created lazily, when either
     * {@link #getKeywordNames()} or {@link #runKeyword(String, Object[])}
     * is called for the first time.
     *
     * @return keyword factory
     */
    protected abstract KeywordFactory<T> createKeywordFactory();

    KeywordFactory<T> getKeywordFactory() {
        if (keywordFactory == null) {
            keywordFactory = createKeywordFactory();
        }
        return keywordFactory;
    }
}
