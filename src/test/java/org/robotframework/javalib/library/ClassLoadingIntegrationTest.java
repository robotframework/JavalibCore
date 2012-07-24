package org.robotframework.javalib.library;

import junit.framework.TestCase;


public class ClassLoadingIntegrationTest extends TestCase {
    private MockClassLoader mockClassLoader;

    protected void setUp() throws Exception {
        mockClassLoader = new MockClassLoader();
    }

    public void testClassPathLibraryUsesProvidedClassLoaderForKeywordCreation() throws Exception {
        ClassPathLibrary library = createClassPathLibraryWithMockClassLoader();

        library.runKeyword("Empty Keyword", null);
        assertClassWasLoaded("org.robotframework.javalib.keyword.EmptyKeyword");
    }

    public void testClassPathLibraryUsesProvidedClassLoaderForKeywordExtraction() throws Exception {
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
