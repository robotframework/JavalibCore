package org.robotframework.javalib.mocks;

import org.robotframework.javalib.factory.KeywordFactory;
import org.robotframework.javalib.keyword.Keyword;


public class KeywordFactoryWithOneKeyword implements KeywordFactory {
    public String keywordName;
    public Keyword keywordInstance;
    public String createKeywordArgument;
    public boolean keywordCreated;
    public boolean keywordNamesRetrieved;

    public KeywordFactoryWithOneKeyword(String keywordname, Keyword keyword) {
        this.keywordName = keywordname;
        this.keywordInstance = keyword;
    }
    
    public Keyword createKeyword(String keywordName) {
        keywordCreated = true;
        createKeywordArgument = keywordName;
        if (this.keywordName.equals(keywordName)) {
            return keywordInstance;
        } else {
            return null;
        }
    }

    public String[] getKeywordNames() {
        keywordNamesRetrieved = true;
        return new String[] { keywordName };
    }
}
