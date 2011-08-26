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

package org.robotframework.javalib.beans.spring;

import java.io.IOException;

import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * @see BeanDefinitionReader
 */
public class KeywordsXmlBeanDefinitionReader {
    private ResourcePatternResolver resourcePatternResolver;
    private BeanDefinitionRegistry registry;

    /**
     * Constructs a reader for the given registry with given classloader.
     * 
     * @param registry registry to register beans to
     * @param classLoader classloader to use
     */
    public KeywordsXmlBeanDefinitionReader(BeanDefinitionRegistry registry, ClassLoader classLoader) {
        this.registry = registry;
        resourcePatternResolver = new PathMatchingResourcePatternResolver(classLoader);
    }
    
    /**
     * Loads bean definitions from configuration files found from classpath
     * with given pattern. By default the pattern is resolved with 
     * {@link PathMatchingResourcePatternResolver} and prefixed with
     * "classpath*:" so we also look inside jar files.
     * 
     * @param pattern pattern to resolve configuration files, see {@link PathMatchingResourcePatternResolver}
     * @return number of bean definitions loaded
     */
    public int loadBeanDefinitions(String pattern) {
        Resource[] configurationFiles = getConfigurationFiles(pattern);
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);

        return reader.loadBeanDefinitions(configurationFiles);
    }

    private Resource[] getConfigurationFiles(String pattern) {
        try {
            return resourcePatternResolver.getResources("classpath*:" + pattern);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The default {@link ResourcePatternResolver} is a
     * {@link PathMatchingResourcePatternResolver}. This must be set prior to 
     * calling {@link #loadBeanDefinitions(String)} for it to have an
     * effect. 
     * 
     * @param resolver new resolver
     */
    public void setResourcePatternResolver(ResourcePatternResolver resolver) {
        this.resourcePatternResolver = resolver;
    }
}
