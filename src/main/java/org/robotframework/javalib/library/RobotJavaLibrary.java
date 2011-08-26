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

/**
 * A Java library for Robot Framework.
 */
public interface RobotJavaLibrary {
    /**
     * Runs a keyword and returns the result. If an exception is thrown
     * the keyword fails, otherwise it passes.
     * 
     * @param keywordName keyword name to run
     * @param args arguments for the keyword
     * @return keyword return value
     */
    public Object runKeyword(String keywordName, Object[] args);
    
    /**
     * Returns all the keywords this library contains
     * 
     * @return names of keywords this library contains
     */
    public String[] getKeywordNames();
}
