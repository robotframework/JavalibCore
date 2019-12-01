package org.robotframework.javalib.library;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ClassLoadingIntegrationTest {
    private static MockClassLoader mockClassLoader;

    @BeforeAll
    public static void setUp() {
        mockClassLoader = new MockClassLoader();
    }

    @Test
    public void testClassPathLibraryUsesProvidedClassLoaderForKeywordCreation() {
        ClassPathLibrary library = createClassPathLibraryWithMockClassLoader();

        library.runKeyword("Empty Keyword", null);
        assertClassWasLoaded("org.robotframework.javalib.keyword.EmptyKeyword");
    }

    @Test
    public void testClassPathLibraryUsesProvidedClassLoaderForKeywordExtraction() {
        ClassPathLibrary library = createClassPathLibraryWithMockClassLoader();

        library.getKeywordNames();
        assertProvidedClassLoaderWasUsedForSearching();
    }

    private void assertProvidedClassLoaderWasUsedForSearching() {
        assertTrue(mockClassLoader.searchedResources.contains("org/"));
    }

    private void assertClassWasLoaded(String expectedClassToBeLoaded) {
        if (mockClassLoader.loadedClasses.size() < 1) {
            fail("0 classes loaded through custom class loader");
        }

        assertTrue(mockClassLoader.loadedClasses.contains(expectedClassToBeLoaded));
    }

    private ClassPathLibrary createClassPathLibraryWithMockClassLoader() {
        ClassPathLibrary library = new ClassPathLibrary();
        library.setKeywordPattern("org/**/keyword/**.class");
        library.setClassLoader(mockClassLoader);
        return library;
    }

}
