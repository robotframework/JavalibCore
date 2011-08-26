package org.robotframework.javalib.library;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;


public class MockClassLoader extends URLClassLoader {
    public ArrayList loadedClasses = new ArrayList();
    public ArrayList searchedResources = new ArrayList();

    public MockClassLoader() {
        super(new URL[0], Thread.currentThread().getContextClassLoader());
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        loadedClasses.add(name);
        return super.loadClass(name);
    }

    public Enumeration findResources(String name) throws IOException {
        searchedResources.add(name);
        return super.findResources(name);
    }

    public void resetLists() {
        loadedClasses.clear();
        searchedResources.clear();
    }
}
