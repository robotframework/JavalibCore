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

package org.robotframework.javalib.beans.classpath;

import org.robotframework.javalib.beans.common.BasicKeywordFilter;
import org.robotframework.javalib.beans.common.BasicKeywordFilter.Condition;
import org.robotframework.javalib.keyword.Keyword;

/**
 * A filter that only accepts classes that<br/>
 *     - implement the {@link Keyword} interface<br/>
 *  - are public<br/>
 *  - are not abstract<br/>
 *  - have a default constructor<br/>
 */
public class InterfaceBasedKeywordFilter extends BasicKeywordFilter {
    public InterfaceBasedKeywordFilter() {
        addCondition(new ImplementsKeywordInterfaceCondition());
    }

    private class ImplementsKeywordInterfaceCondition implements Condition {
        public boolean check(Class clazz) {
            return Keyword.class.isAssignableFrom(clazz);
        }
    }
}
