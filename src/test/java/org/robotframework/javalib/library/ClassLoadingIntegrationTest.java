package org.robotframework.javalib.library;

import junit.framework.TestCase;

import org.robotframework.javalib.factory.ApplicationContextKeywordFactory;
import org.robotframework.javalib.factory.CompositeKeywordFactory;

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

    public void testSpringLibraryUsesProvidedClassLoaderForKeywordCreation() throws Exception {
        SpringLibrary library = createSpringLibraryWithMockClassLoader();

        library.runKeyword("Keyword Wired From Spring", null);
        assertClassWasLoaded("org.robotframework.javalib.keyword.SpringKeyword");
    }

    public void testCompositeLibraryUsesProvidedClassLoaderForKeywordCreationWithASpringKeyword() throws Exception {
        CompositeLibrary library = createCompositeLibraryWithMockClassLoader();

        library.runKeyword("Keyword Wired From Spring", null);
        assertClassWasLoaded("org.robotframework.javalib.keyword.SpringKeyword");
    }

    public void testCompositeLibraryUsesProvidedClassLoaderForKeywordCreationWithAClassPathKeyword() throws Exception {
        CompositeLibrary library = createCompositeLibraryWithMockClassLoader();

        library.runKeyword("Empty Keyword", null);
        assertClassWasLoaded("org.robotframework.javalib.keyword.EmptyKeyword");
    }

    public void testCompositeLibraryUsesProvidedClassLoaderWithSpringKeywordFactory() throws Exception {
        CompositeLibrary library = createCompositeLibraryWithMockClassLoader();
        library.getKeywordNames();

        CompositeKeywordFactory compositeKeywordFactory = (CompositeKeywordFactory) library.createKeywordFactory();
        ApplicationContextKeywordFactory springKeywordFactory = (ApplicationContextKeywordFactory) compositeKeywordFactory.getKeywordFactories()[0];

        ClassLoader usedClassLoader = springKeywordFactory.getClassLoader();
        assertEquals(mockClassLoader, usedClassLoader);
    }

    public void testCompositeLibraryUsesProvidedClassLoaderWithClassPathKeywordFactory() throws Exception {
        CompositeLibrary library = createCompositeLibraryWithMockClassLoader();
        mockClassLoader.resetLists();

        library.runKeyword("Empty Keyword", null);
        assertClassWasLoaded("org.robotframework.javalib.keyword.EmptyKeyword");
    }

    public void testClassPathLibraryUsesProvidedClassLoaderForKeywordExtraction() throws Exception {
        ClassPathLibrary library = createClassPathLibraryWithMockClassLoader();

        library.getKeywordNames();
        assertProvidedClassLoaderWasUsedForSearching();
    }

    public void testSpringLibraryUsesProvidedClassLoaderForKeywordExtraction() throws Exception {
        SpringLibrary library = createSpringLibraryWithMockClassLoader();

        library.getKeywordNames();
        assertProvidedClassLoaderWasUsedForSearching();
    }

    public void testCompositeLibraryUsesProvidedClassLoaderForKeywordExtraction() throws Exception {
        CompositeLibrary library = createCompositeLibraryWithMockClassLoader();

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

    private CompositeLibrary createCompositeLibraryWithMockClassLoader() {
        CompositeLibrary library = new CompositeLibrary();
        library.setConfigFilePattern("org/**/keywords.xml");
        library.setKeywordPattern("org/**/keyword/**.class");
        library.setClassLoader(mockClassLoader);
        return library;
    }

    private ClassPathLibrary createClassPathLibraryWithMockClassLoader() {
        ClassPathLibrary library = new ClassPathLibrary();
        library.setKeywordPattern("org/**/keyword/**.class");
        library.setClassLoader(mockClassLoader);
        return library;
    }

    private SpringLibrary createSpringLibraryWithMockClassLoader() {
        SpringLibrary library = new SpringLibrary();
        library.setConfigFilePattern("org/**/keywords.xml");
        library.setClassLoader(mockClassLoader);
        return library;
    }
}
