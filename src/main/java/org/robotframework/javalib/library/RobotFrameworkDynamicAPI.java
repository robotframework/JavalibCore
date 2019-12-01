/*
 * Copyright 2013 Nokia Solutions and Networks Oyj
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

import java.util.List;
import java.util.Map;

/**
 * A Java library for Robot Framework.
 */
public interface RobotFrameworkDynamicAPI {

    /**
     * Returns all the keywords this library contains
     *
     * @return names of keywords this library contains
     */
    List<String> getKeywordNames();

    /**
     * Runs a keyword and returns the result. If an exception is thrown
     * the keyword fails, otherwise it passes.
     *
     * @param name keyword name to run
     * @param arguments arguments for the keyword
     * @return keyword return value
     */
    Object runKeyword(String name, List arguments);

    /**
     * Runs a keyword and returns the result. If an exception is thrown
     * the keyword fails, otherwise it passes.
     *
     * @param name keyword name to run
     * @param arguments arguments for the keyword
     * @param kwargs named arguments for the keyword
     * @return keyword return value
     */
    Object runKeyword(String name, List arguments, Map kwargs);

//    List<String> getKeywordArguments(String name);
//
//    List<String> getKeywordTypes(String name);
//
////    List<String> getKeywordTags(String name);
//
//    String getKeywordDocumentation(String name);
}
