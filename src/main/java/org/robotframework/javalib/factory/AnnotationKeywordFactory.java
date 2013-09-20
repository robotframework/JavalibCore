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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.robotframework.javalib.beans.annotation.AnnotationKeywordExtractor;
import org.robotframework.javalib.beans.annotation.IKeywordExtractor;
import org.robotframework.javalib.keyword.DocumentedKeyword;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.javalib.util.IKeywordNameNormalizer;
import org.robotframework.javalib.util.KeywordNameNormalizer;

public class AnnotationKeywordFactory implements KeywordFactory<DocumentedKeyword> {
	private Map<String, DocumentedKeyword> keywords = new HashMap<String, DocumentedKeyword>();
	private IKeywordNameNormalizer keywordNameNormalizer = new KeywordNameNormalizer();
	private List<String> keywordNames = new ArrayList<String>();

	public AnnotationKeywordFactory(AnnotationLibrary library, Map<String, Object> keywordBeansMap) {
		extractKeywordsFromKeywordBeans(library, keywordBeansMap);
	}

	public AnnotationKeywordFactory(AnnotationLibrary library, List<Map> keywordBeansMaps) {
		for (Map<String, Object> keywordBeansMap : keywordBeansMaps) {
			extractKeywordsFromKeywordBeans(library, keywordBeansMap);
		}
	}

	public DocumentedKeyword createKeyword(String keywordName) {
		String normalizedKeywordName = keywordNameNormalizer.normalize(keywordName);
		return keywords.get(normalizedKeywordName);
	}

	public String[] getKeywordNames() {
		return (String[]) keywordNames.toArray(new String[0]);
	}

	protected void extractKeywordsFromKeywordBeans(AnnotationLibrary library, Map<String, Object> keywordBeansMap) {
		Collection<Object> keywordBeanValues = keywordBeansMap.values();
		IKeywordExtractor<DocumentedKeyword> keywordExtractor = createKeywordExtractor();

		for (Object keywordBean : keywordBeanValues) {
			Map<String, DocumentedKeyword> extractedKeywords = keywordExtractor.extractKeywords(library, keywordBean,
					keywordBeanValues);
			addKeywordNames(extractedKeywords);
			addKeywords(extractedKeywords);
		}
	}

	IKeywordExtractor<DocumentedKeyword> createKeywordExtractor() {
		return new AnnotationKeywordExtractor();
	}

	private void addKeywords(Map<String, DocumentedKeyword> extractedKeywords) {
		for (String keywordName : extractedKeywords.keySet()) {
			handleDuplicateKeywordNames(keywordName);
			keywords.put(keywordNameNormalizer.normalize(keywordName), extractedKeywords.get(keywordName));
		}
	}

	private void handleDuplicateKeywordNames(String keywordName) {
		if (keywords.containsKey(keywordNameNormalizer.normalize(keywordName))) {
			throw new RuntimeException("Two keywords with name '" + keywordName + "' found!");
		}
	}

	private void addKeywordNames(Map<String, DocumentedKeyword> extractedKeywords) {
		keywordNames.addAll(extractedKeywords.keySet());
	}
}
