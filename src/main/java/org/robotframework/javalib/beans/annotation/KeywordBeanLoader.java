/*
 * Copyright 2008 Nokia Siemens Networks Oyj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.robotframework.javalib.beans.annotation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.robotframework.javalib.beans.common.IClassFilter;
import org.robotframework.javalib.util.AntPathMatcher;
import org.robotframework.javalib.util.KeywordNameNormalizer;


public class KeywordBeanLoader implements IBeanLoader {
    protected final String keywordPattern;
    private final ClassLoader loader;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public KeywordBeanLoader(String keywordPattern, ClassLoader loader) {
        this.keywordPattern = keywordPattern;
        this.loader = loader;
    }

    public Map loadBeanDefinitions(IClassFilter classFilter) {
        Map kws = new HashMap<String, Object>();
        Enumeration<URL> entries = getRootResources();
        while (entries.hasMoreElements()) {
            try {
                addURLKeywords(classFilter, kws, entries.nextElement());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return kws;
    }

    private void addURLKeywords(IClassFilter classFilter, Map kws, URL url) throws IOException {
        if (url.getProtocol().startsWith("jar")) {
            addJarKeywords(classFilter, kws, url);
        } else if (url.getProtocol().startsWith("file")) {
            addFileKeywords(classFilter, kws, url);
        } else {
            throw new RuntimeException("Unsupported URL type "+url);
        }
    }

    private void addFileKeywords(IClassFilter classFilter, Map kws, URL url) throws IOException {
        File urlFile = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
        if (urlFile.isDirectory()) {
            for (String f: getChildrenFrom(pathMatcher.getRoot(keywordPattern), urlFile)) {
                addKeyword(classFilter, kws, f);
            }
        }
    }

    private void addJarKeywords(IClassFilter classFilter, Map kws, URL url) throws IOException {
        JarURLConnection connection =
                    (JarURLConnection) url.openConnection();
        File jar = new File(URLDecoder.decode(connection.getJarFileURL().getFile(), "UTF-8"));
        JarInputStream is = new JarInputStream(new FileInputStream(jar));
        JarEntry entry;
        while( (entry = is.getNextJarEntry()) != null) {
            if(entry.getName().endsWith(".class")) {
                addKeyword(classFilter, kws, entry.getName());
            }
        }
    }

    private Enumeration<URL> getRootResources() {
        String root = pathMatcher.getRoot(keywordPattern);
        try {
            return loader.getResources(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<String> getChildrenFrom(String root, File file) {
        ArrayList<String> classes = new ArrayList<String>();
        for (File f: file.listFiles()) {
            if (f.isFile()) {
                if (f.getName().endsWith(".class"))
                    classes.add(root + f.getName());
            } else
                classes.addAll(getChildrenFrom(root + f.getName() + "/", f));
        }
        return classes;
    }

    private void addKeyword(IClassFilter classFilter, Map<String, Object> kws, String className) throws IOException {
        if (className.indexOf("$")!=-1)
            return;
        if (className.startsWith("java/") || className.startsWith("javax/") )
            return;
        if (!pathMatcher.match(keywordPattern, className))
            return;
        String name = className.substring(0, className.length() - 6);
        Class cls = loadClass(name);
        if (classFilter.accept(cls))
            putInstance(kws, name, cls);
    }

    private void putInstance(Map<String, Object> kws, String name, Class cls) {
        try {
            kws.put(new KeywordNameNormalizer().normalize(name), cls.newInstance());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Class loadClass(String name) {
        try {
            return loader.loadClass(name.replace("/", "."));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
