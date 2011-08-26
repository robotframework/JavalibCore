package org.robotframework.javalib.robottests;

import org.robotframework.javalib.keyword.KeywordNameCollisionException;
import org.robotframework.javalib.library.CompositeLibrary;


public class JavalibCoreTestLibrary {
    public String runConflictingKeywordWithCompositeLibrary(String keywordName) {
        CompositeLibrary library = new CompositeLibrary("org/**/keyword/**/**.class", "org/**/keywords.xml");
        try {
            library.runKeyword(keywordName, null);
            throw new RuntimeException("Keyword should've thrown an exception");
        } catch(KeywordNameCollisionException e) {
            return e.getMessage();
        }
    }
}
