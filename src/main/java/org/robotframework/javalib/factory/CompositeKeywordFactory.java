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
import java.util.List;

import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.collections.list.PredicatedList;
import org.apache.commons.collections.list.SetUniqueList;
import org.robotframework.javalib.keyword.CollisionKeyword;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.keyword.KeywordMap;


/**
 * A composite keyword factory that combines several keyword factories.
 * In case of a conflicting keyword name we register a
 * {@link CollisionKeyword} instead of either of the real implementations.
 * When executed, the collision keyword will notify the user of the
 * keyword name conflict.
 *
 * @see CollisionKeyword
 */
public class CompositeKeywordFactory implements KeywordFactory<Keyword> {
    private List keywordFactories;
    private KeywordMap keywordNamesAndTheirFactories;
    private KeywordMap collidingKeywords;

    public CompositeKeywordFactory() {
        keywordFactories = createUniqueListThatDoesntAcceptNullValues();
        keywordNamesAndTheirFactories = new KeywordMap();
        collidingKeywords = new KeywordMap();
    }

    /**
     * @see KeywordFactory#createKeyword(String)
     */
    public Keyword createKeyword(String keywordName) {
        if (isCollidingKeyword(keywordName)) {
            Keyword keyword = (Keyword) collidingKeywords.get(keywordName);
            return keyword;
        }

        KeywordFactory factory = getKeywordFactory(keywordName);
        return factory.createKeyword(keywordName);
    }

    /**
     * @see KeywordFactory#getKeywordNames()
     */
    public String[] getKeywordNames() {
        return keywordNamesAndTheirFactories.getKeywordNames();
    }

    /**
     * Adds a keyword factory. After this all the keywords in the given
     * factory can also be instansiated with this CompositeKeywordFactory.
     * Keyword name collision handling explained above in the class
     * description.
     *
     * @param factory factory to add
     */
    public synchronized void addKeywordFactory(KeywordFactory factory) {
        int factoryNumber = keywordFactories.size();
        addKeywordNamesToTable(factory, factoryNumber);
        keywordFactories.add(factory);
    }

    /**
     * Returns the keyword factories this instance combines
     *
     * @return array of keyword factories
     */
    public KeywordFactory[] getKeywordFactories() {
        return (KeywordFactory[]) keywordFactories.toArray(new KeywordFactory[0]);
    }

    private boolean isCollidingKeyword(String keywordName) {
        return collidingKeywords.containsKeyword(keywordName);
    }

    private KeywordFactory<Keyword> getKeywordFactory(String keywordName) {
        int factoryNumber = ((Integer) keywordNamesAndTheirFactories.get(keywordName)).intValue();
        KeywordFactory factory = (KeywordFactory) keywordFactories.get(factoryNumber);
        return factory;
    }

    private void handleDuplicateKeyword(KeywordFactory keywordFactory, String keywordName) {
        if (alreadyAConflictingKeyword(keywordName)) {
            // Ignore
            return;
        } else {
            KeywordFactory alreadyAddedKeywordFactory = getKeywordFactory(keywordName);
            Keyword keyword1 = alreadyAddedKeywordFactory.createKeyword(keywordName);

            Keyword keyword2 = keywordFactory.createKeyword(keywordName);

            collidingKeywords.add(keywordName, new CollisionKeyword(keyword1.getClass().toString(), keyword2.getClass().toString()));
        }
    }

    private boolean alreadyAConflictingKeyword(String keywordName) {
        return collidingKeywords.containsKeyword(keywordName);
    }

    private void addKeywordNamesToTable(KeywordFactory keywordFactory, int factoryNumber) {
        String[] keywordNames = keywordFactory.getKeywordNames();
        for(int i = 0; i < keywordNames.length; i++) {
            String keywordName = keywordNames[i];

            if (keywordAlreadyExists(keywordName)) {
                handleDuplicateKeyword(keywordFactory, keywordName);
                continue;
            } else {
                keywordNamesAndTheirFactories.add(keywordName, new Integer(factoryNumber));
            }
        }
    }

    private boolean keywordAlreadyExists(String keywordName) {
        return keywordNamesAndTheirFactories.containsKeyword(keywordName);
    }

    private static List createUniqueListThatDoesntAcceptNullValues() {
        List list = new ArrayList();
        list = SetUniqueList.decorate(list);
        list = PredicatedList.decorate(list, NotNullPredicate.INSTANCE);
        return list;
    }
}
