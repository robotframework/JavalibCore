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

package org.robotframework.javalib.beans.classpath;

import org.robotframework.javalib.beans.common.BasicKeywordFilter;
import org.robotframework.javalib.keyword.Keyword;

/**
 * A filter for classes that implement the {@link Keyword} interface.
 * This filter extends the basic keyword filter and adds a condition to check
 * for the implementation of the Keyword interface.
 */

public class InterfaceBasedKeywordFilter extends BasicKeywordFilter {
    /**
     * Constructs a new InterfaceBasedKeywordFilter.
     * Adds a condition to check if the class implements the Keyword interface.
     */
    public InterfaceBasedKeywordFilter() {
        addCondition(new ImplementsKeywordInterfaceCondition());
    }

    /**
     * Checks if a class implements the Keyword interface.
     */
    private class ImplementsKeywordInterfaceCondition implements Condition {

        /**
         * Determines if the class implements the Keyword interface.
         * 
         * @param clazz the class to check
         * @return true if the class implements Keyword
         */
        public boolean check(Class clazz) {
            return Keyword.class.isAssignableFrom(clazz);
        }
    }
}
