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

package org.robotframework.javalib.factory;

import org.robotframework.javalib.context.KeywordApplicationContext;
import org.robotframework.javalib.keyword.Keyword;
import org.springframework.context.ApplicationContext;

/**
 * Keyword factory that wraps an {@link ApplicationContext}. To be used
 * with {@link KeywordApplicationContext}, otherwise keyword names will
 * not be normalized.
 *
 * @see KeywordApplicationContext
 */
public class ApplicationContextKeywordFactory implements KeywordFactory<Keyword> {
    private ApplicationContext ctx;

    /**
     * @param ctx {@link ApplicationContext} to use. Preferably a
     * {@link KeywordApplicationContext}.
     */
    public ApplicationContextKeywordFactory(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * @see KeywordFactory#createKeyword(String)
     */
    public Keyword createKeyword(String keywordName) {
        return (Keyword) ctx.getBean(keywordName);
    }

    /**
     * @see KeywordFactory#getKeywordNames()
     */
    public String[] getKeywordNames() {
        return ctx.getBeanNamesForType(Keyword.class);
    }

    /**
     * Returns the underlying ApplicationContext's class loader.
     *
     * @return class loader
     */
    public ClassLoader getClassLoader() {
        return ctx.getClassLoader();
    }
}
