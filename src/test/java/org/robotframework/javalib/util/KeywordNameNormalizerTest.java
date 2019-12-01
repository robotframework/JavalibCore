package org.robotframework.javalib.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeywordNameNormalizerTest {

    @Test
    public void testNormalizesWhiteSpacesUnderScoresAndUppercaseCharacters() {
        IKeywordNameNormalizer normalizer = new KeywordNameNormalizer();
        String normalized = normalizer.normalize("sOmE string\tWI TH\rwHitespa ce\nandnewlinesandUnder_Scores");
        assertEquals("somestringwithwhitespaceandnewlinesandunderscores", normalized);
    }
}
