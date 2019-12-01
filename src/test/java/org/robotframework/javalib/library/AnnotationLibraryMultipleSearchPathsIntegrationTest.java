package org.robotframework.javalib.library;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class AnnotationLibraryMultipleSearchPathsIntegrationTest {
    private AnnotationLibrary annotationLibrary;
    private String keywordThatReturnsItsArguments = "keywordThatReturnsItsArguments";

    @Test
    public void testFindsAnnotatedKeywordsFromClassPath() throws Exception {
    	List<String> searchPaths = new ArrayList<String>();
    	searchPaths.add("com/some/**/keyword/**/**.class");
    	searchPaths.add("my/same/keyword/**/**.class");
		annotationLibrary = new AnnotationLibrary(searchPaths);
    	try {
    		annotationLibrary.getKeywordNames();
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "Two keywords with name 'myFailingKeyword' found!");
			return;
		}
		fail("Excpected RuntimeException to be thrown");
    }

}
