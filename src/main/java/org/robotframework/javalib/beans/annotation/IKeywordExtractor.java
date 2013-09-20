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

import java.util.Collection;
import java.util.Map;

import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.library.AnnotationLibrary;

public interface IKeywordExtractor<T extends Keyword> {
	Map<String, T> extractKeywords(AnnotationLibrary library, Object keywordBean, Collection<Object> keywordBeanValues);
}
