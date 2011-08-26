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

package org.robotframework.javalib.beans.annotation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.keyword.DocumentedKeyword;
import org.robotframework.javalib.reflection.IKeywordInvoker;
import org.robotframework.javalib.reflection.KeywordInvoker;

public class AnnotationKeywordExtractor implements IKeywordExtractor<DocumentedKeyword> {
    public Map<String, DocumentedKeyword> extractKeywords(final Object keywordBean) {
        Map<String, DocumentedKeyword> extractedKeywords = new HashMap<String, DocumentedKeyword>();
        Method[] methods = keywordBean.getClass().getMethods();

        for (final Method method : methods) {
            if (method.isAnnotationPresent(RobotKeyword.class)) {
                DocumentedKeyword keyword = createKeyword(keywordBean, method);
                extractedKeywords.put(method.getName(), keyword);
            }
        }

        return extractedKeywords;
    }

    IKeywordInvoker createKeywordInvoker(Object keywordBean, Method method) {
        return new KeywordInvoker(keywordBean, method);
    }

    private DocumentedKeyword createKeyword(Object keywordBean, Method method) {
        IKeywordInvoker keywordInvoker = createKeywordInvoker(keywordBean, method);
        return createKeyword(keywordInvoker);
    }

    private DocumentedKeyword createKeyword(final IKeywordInvoker keywordInvoker) {
        return new DocumentedKeyword() {
            public Object execute(Object[] arguments) {
                return keywordInvoker.invoke(arguments);
            }

            public String[] getArgumentNames() {
                return keywordInvoker.getParameterNames();
            }

            public String getDocumentation() {
                return keywordInvoker.getDocumentation();
            }
        };
    }
}
