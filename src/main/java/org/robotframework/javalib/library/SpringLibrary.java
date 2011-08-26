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

import org.robotframework.javalib.beans.spring.KeywordsXmlBeanDefinitionReader;
import org.robotframework.javalib.context.KeywordApplicationContext;
import org.robotframework.javalib.factory.ApplicationContextKeywordFactory;
import org.robotframework.javalib.factory.KeywordFactory;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.util.KeywordNameNormalizer;


/**
 * <p>
 * A Robot keyword library that uses Spring configuration files for defining
 * keywords.
 * </p>
 *
 * <p>
 * Before using this library, you must set the configuration file pattern
 * with {@link #setConfigFilePattern(String)} so that it will find your
 * configuration files.
 * </p>
 *
 * <p>
 * The library finds all the configuration files matching the pattern.
 * All the defined beans that implement the {@link Keyword} interface are
 * available as keywords.
 * </p>
 */
public class SpringLibrary extends KeywordFactoryBasedLibrary<Keyword> {
    private String configFilePattern = null;

    public SpringLibrary(String configFilePattern) {
        setConfigFilePattern(configFilePattern);
    }

    public SpringLibrary() {}

    /**
     * @see KeywordFactoryBasedLibrary#createKeywordFactory()
     */
    protected KeywordFactory<Keyword> createKeywordFactory() {
        assumeConfigFilePatternIsSet();
        KeywordApplicationContext ctx = new KeywordApplicationContext(new KeywordNameNormalizer());
        ctx.setClassLoader(getClassLoader());

        KeywordsXmlBeanDefinitionReader reader = new KeywordsXmlBeanDefinitionReader(ctx, getClassLoader());
        reader.loadBeanDefinitions(configFilePattern);

        ctx.refresh();
        return new ApplicationContextKeywordFactory(ctx);
    }

    /**
     * Sets the pattern. See class description for details. Must
     * be set before Robot calls
     * {@link RobotJavaLibrary#runKeyword(String, Object[])} or
     * {@link RobotJavaLibrary#getKeywordNames()}.
     *
     * @param pattern pattern
     */
    public void setConfigFilePattern(String pattern) {
        this.configFilePattern = pattern;
    }

    private void assumeConfigFilePatternIsSet() {
        if (configFilePattern == null)
            throw new IllegalStateException("Config file pattern must be set before calling getKeywordNames.");
    }
}
