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

import org.robotframework.javalib.beans.classpath.InterfaceBasedKeywordFilter;
import org.robotframework.javalib.beans.common.IKeywordBeanDefintionReader;
import org.robotframework.javalib.beans.common.KeywordBeanDefinitionReader;
import org.robotframework.javalib.context.KeywordApplicationContext;
import org.robotframework.javalib.factory.ApplicationContextKeywordFactory;
import org.robotframework.javalib.factory.KeywordFactory;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.util.KeywordNameNormalizer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;


/**
 * <p>
 * A Robot keyword library that finds keywords dynamically from the
 * classpath.
 * </p>
 *
 * <p>
 * To add keywords to the library, first make sure that the pattern and
 * package prefix are correct. The pattern is used to find potential
 * keywords according to their name. E.g. if your class name is
 * <code>com.acme.mycomponent.keyword.DoSomething</code> and you want it
 * to be registered as a keyword, the keyword pattern could be
 * <code>com/&#42;&#42;/keyword/&#42;&#42;/&#42;&#42;.class</code>. Try to
 * set it as restrictive as possible in order to avoid keyword name collisions
 * with other libraries using javalib-core. For example, it is perhaps not
 * a good idea to set it to <code>com/&#42;&#42;/&#42;&#42;.class</code>.
 * The pattern is resolved by {@link PathMatchingResourcePatternResolver}.
 * </p>
 *
 * <p>
 * The package prefix is used to determine the class path location of the
 * class from the filesystem path. Do not use it for filtering out
 * keywords, that's what the pattern is for. The package prefix must
 * match for all keywords the pattern finds. The default package prefix
 * is "org.".
 * </p>
 *
 * <p>
 * Then make sure your keyword fills all the requirements of
 * {@link InterfaceBasedKeywordFilter}.
 * </p>
 *
 * <p>
 * After that, all you need to do is to add your keywords to your class
 * path and this library will automatically find them. Contents of JAR
 * files are also searched.
 * </p>
 */
public class ClassPathLibrary extends KeywordFactoryBasedLibrary<Keyword> {
    private String keywordPattern = null;

    public ClassPathLibrary() { }

    public ClassPathLibrary(String keywordPattern) {
        setKeywordPattern(keywordPattern);
    }

    @Override
    protected KeywordFactory<Keyword> createKeywordFactory() {
        assumeKeywordPatternIsSet();
        KeywordApplicationContext ctx = new KeywordApplicationContext(new KeywordNameNormalizer());
        IKeywordBeanDefintionReader reader = new KeywordBeanDefinitionReader(ctx, getClassLoader());
        reader.loadBeanDefinitions(keywordPattern, new InterfaceBasedKeywordFilter());
        ctx.refresh();
        return new ApplicationContextKeywordFactory(ctx);
    }

    /**
     * Sets a new pattern. See class description for details. Must
     * be set before Robot calls
     * {@link RobotJavaLibrary#runKeyword(String, Object[])} or
     * {@link RobotJavaLibrary#getKeywordNames()}.
     *
     * @param pattern new pattern
     */
    public void setKeywordPattern(String pattern) {
        this.keywordPattern = pattern;
    }

    private void assumeKeywordPatternIsSet() {
        if (keywordPattern == null) {
            throw new IllegalStateException("Keyword pattern must be set before calling getKeywordNames.");
        }
    }
}
