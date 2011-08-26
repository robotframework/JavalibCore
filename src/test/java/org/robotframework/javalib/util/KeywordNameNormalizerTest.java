package org.robotframework.javalib.util;

import org.robotframework.javalib.util.IKeywordNameNormalizer;
import org.robotframework.javalib.util.KeywordNameNormalizer;

import junit.framework.TestCase;

public class KeywordNameNormalizerTest extends TestCase {
    public void testNormalizesWhiteSpacesUnderScoresAndUppercaseCharacters() throws Exception {
        IKeywordNameNormalizer normalizer = new KeywordNameNormalizer();
        String normalized = normalizer.normalize("sOmE string\tWI TH\rwHitespa ce\nandnewlinesandUnder_Scores");
        assertEquals("somestringwithwhitespaceandnewlinesandunderscores", normalized);
    }
}
