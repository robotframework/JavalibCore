package org.robotframework.javalib.factory;

import org.robotframework.javalib.beans.annotation.KeywordBeanLoader;
import org.robotframework.javalib.beans.classpath.InterfaceBasedKeywordFilter;
import org.robotframework.javalib.beans.common.BasicKeywordFilter;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.javalib.util.IKeywordNameNormalizer;
import org.robotframework.javalib.util.KeywordNameNormalizer;

import java.util.HashMap;
import java.util.Map;


public class ClassPathKeywordFactory implements KeywordFactory<Keyword> {

    private Map<String, Object> map;
    private IKeywordNameNormalizer keywordNameNormalizer = new KeywordNameNormalizer();

    public ClassPathKeywordFactory(KeywordBeanLoader loader) {
        Map<String, Object> kws = loader.loadBeanDefinitions(new InterfaceBasedKeywordFilter());
        this.map = remapNames(kws);
    }

    private Map<String, Object> remapNames(Map<String, Object> kws) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (String key: kws.keySet()) {
            String lastPart = getKwName(key);
            result.put(lastPart, kws.get(key));
        }
        return result;
    }

    private String getKwName(String name) {
        if (!name.contains("/"))
            return name;
        return  name.substring(name.lastIndexOf('/')+1);
    }

    public Keyword createKeyword(String keywordName) {
        return (Keyword)map.get(keywordNameNormalizer.normalize(keywordName));
    }

    public String[] getKeywordNames() {
        return map.keySet().toArray(new String[0]);
    }

}
