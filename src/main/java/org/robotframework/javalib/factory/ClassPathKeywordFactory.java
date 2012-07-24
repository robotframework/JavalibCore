package org.robotframework.javalib.factory;

import org.robotframework.javalib.beans.annotation.KeywordBeanLoader;
import org.robotframework.javalib.beans.classpath.InterfaceBasedKeywordFilter;
import org.robotframework.javalib.beans.common.BasicKeywordFilter;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.keyword.KeywordMap;
import org.robotframework.javalib.util.IKeywordNameNormalizer;
import org.robotframework.javalib.util.KeywordNameNormalizer;

import java.util.HashMap;
import java.util.Map;


public class ClassPathKeywordFactory implements KeywordFactory<Keyword> {

    private KeywordMap map = new KeywordMap();

    public ClassPathKeywordFactory(KeywordBeanLoader loader) {
        Map<String, Object> kws = loader.loadBeanDefinitions(new InterfaceBasedKeywordFilter());
        remapNames(kws);
    }

    private void remapNames(Map<String, Object> kws) {
        for (String key: kws.keySet()) {
            String lastPart = getKwName(key);
            map.add(lastPart, kws.get(key));
            // FIXME: test for duplicates
        }
    }

    private String getKwName(String name) {
        if (!name.contains("/"))
            return name;
        return  name.substring(name.lastIndexOf('/')+1);
    }

    public Keyword createKeyword(String keywordName) {
        return (Keyword)map.get(keywordName);
    }

    public String[] getKeywordNames() {
        return map.getKeywordNames();
    }

}
