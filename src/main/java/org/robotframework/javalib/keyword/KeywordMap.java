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

package org.robotframework.javalib.keyword;

import java.util.Map;

import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.collections.functors.TruePredicate;
import org.apache.commons.collections.functors.UniquePredicate;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections.map.PredicatedMap;

/**
 * A data structure for keywords and related values, such as instances or
 * class names. Keyword names must be unique after normalization. Names
 * and values have to be non null.
 */
public class KeywordMap {
    private Map map;

    public KeywordMap() {
        map = new HashedMap();
        map = PredicatedMap.decorate(map, UniquePredicate.getInstance(), TruePredicate.INSTANCE);
        map = PredicatedMap.decorate(map, NotNullPredicate.INSTANCE, NotNullPredicate.INSTANCE);
    }

    /**
     * Adds a keyword to the map. Name will be normalized.
     * 
     * @param keywordName name to be added
     * @param value associated value
     */
    public void add(String keywordName, Object value) {
        map.put(normalizeKeywordName(keywordName), value);
    }
    
    /**
     * Gets the value associated with given keyword name. Keyword name
     * is normalized before searching.
     * 
     * @param keywordName keyword name
     * @return associated value
     */
    public Object get(String keywordName) {
        return map.get(normalizeKeywordName(keywordName));
    }
    
    /**
     * Normalizes a keyword name. Removes spaces and special characters.
     * Converts all letters to lower case.
     * 
     * @param keywordName keyword name
     * @return normalized keyword name
     */
    public static String normalizeKeywordName(String keywordName) {
        if (keywordName == null) {
            return null;
        }
        keywordName = keywordName.toLowerCase().trim();
        keywordName = keywordName.replaceAll(" ", "");
        keywordName = keywordName.replaceAll("_", "");
        keywordName = keywordName.replaceAll("\t", "");
        keywordName = keywordName.replaceAll("\r", "");
        keywordName = keywordName.replaceAll("\n", "");
        return keywordName;
    }
    
    /**
     * Amount of pairs in map
     * 
     * @return amount of pairs in map
     */
    public int size() {
        return map.size();
    }
    
    /**
     * Returns the keyword names. Similar to {@link Map#keySet()}.
     * 
     * @return array of keyword names
     */
    public String[] getKeywordNames() {
        return (String[]) map.keySet().toArray(new String[0]);
    }
    
    /**
     * Checks whether map contains a pair with given keyword name
     * 
     * @param keywordName keyword name
     * @return true if pair exists, false otherwise
     */
    public boolean containsKeyword(String keywordName) {
        return map.containsKey(normalizeKeywordName(keywordName));
    }

    /**
     * Returns the underlying Map instance.
     * 
     * @return underlying predicated HashedMap
     */
    protected Map getUnderlyingMap() {
        return map;
    }
}
