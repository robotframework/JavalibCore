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

import org.robotframework.javalib.keyword.Keyword;

/**
 * Creates instances of keywords.
 */
public interface KeywordFactory<T extends Keyword> {
    /**
     * Creates an instance of the class implementing the given keyword
     * name
     *
     * @param keywordName keyword name (will be normalized, so pretty much
     *                       any formatting will do)
     * @return keyword instance
     */
    T createKeyword(String keywordName);

    /**
     * Returns all the names of the keywords that this factory can create
     *
     * @return names of available keywords
     */
    String[] getKeywordNames();
}
